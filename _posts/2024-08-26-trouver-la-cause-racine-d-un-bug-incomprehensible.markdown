---
date:      2024-08-26 22:49:55 +0200
layout:    post
title:     "Trouver la cause racine d’un bug incompréhensible"
img_large: 2024-08-26-capture-ecran-diff-github.png
---

Je vais revenir dans cet article sur un bug survenu lors de la génération d’un contrat openapi par une librairie _code
first_ — [Tapir] — qu’on utilise pour une api interne. Ce sera l’occasion d’analyser la manière dont les développeurs
creusent un sujet qui parait incompréhensible de premier abord.

### Constat

Première chose à faire : être attentif à l’inattendu. Ici, on aurait pu décider de ne pas s’émouvoir de la différence
constatée sur le code généré par [Tapir] au niveau du contrat [OpenAPI] :

```diff
components:
  schemas:
    Entity:
      title: Entity
      oneOf:
      - $ref: '#/components/schemas/Organization'
-     - $ref: '#/components/schemas/Person'
+     - $ref: '#/components/schemas/Person1'
      discriminator:
        propertyName: kind
        mapping:
          organization: '#/components/schemas/Organization'
-         person: '#/components/schemas/Person'
+         person: '#/components/schemas/Person1'
```

Cette modification de référence de schéma pourrait ne pas être si important que cela si d’autres outils comme
[OpenAPI Generator] ne s’en servaient pour générer les types dans des langages de programmation cible fortement typés et
compilés.

On constate donc que le nom de cette référence semble suffixé avec un `1`. La prochaine étape est de comprendre pourquoi
cela se produit et si on a la possibilité de changer ce comportement.

### Tâtonnement

[Tapir] documente et offre plusieurs manières de contrôler la génération du schéma OpenAPI. L’inconvénient, c’est que ça
fonctionne à base de macros opaques.

On a donc perdu pas mal de temps à vouloir modifier le nom de cette référence, mais sans succès.

Puis, nous nous sommes focalisés sur les changements récents dans notre base de code. Malgré le fait que le contrat est
volumineux, car le nombre de endpoints et de types associés est élevé, nous avons tout de même eu l’intuition que
c’était quand même la première fois qu’on utilisait le type à la fois dans un type produit et dans un type somme (avec
`discriminator`). Avec ceci en tête, il apparaissait clairement qu’on devait trouver la preuve que c’est bien cette
nouvelle caractéristique qui introduit le suffixe `1`.

Pour cela, nous avons essayé de trouver des indices dans :
 - la documentation
   > Suffixes might be added at a later stage to disambiguate between different schemas with same names.
 - lire le code source et étudier l’histoire du projet
   - rechercher le code qui ajoute `1` n’a rien donné
   - rechercher et trouver [le commit] qui a introduit le laïus ci-dessus : échec. Il s’agit d’un commit spécifique à la
     documentation, non liée au code, et sans [pull request] ni [issue] associée.
   - rechercher spécifiquement les caractères `1’$` dans tous les fichiers `*.yml` ou `*.yaml` du projet : Eureka !

### Dénouement

Grâce à cette recherche spécifique des caractères `1’$` dans tous les fichiers `*.yml` ou `*.yaml` du projet, on finit
par mettre la main sur la règle qui a été implémentée et qui est garantie par un test de non-régression.

Voici l’[issue][found-issue] ainsi que [la pull request][found-pull-request] qui nous prouvent qu’en cas d’usage d’un
type de manière directe (sans _discriminator_) mais aussi utilisé en tant que type polymorphique (avec _discriminator_),
le schéma OpenAPI doit dupliquer la définition de ce type en deux afin d’introduire le _discriminator_ comme attribut
obligatoire du type pour l’une des deux définitions. [Tapir] résout ce problème de duplication en rajoutant ce fameux
suffixe `1` derrière le second. L’ordre est également arbitrairement défini par l’usage des types lors de la déclaration
progressif des endpoints les uns après les autres.

[found-issue]: https://github.com/softwaremill/tapir/pull/2376
[found-pull-request]: https://github.com/softwaremill/tapir/pull/2376
[issue]: https://github.com/softwaremill/tapir/issues
[le commit]: https://github.com/softwaremill/tapir/commit/53e3a588267d15fa012c1ee8ec99a90ca082be54
[OpenAPI]: https://www.openapis.org
[OpenAPI Generator]: https://openapi-generator.tech
[pull request]: https://github.com/softwaremill/tapir/pulls
[Tapir]: https://tapir.softwaremill.com
