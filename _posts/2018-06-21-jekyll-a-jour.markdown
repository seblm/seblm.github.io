---
layout: post
title:  "Jekyll à jour"
date:   2018-06-21 03:19:54 +0200
categories: Techno
tags: oui
---
J'ai enfin pris le temps de mettre à jour mon blog. L'objectif va être de poster des articles un peu plus régulièrement.

Voici le résumé des actions que j'ai réalisées :

 -  Je voulais absolument pouvoir builder le site en local donc j'ai suivi [la doc][github-doc], lancé jekyll en local
    et réparé les warnings.
 -  Ensuite j'ai créé un nouveau site à partir de zéro pour mesurer les différences avec mon site actuel. J'ai gagné un
    `.gitignore` ou une page 404 par exemple. J'en ai profité pour changer de thème : je suis passé d'un vieux thème
    écrit à la main et hérité [d'un moteur de blog français][dotclear] à [jekyll-theme-hacker]. L'import d'un thème
    publié en gem se fait très simplement en spécifiant la proriété `theme` dans `_config.yml`.
 -  Enfin j'ai réagi à un warning émis par github et reçu par mail qui m'indiquait que le plugin de coloration
    syntaxique n'avait pas la bonne valeur. Github utilise uniquement [Rouge][pourquoi-rouge].

J'ai découvert quelque chose d'assez simple pour _débugger_ le rendu des articles avec les templates d'un thème
externe : la commande `bundle show` permet de connaitre le chemin où est installé une dépendance. Exemple : 

{% highlight bash %}
$ ls -l $(bundle show jekyll-theme-hacker)
total 32
-rw-r--r--  1 user  staff  6555 Jun 13 22:27 LICENSE
-rw-r--r--  1 user  staff  4814 Jun 13 22:27 README.md
drwxr-xr-x  4 user  staff   136 Jun 21 03:06 _layouts
drwxr-xr-x  5 user  staff   170 Jun 13 22:27 _sass
drwxr-xr-x  4 user  staff   136 Jun 13 22:27 assets
{% endhighlight %}

Ces actions me permettent maintenant très facilement de tester mon blog en local puis de le publier en un seul push :

{% highlight bash %}
bundle exec jekyll serve
{% endhighlight %}

[github-doc]: https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll
[dotclear]: https://dotclear.org
[jekyll-theme-hacker]: https://github.com/pages-themes/hacker
[pourquoi-rouge]: https://help.github.com/articles/page-build-failed-config-file-error/#fixing-highlighting-errors 
