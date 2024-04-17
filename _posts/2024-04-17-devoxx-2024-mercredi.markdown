---
layout:    post
date:      2024-01-25 21:37:00 +0100
title:     "Mon feedback sur Devoxx 2024 - mercredi"
img_large: 2024-04-17-badge-devoxx.jpg
---

Ça faisait assez longtemps que je n'avais pas participé à une conférence et l'occasion s'est présentée pour moi de
participer à l'édition 2024 de Devoxx. Je vais donc revenir dans ce billet sur les différents talks auxquels j'ai choisi
d'assister.

### [Keynote]

C'est un médecin qui nous a parlé d'IA en faisant une petite rétrospective des avancées et impacts de ce nouveau champ
des possibles dans sa discipline. Il est l'auteur de certains des papiers qu'il a présentés donc la légitimité pour en
parler était bien là. Il a surtout insisté sur le rapport coût/bénéfice de ces nouvelles techniques : on gagne du temps
sur l'analyse, mais les risques liés aux biais d'entrainement et la perte de connaissance sont nombreux.

### [Butcher Virtual Threads like a pro!] par Piotr Przybyl

Cette présentation en anglais était assez rigolote et également super intéressante : comment éviter les pièges que nous
tendent les virtual threads du project Loom disponibles en Java 21. La conférence était truffées de démos en live
coding. Ça allait vite, il fallait suivre, mais on finissait toujours par être convaincus. Il y a des choses à retenir
de ce que nous a démontré Piotr dans ses démos.

Non les virtual threads ne vous donnent pas plus de puissance de calcul.

Pour comprendre comment ils interagissent avec les threads OS, la métaphore du taxi a été employée et elle fonctionne
super bien. Dans une ville un peu dense, il est impossible que chaque habitant possède sa propre voiture pour se
déplacer : cela n'aurait pas de sens et provoquerait des embouteillages perpétuels. Donc des companies de taxis sont là
pour mettre à disposition un nombre limité de taxis afin que les habitants puissent les utiliser. Les taxis sont les
threads OS, les habitants sont les green threads.

Oublions nos vieux réflexes : les virtual threads ne se réutilisent pas, on ne les range pas dans des pools, on ne les
pinnent pas. Sur ce dernier point, il est intéressant de pouvoir détecter ce comportement. Il est tout-à-fait possible
de rencontrer ce cas de figure, car les virtual threads possèdent la même api que leurs homologues old school. Ainsi si
on décide de changer l'implémentation dans un client HTTP par exemple, on peut détecter si notre code ou celui de
librairies tierces pinnent les threads grâce à la mise en place d'un [proxy toxique] qui introduit une latence aléatoire
et d'un flag de la JVM qui log les cas de pinned virtual threads.

Piotr nous a également montré que le simple fait d'introduire du monitoring pour comprendre ce qu'il se passe pouvait
totalement changer le comportement initial.

Enfin le mécanisme du ThreadLocal est remplacé par la notion de scoped value. Ça fonctionne bien, mais à condition que
les valeurs qu'on y range soient immuables.

### [High-speed DDD] par Thomas Pierrain

Dans cette présentation qui a eu lieue dans le grand amphi, Thomas pierrain nous a montré trois exemples d'applications
de patterns stratégiques du DDD avec une bonne mise en contexte à chaque fois. De prime abord, j'ai même trouvé que
l'introduction des problématiques métiers prenait pas mal de temps et qu'il semblait s'y attarder un peu trop. Mais en
fait s'est complètement logique : après tout on est dans un talk DDD. Et dans **DDD**, il y a le **D** de Domain.

Pour justifier que ces patterns ne soient pas parfaits, Thomas nous a rappelé le concept de dette produit. Il n'était
pas coutumier de cette notion car il travaillait précédemment dans des sociétés qui n'étaient ni des startups, ni des
scale-ups.

Premier cas concret : le produit refuse d'introduire une nouvelle notion métier car il estime que c'est trop coûteux.
Qu'à cela ne tienne, un _bounded context_ caché est créé pour sous-tendre les calculs d'estimations de TVA. Puisque ce
nouveau domaine est caché, il suscite tout naturellement de nombreuses questions de la part des clients. L'équipe de dev
met en place une page d'exposition dédiée sur ce domaine pour le support et cela fini par terminer dans le produit. La
boucle est bouclée.

Le second cas concret est la nécessité d'intégrer des données d'un tout nouveau domaine qui n'est pas encore stable dans
l'un des domaines phares de l'application : le plan de trésorerie. Plutôt que de proposer une intégration profonde à
base d'Event Driven Architecture et de stoquage de cette donnée non stable et difficile à intégrer au domaine du plan de
trésorie, le choix a été fait d'écrire une couche d'_Anti-Corruption Layer_ _ombilical_ : le modèle de données en base
ne change pas - on n'intègre la donnée qu'au moment de l'exposer à l'extérieur. Le problème de l'intégration peu
naturelle reste présent mais on évite au moins des migrations de bases de données, de tordre un modèle de persistance et
de subir toutes les mises à jour du cycle de vie de ces entitées d'un autre domaine.

Enfin le dernier cas est plus classique : une table centrale qui est trop couplée à l'existant et dont tout le monde
dépend. Ici, on a choisi de créer un chemin spécifique pour l'écriture. Cela a permis de simplifier les accès qui ne se
faisaient majoritairement qu'en lecture.

La conclusion s'est faite en deux points :

1. Comment vendre le concept de dette technique ? Utiliser la métaphore du réseau routier à entretenir avec ses routes
   qui se dégradent, ses voies qui sont trop empruntées, etc.
2. Pour palier à une communication difficile avec le produit, il recommande de plus collaborer et notamment en
   encourageant les devs à prendre en charge une bonne partie de l'activité de discovery avec des réalisations de POC en
   amont.

### [Canon TDD] par Anis Chaabani

Ce talk d'un petit quart d'heure a été l'occasion de relayer un [blog post de Kent Beck] sur la pratique du TDD. Les
point que je ne connaissais pas sont les suivants :

1. Avant de commencer un dev, commencer par établir une liste de tests qu'on a en tête.
2. Le choix du prochain test à écrire est difficile et ne vient que par la pratique. Il faut réussir à sentir ce genre
   de choses et ne pas hésiter à revenir en arrière si on pense que l'itération suivante est trop complexe.
3. Astuce : quand on écrit un nouveau test, la préconisation est de commencer par les assertions, puis _remonter_ au
   _act_ puis au _arrange_.
4. Vocabulaire : les trois phases d'un test peuvent s'appeler _Given_, _When_, _Then_ tout comme _Arrange_, _Act_,
   _Assert_. La force de l'habitude me ferai préférer le premier au second.

Pour rappel, Kent Benk nous dit bien que le TDD n'est qu'une technique de développement dont l'intérêt principal est de
faire diminuer la charge cognitive.

### [Passer d'une application Flutter mobile à une application Web de production en 2 mois, c'est possible !] par Michaël Ohayon

De prime abord hypé par le fait que je suis un utilisateur d'edenred (en tant que possesseur d'une carte Ticket
Restaurant), je me suis finalement rendu compte qu'il s'agissait en fait d'une autre application d'edenred basée sur les
coupons de réductions offerts par l'intermédiaire des CE. Quoi qu'il en soit, ce talk était assez intéressant car il
rentrait dans les détails d'une migration qui apporte plus de portabilité et d'ouverture il me semble : le portage de
l'application native Flutter en Flutter web. En effet, quoi de plus universel qu'une application web plutôt qu'une
application mobile liée à un store ? On a donc pu aborder avec Michaël certaines des problématiques que posent le
passage au web : certaines librairies ne sont pas tout-à-fait prêtes pour le web ; il faut débrancher certaines
fonctionnalités natives chez soi ; l'utilisation d'un design system est un must have ; le state de l'application est à
revoir car l'utilisateur peut à tout moment décider de rafraichir la page.

Je ne connais que de nom Flutter mais ce qui m'a le plus impressionné avec cette présentation est leur volonté de ne
garder qu'une seule base de code commune avec des targets aussi diverses que web, natif Android et natif iOS.

### [Histoire de l'ALTO et du Xerox PARC] par Marc Chevaldonné

Il ne serait pas très utile de détailler ici la somme d'informations historiques très documentées et apportées avec
humour par Marc lors de cette conférence. Je me contente donc de restituer les points qui m'ont le plus marqué :

1. L'esprit hippie qui régnait à l'époque au Xerox PARC et qui leur a permis d'innover tout en reniant le profit et
   l'aspect commercial
2. Les pionniers étaient capables de prouesses avec si peu. Aujourd'hui on dispose à la fois d'un savoir accessible par
   le biais d'Internet mais aussi une _complexité accidentelle_ non négligeable par rapport à nos ainés (OS, kube,
   interfaces graphiques web, etc.)
3. Revoir le film [Les Pirates de la Silicon Valley]

### [Infuser du métier dans les autorisations avec ReBAC] par Geoffroy Braun et Pauline Jamin

Cette conférence a été super instructive, car elle m'a permis de prendre conscience que les autorisations pouvaient
cacher une complexité telle qu'elles justifient parfois de les externaliser dans un domaine à part entière. Cette
démonstration a été faite durant la première partie du talk. Après nous avoir classiquement montré ce que font les
géants du web sur le sujet - Google a publié un article en 2019 : [Zanzibar] ; les speakers nous ont démontré comment
ils ont mis en place un équivalent open source : [OpenFGA]. J'émet juste une réserve sur la cardinalité des objets qu'on
doit potentiellement importer et maintenir en cohérence dans ce système déporté si notre modèle d'authorisation est
couplé à de nombreuses entités (les utilisateurs, les groupes, les objets du domaine, etc.).

### Multicloud et on premise

TODO

### La recherche sous stéroide

TODO

### réduisez votre coût d'entrée avec Nix

TODO

[Keynote]: https://www.devoxx.fr/schedule/talk/?id=74352
[Butcher Virtual Threads like a pro!]: https://www.devoxx.fr/schedule/talk/?id=44904
[proxy toxique]: https://github.com/Shopify/toxiproxy
[High-speed DDD]: https://www.devoxx.fr/schedule/talk/?id=44937
[Canon TDD]: https://www.devoxx.fr/schedule/talk/?id=21868
[blog post de Kent Beck]: https://tidyfirst.substack.com/p/canon-tdd
[Passer d'une application Flutter mobile à une application Web de production en 2 mois, c'est possible !]: https://www.devoxx.fr/schedule/talk/?id=13355
[Histoire de l'ALTO et du Xerox PARC]: https://www.devoxx.fr/schedule/talk/?id=44911
[Les Pirates de la Silicon Valley]: https://fr.wikipedia.org/wiki/Les_Pirates_de_la_Silicon_Valley
[Infuser du métier dans les autorisations avec ReBAC]: https://www.devoxx.fr/schedule/talk/?id=42356
[Zanzibar]: https://research.google/pubs/zanzibar-googles-consistent-global-authorization-system
[OpenFGA]: https://openfga.dev