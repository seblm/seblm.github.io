---
layout:    post
title:     "Structurer son code en Scala"
---

Le langage Scala a souvent été présenté comme un langage riche — laissant aux développeurs une grande liberté dans la
manière d’implémenter les programmes. On peut en effet décider d’écrire du code orienté objet, fonctionnel ou les deux.
Ce vaste champ des possibles laisse parfois un peu perplexe et peut parfois laisser penser que chaque base de code est
totalement unique. Martin Ordersky a depuis longtemps proposé [une vision assez claire de cette dualité][fpoop-fusion] :
- la programmation fonctionnelle pour la logique
- la programmation orientée objet pour la modularité
  Dans cet article, nous allons rappeler quatre options possibles pour organiser son code.

Ces quatre approches sont présentées dans le _Scala 3 Book_ [disponible en ligne][how-to-organize-functionality] sur le
site officiel du langage.

### Définir les fonctions dans un objet companion

[fpoop-fusion]: https://docs.scala-lang.org/scala3/book/why-scala-3.html#1-fpoop-fusion
[how-to-organize-functionality]: https://docs.scala-lang.org/scala3/book/domain-modeling-fp.html#how-to-organize-functionality