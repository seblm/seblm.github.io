---
layout:    post
title:     "Des nouvelles — techniques — de mon blog"
date:      2023-09-06 19:31:00 +0200
img_large: 2023-09-06-dark-theme.png
---

Comme d’habitude quand on veut améliorer le fond, tout bon développeur préfère [tondre le yak][yak-shaving] et améliorer
la forme. Ma récente reprise de motivation à écrire et contribuer sur Internet s’est très naturellement transformée en
améliorations et changements divers de mon blog. J’expose ces changements dans cet article.

## TLDR; ce qu’il faut retenir ##

Mon site a un thème _dark_ si votre OS est en _dark_ ! Merci les [skins de minima][minima-skins] et la media query
[prefers-color-scheme].

## Les détails ##

Premièrement, je ne me rappelais plus que j’avais apporté quelques modifications au thème de base de Jekyll — [minima] :

| Date       | Description                                                                                                                                                                                                                                                                     |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 21/06/2018 | [Les détails en 2018][blog-details]                                                                                                                                                                                                                                             |
| 18/10/2022 | Je migrais du [thème hacker][hacker] vers [minima].<br/>J’en profitais pour introduire des images d’illustration pour chaque article que j’affichais sur la page d’accueil.                                                                                                     |
| 07/11/2022 | [Passage à Docker][blog-docker]                                                                                                                                                                                                                                                 |
| 20/11/2022 | Je supprimais mon compte X — ça s’appelait encore Twitter à l’époque — et configurais Jekyll pour linker mon compte [Mastodon][mastodon].<br/>Cela nécessitait d’ajouter `rel="me"` au lien vers Mastodon afin de permettre le [Link verification][mastodon-link-verification]. |
| 31/08/2023 | Alors que bon nombre d’articles définissaient une variable [Front Matter][front-matter] nommée `noExcerpt`, elle n’était pas utilisée sur la page d’accueil.                                                                                                                    |

Par la suite, je suis tombé un peu par hasard sur [la page de démo de minima][minima]. Et là j’ai été fortement attiré
par une liste d’options permettant de choisir son skin. Mais quoi ? [minima] propose maintenant une notion de
[skin][minima-skins], parmi lesquels l’un d’entre eux se nomme _dark_ ? _Ok c’est bon je migre vers la dernière version_
fut ma réaction instinctive.

S’est donc posé la question du **comment ?**. Plusieurs stratégies étaient possibles :

- Rester sur ce que je faisais jusque-là : tirer la dépendance à la dernière version de [minima] grâce à `remote_theme`
  et copier les fichiers du thème dans mon site. Cette solution à base de copier/coller n’était évidemment pas la bonne.
- Se résigner à ne pas pouvoir mettre à jour le thème vers sa dernière version.
- [Forker minima][minima-fork] pour [tracker les changements][minima-fork-changes] apportés tout en séparant clairement
  le site de son thème. C’est cette solution que j’ai choisie.

En bonus en plus du thème dark si votre OS l’est, j’ai activé la pagination et de dois avouer que c’est plutôt salvateur
à cause de mes grosses images d’articles.

J’ai donc pour l’instant [apporté 4 changements][minima-fork-changes] à la dernière version de [minima]. Je détaille ici
les changements que je n’ai pas déjà décrits plus haut :

1. Renommer `master` en `main` : qui ose encore appeler sa branche principale `master` en 2023 ?
2. Déplacer la description du site dans l’entête — ma description est vraiment très succincte — et supprimer le lien
   vers le RSS car il est déjà présent dans la liste des liens _sociaux_ juste un peu plus bas.

L’avantage du fork, c’est que je pourrais toujours _rebaser_ la branche de base et apporter mes changements par-dessus
— ainsi, je reste à jour au niveau du thème.

[blog-details]: {% post_url 2018-06-21-jekyll-a-jour %}
[blog-docker]: {% post_url 2022-07-11-ma-stack-docker-pour-github-pages %}
[front-matter]: https://jekyllrb.com/docs/front-matter
[hacker]: https://pages-themes.github.io/hacker
[mastodon]: https://piaille.fr/@seblm
[mastodon-link-verification]: https://docs.joinmastodon.org/user/profile/#verification
[minima]: https://jekyll.github.io/minima
[minima-fork]: https://github.com/seblm/minima
[minima-fork-changes]: https://github.com/jekyll/minima/compare/master...seblm:minima:main
[minima-skins]: https://github.com/jekyll/minima/#skins
[prefers-color-scheme]: https://developer.mozilla.org/en-US/docs/Web/CSS/@media/prefers-color-scheme
[yak-shaving]: https://www.commitstrip.com/fr/2013/09/24/yen-a-pour-2-minutes
