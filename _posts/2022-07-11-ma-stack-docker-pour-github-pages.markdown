---
layout: post
title:  "Ma stack Docker pour GitHub Pages"
date:   2022-07-11 06:42:00 +0200
tags:   Techno
---

Ce site est publié en utilisant [GitHub Pages][github-pages]. Jusqu'à récemment, je maintenais les outils nécessaires à
l'édition de markdown et la publication en local pour vérifier que mes articles se rendaient correctement avant de les
publier sur GitHub. C'était aussi pour moi l'occasion d'effleurer l'écosystème [ruby][ruby] et ses outils de build.

Sauf que c'était assez contraignant et en passant à une puce Apple, de nouveaux problèmes sont apparus.

J'ai donc contenerisé le site web et ça fonctionne merveilleusement bien. Petit tour d'horizon des commandes que
j'utilise.

## Création d'un volume

Les librairies utilisées par le système de build de [ruby][ruby] s'appellent des gems. Nous allons les stocker dans un
volume Docker :

```shell
docker volume create gem_home
```

## Installation de [Jekyll][jekyllrb] et ruby

Grâce à la page de référence [GitHub Pages dependency versions][github-pages-versions], on peut déterminer
précisément les bonnes versions de [ruby][ruby] et [Jekyll][jekyllrb] utilisées par [GitHub Pages][github-pages] - ce ne
sont pas les dernières versions.

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle ruby:2.7.3 gem install jekyll --version 3.9.2
```

## Création d'un site

On va créer un nouveau site sans installer les bundles car nous avons besoin d'adapter légèrement la configuration par
défaut de [Jekyll][jekyllrb] à [GitHub Pages][github-pages]:

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site ruby:2.7.3 jekyll new --skip-bundle /usr/site
```

## Adaptation du site pour GitHub Pages

En suivant les recommandation, il est nécessaire de commenter `gem "jekyll", "~> 3.9.2"` and décommenter
`gem "github-pages", group: :jekyll_plugins` dans le fichier `"$(pwd)"/Gemfile`.

Une fois que c'est fait, on peut installer les dépendences :

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.3 bundle install
```

## Tester son site en local

Il ne reste plus qu'à lancer [Jekyll][jekyllrb] :

```shell
docker run --name ruby --publish 4000:4000 --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.3 bundle exec jekyll serve --drafts --host=0.0.0.0
```

## Mise à jour des dépendances

Pour rester à jour sur les dépendances transitives, on peut lancer cette commande :

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.3 bundle update
```

## Analyser les différences si GitHub Pages mettait à jour ses dépendances

On a déjà vu que la page [GitHub Pages dependency versions][github-pages-versions] permettait de savoir sur quelles
versions de [ruby][ruby] et de [Jekyll][jekyllrb] GitHub Pages tournait. Si jamais GitHub décidait de se mettre à jour, on pourrait
adapter notre site aux nouvelles versions en générant à nouveau un site à côté :

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.3 bundle exec jekyll new test
```

Et ainsi comparer la configuration actuelle avec la nouvelle par défaut.

Le code du thème aussi peut être comparé en allant chercher les fichiers au moyen de cette commande :

```shell
$ ls -l $(bundle info jekyll-theme-hacker | grep "Path:" | grep --only-matching "/.*")
total 32
-rw-r--r--  1 ***  staff  6555 Jun 13 22:27 LICENSE
-rw-r--r--  1 ***  staff  4814 Jun 13 22:27 README.md
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 _layouts
drwxr-xr-x  5 ***  staff   170 Jun 13 22:27 _sass
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 assets
```

[github-pages]: https://pages.github.com
[github-pages-versions]: https://pages.github.com/versions
[jekyllrb]: https://jekyllrb.com
[ruby]: https://www.ruby-lang.org