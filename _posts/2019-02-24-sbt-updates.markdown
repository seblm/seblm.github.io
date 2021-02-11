---
layout: post
title:  "Comment s'assurer que mon projet sbt est à jour"
date:   2019-02-24 07:22:00 +0100
tags:   Techno
---
La bonne pratique sur un projet est de toujours être à jour sur ses dépendances. Or c'est assez fastidieux d'aller
vérifier ça _à la main_ sur chacune des dépendances. C'est là que [sbt-updates] peut nous aider à nous en sortir.

# Installation du plugin

Bien qu'on puisse envisager de l'installer sur un projet en particulier, ce plugin a plus de sens si il est installé de
manière globale sur le poste du développeur. Pour cela, on peut suivre [la documentation sbt]
conjointement avec [la documentation du plugin][sbt-updates] en exécutant ces commandes :

{% highlight bash %}
$ echo "// install sbt-updates to any sbt project according to documentation from https://github.com/rtimush/sbt-updates" > ~/.sbt/1.0/plugins/sbt-updates.sbt
$ echo "addSbtPlugin(\"com.timushev.sbt\" % \"sbt-updates\" % \"0.4.0\")" >> ~/.sbt/1.0/plugins/sbt-updates.sbt
{% endhighlight %}

# Utilisation du plugin

Une fois installé, le plugin peut-être utilisé dans n'importe quel projet pour savoir si vos dépendances peuvent-être
mises à jour. Voici comment vérifier ces mises à jour à la fois pour les dépendances de librairies du projet et pour les
plugins sbt :

{% highlight bash %}
$ sbt
sbt$ dependencyUpdates
sbt$ reload plugins
sbt$ dependencyUpdates
sbt$ reload return
{% endhighlight %}

[sbt-updates]: https://github.com/rtimush/sbt-updates
[la documentation sbt]: https://www.scala-sbt.org/1.x/docs/Using-Plugins.html#Global+plugins
