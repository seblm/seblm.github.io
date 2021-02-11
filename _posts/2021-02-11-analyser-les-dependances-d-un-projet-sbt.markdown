---
layout: post
title:  "Analyser les dépendances d’un projet sbt"
date:   2021-02-11 22:12:00 +0200
tags:   Techno
---

Depuis la version 1.4.0, le fameux plugin `sbt-dependency-graph` est [inclus nativement dans sbt][sbt-release-note]. La
chose à savoir en revanche, c’est que seule la tâche `dependencyTree` est utilisable dans les configurations `Compile`
et `Test`. Pour avoir accès aux autres tâches, il faut rajouter cette ligne dans `project/plugins.sbt` :
```sbt
addDependencyTreePlugin
```

## Connaître la provenance d’une dépendance transitive

Vous ne comprenez pas pourquoi une librairie se retrouve dans votre classpath ? Voici la commande à passer pour savoir
quel(s) composant(s) en a besoin :

```sbt
whatDependsOn <organization> <module> <revision>?
```

[sbt-release-note]: https://www.scala-sbt.org/1.x/docs/sbt-1.4-Release-Notes.html#sbt-dependency-graph+is+in-sourced
