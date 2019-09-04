---
layout: post
title:  "Angular getting started : pas à pas"
date:   2019-10-08 04:13:00 +0100
categories: Techno
---
Après trois ans à ne coder que du backend, je suis un peu - voire beaucoup - rouillé sur le front. Je vais donc me
remettre un peu dans le game en commençant par décrire la prise en main d'[Angular] et de son [getting started].

# Un IDE dans mon navigateur : je suis trop vieux pour ces c#@&!!!

Dès le démarrage, on nous propose de [coder dans StackBlitz][StackBlitz]. Même si ça permet d'aller vite et de découvrir
rapidement la technologie, je souhaite quand même passer un peu de temps pour intégrer la base de code dans mon
[ide préféré][IntelliJ].

Donc la première étape est d'installer l'outil en ligne de commande d'Angular : [Angular CLI]. Je suis les
[instructions][Angular SETUP] pour l'installer ; générer mon premier site Angular et le mettre sous git avec un
commentaire de commit explicite mais non moins très répandu :

{% highlight bash %}
npm install --global @angular/cli
ng new angular-getting-started
cd angular-getting-started
git init
git add .
git commit --message "initial commit"
{% endhighlight %}

# Mesurer la différence entre le scafold ng new et le code du tutorial

Je me rend sur [StackBlitz], je télécharge le projet tel quel (c'est une archive zip) que je place dans mon site
fraîchement généré.

Le diff est assez intéressant à regarder. Il y a des choses peu importantes comme des tags de licence Google dans
certains fichiers et d'autres plus remarquables comme les fichiers de sources qui vont être utilisés pendant le tutorial
ou les versions des librairies. J'ai l'impression que pas mal de changements sont dûs au fait que j'ai scafoldé mon
application avec une version plus récente de `ng` que celle construite sur [StackBlitz]. C'est souvent le reproche qu'on
peut faire aux technologies de génération de code : la difficulté à mesurer et à appliquer les nouvelles fonctionnalités
des générateurs dans le code déjà généré dans une version plus ancienne.

# Prêts à démarrer

L'étape finale : démarrer le serveur et commencer [le tutorial][getting started].

{% highlight bash %}
ng server --open
{% endhighlight %}

[Angular]: https://angular.io
[getting started]: https://angular.io/start
[StackBlitz]: https://angular.io/generated/live-examples/getting-started-v0/stackblitz.html
[IntelliJ]: https://www.jetbrains.com/idea
[Angular CLI]: https://angular.io/cli
[Angular SETUP]: https://angular.io/guide/setup-local
