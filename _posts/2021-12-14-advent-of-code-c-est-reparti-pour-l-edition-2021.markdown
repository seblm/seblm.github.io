---
layout: post
title:  "Advent of code : c’est reparti pour l’édition 2021"
date:   2021-12-14 03:06:00 +0200
---

Avec cet article, j’aimerais revenir sur les ressorts qui poussent chaque année les développeurs du monde entier à
participer à une compétition sur la base d'un calendrier de l’avant un peu spécial : [advent of code][advent-of-code].

## Le calendrier de l’avant du code

Durant notre enfance, qui n’a pas ressenti au fur et à mesure que les petites fenêtres de son calendrier de l’avent
s’ouvraient l’excitation d’atteindre tout au long de ce mois de décembre les nombreux jalons attendus toute l’année :
la Saint Nicolas, les vacances, (son anniversaire) et enfin le réveillon puis le jour de Noël.

Maintenant devenus adultes et développeurs, un autre type de calendrier de l’avent a vu le jour et est devenu de ma
perception tout au moins de plus en plus populaire. Cette compétition permet à toute personne de résoudre chaque jour de
décembre jusqu’au 24 inclus deux exercices de code.

## Advent of code : un certain type de problèmes à résoudre

Voici selon moi les caractéristiques qui font l’intérêt de cette compétition :

 - *un énoncé verbeux* : pour amener les concurrents à résoudre les problèmes du jour, on nous invite souvent à lire
   attentivement une histoire fortement scénarisée. Bien qu’ayant peu d’intérêt, on ne peut qu’admirer l’imagination des
   concepteurs pour inventer des situations farfelues et originales afin de nous amener à l’énoncé du problème. Pro
   tip : les emphases en gras sont à lire avec attention et sont souvent clés pour la compréhension.
 - *des exemples illustrant le problème* : sans eux, difficile d’imaginer prendre du plaisir dans la résolution. Ils
   permettent une compréhension graduelle et sont très souvent les entrées de nos tests unitaires.
 - *un fichier de test unique* : afin d’éviter les tricheries les plus simples, le fichier de test de chaque jour est
   généré spécialement pour vous. Ça nous donne un petit sentiment d’exclusivité.
 - *un fichier de test à interpréter* : on n’est pas forcément confrontés tous les jours à l’interprétation d’un fichier
   texte dont la syntaxe n’est pas dans un format aussi stricte que json, xml ou csv. Ça nous fait réviser nos
   expressions régulières.
 - *un résultat concis* : le fichier d’entrée est souvent assez volumineux alors que la sortie attendue est très petite.
   Souvent un nombre. Ça donne une idée du genre de problèmes posés : des algorithmes de traitements ou de parcours puis
   une réduction.
 - *une réponse en deux parties* : on entrouvre notre fenêtre du jour en répondant à une première question qui nous
   amène à la seconde partie du problème. L’intérêt est souvent de pouvoir réutiliser du code de la première partie dans
   la seconde. Hélas parfois, on ne réutilisera que notre compréhension du fichier d’entrée.
 - *un leaderboard entre amis* : l’aspect compétitif est renforcé par la possibilité de créer des leaderboards plus
   petits que le leaderboard mondial. Ce dernier a d’ailleurs souvent tendance à énerver si on étudie le temps de
   réponse des premiers.

## Mon expérience

J’ai participé à mon premier advent of code il y a 7 ans ! La plupart du temps, je n’ai réalisé que très peu
d’exercices. [Cette année][my-repository], je m’y tiens depuis 13 jours. Les années précédentes, je me rappelle avoir
_rage quit_ sur des exercices trop compliqués. Cette année, je me tiens à ce que je connais le mieux : du Scala. La
seule excentricité, c’est d’utiliser [Scala 3][scala-3] pour découvrir sa syntaxe.

![mes stars sur advent of code]({{ '/img/2021-advent-of-code.png' | relative_url }})

Comme toujours le plus sympa reste d’échanger avec les autres sur le cheminement vers la solution employée par chacun. Pour l’instant, je n’ai pas encore regardé d’autres repositories. J’avoue qu’une fois résolu, je passe parfois un peu de temps à refactorer mais bien souvent, je me contente des tests vers et de mes deux étoiles quotidiennes. Il faut savoir économiser ses forces et tenir sur la durée.

Il me reste à vous souhaiter de bien vous amuser sur [cette édition 2021][advent-of-code-2021], ou bien la prochaine !

[advent-of-code]: https://adventofcode.com
[advent-of-code-2021]: https://adventofcode.com/2021
[scala-3]: https://docs.scala-lang.org/scala3/new-in-scala3.html
[my-repository]: https://github.com/seblm/katas/tree/main/adventofcode/scala/src/main/scala/name/lemerdy/sebastian/katas/adventofcode/_2021