---
layout:    post
date:      2024-04-19 21:15:00 +0200
title:     "Mon feedback sur Devoxx 2024 - vendredi"
img_large: 2024-04-19-robot-devoxx.png
---

Dernier billet de rétrospective de mes trois jours à Devoxx 2024 : voici le résumé des sessions auxquelles j'ai assisté
le vendredi.

### Keynotes

Dans [la première keynote], Anatole Chouard nous a présenté les résultats du rapport Meadows sur la modélisation globale
du monde et la manière dont les ressources, la population, la pollution et d'autres critères peuvent évoluer au cours du
temps.

[La seconde keynote] menée par Guillaume Poupard a été l'occasion de rappeler que la menace des attaques d'états est
permanente et que le rempart le plus solide face à ces attaques était la prise en compte de la sécurité informatique dès
la conception des systèmes.

### [Au-delà du Craft : Revisitez Votre Relation avec le Software Craft] par Cyrille Martraire

Dans cette excellente conférence Cyrille nous a fait un rappel historique rafraichissant pour finalement amener l'idée
que chaque mouvement nait en réaction à l'ordre établi. Le craft notamment est né du fait que les langages sans gestion
de la mémoire étaient plus malléables et pouvaient changer plus vites que leurs ancêtres à gestion de mémoire manuelle.
Toute fois 15 ans après sa création, le mouvement craft est le nouvel ordre établi. Il est maintenant venu le temps de
se réinventer - d'autant que des dérives se sont manifestées au sein même de ce mouvement comme le dogmatisme ou les
leaders toxiques.

Il est nécessaire d'innover et de détecter dans la hype ce qui est vraiment nouveau. Il faut entreprendre soi-même.

### [Naviguer dans le Labyrinthe de la Gestion de Dépendances] par Louis Jacomet et Hervé Boutemy

Les deux speakers représentaient deux monstres des outils de builds JVM que sont Maven et Gradle. Ils ont tout de même
tenus à mettre en perspective leur présentation avec leur équivalent dans le monde JavaScript qu'est npm. Ils ont parlé
de la notion de _Bill Of Materials_ (BOM) dont l'équivalent côté Gradle est _Platform_.

En cas de conflit de version sur une dépendance transitive, les outils auront un comportement différent :
 - Maven prendra toujours la première version qu'il trouve
 - Gradle prendra toujours la version la plus récente
 - npm utilisera des ranges (par exemple `^` signifiant _jusqu’à la prochaine majeure_) - si un conflit apparait, on
   peut lancer un `dedupe` pour faire remonter et fixer la dépendance transitive dans le `package-lock.json` mais
   généralement, on pourra vivre avec plusieurs versions différentes car il n'y a pas de notion de classpath en Node.

J'ai bien aimé que ces deux experts ne bashent pas directement le `package-lock` car il a une utilité et encourage des
builds reproductibles. Hervé nous a rappelé l'existence en natif de `dependency:analyse` que j'avais déjà documenté sur
[ce blog] il y a 14 ans. Cela permet de vérifier par une analyse du bytecode que les dépendances sont bien déclarées.

Le point le plus intéressant a été la découverte de nouveaux formats de définition des _Bills Of Materials_ nommés
xBOM. Généralisés, ces descripteurs permettront à des outils tiers de contrôler la chaîne d'approvisionnement.

### [Apprivoiser la guitare ET la programmation fonctionnelle !] par Bastien Tran

15 minutes, c’est extrèmement court pour présenter la programmation fonctionnelle. Du coup on a pu entre-apercevoir les
notions de _pipe_, _option_ et _either_ développés à la main en TypeScript. Je n'ai malheureusement rien appris de neuf
sauf aprécier la démo de son application de génération de sons avec Web Audio.

### [De la couleurs dans nos Apps] par Emmanuel De Saint Steban

Ce micro talk nous a rappelé l'importance des couleurs dans les applications web. La meilleure manière de les encoder
est HTC (pour **H**ue **C**hroma **T**one - teinte/chrominance/ton) car les trois valeurs sont assez cohérentes : en
faisant varier l'une des trois valeurs, la palette fluctue _dans le bon sens_.

### [Netty, découvrez LE framework IO pour la JVM] par Stéphane Landelle

Ce framework IO est arrivé après que NIO ai vu le jour mais que ce dernier n'évolue pas suffisemment rapidement à
l'époque. Le futur de Netty : implémenter quic (Google), http/3, io_uring. Les virtual threads ne sont pas encore
supportés car Netty utilise massivement les `ThreadLocal` pour fonctionner.

### [Apache Flink - Data Processing en temps réel] par Gaël Renoux

Dans ce talk, on nous a présenté Flink comme un framework de traitement de données en quasi-temps réel - contrairement à
Spark, il ne fait pas de micro batch. Les concepts de base sont assez classique, mais la complexité arrive lorsqu'on
prend en compte son aspect stateful en mémoire. Il faut donc beaucoup de RAM et ne pas compter sur la persistance de
l'état, en tout cas pour le usecase de DataDome. Il n'a pas de concurrent dans son domaine mais sa complexité aussi bien
ops que dev le réserve à des usages où il est vraiment nécessaire.

### [J'ai terminé les 9 Advents of Code : Leçons Apprises] par Teiva Harsanyi

En tant que [pratiquant de l'Advent of Code], j'ai tout de suite été curieux de découvrir ce que Teiva avait à nous dire
sur le sujet. En bon googler, c'était assez intéressant de voir que son focus a été de nous présenter certains
algorithmes comme Topological Sort. Il a aussi parlé de la complexité en O et que parfois il suffisait de changer sa
structure de données pour que la complexité globale baisse. Avec deux exercices s'enchainant l'un puis l'autre, le
principe de YAGNI est souvent illustré : essayer de deviner le problème du second exercice en essayant d'abstraire sa
première solution ne fonctionne pas.

Enfin il a illustré sa vision des tests efficaces : des tests d'acceptance qui couvrent la fonctionnalité sans
_s'embêter_ à tester tous composants logiciels intermédiaires. C'était l'une des affiches présentes dans les toilettes
de Google : _Test behaviour, not implementation_. Je suis assez aligné avec cette vision. Il ne parle malheureusement
pas du plus gros défaut de pousser cette approche jusqu'au bout : les tests sont souvent plus compliqués à mettre en
place, plus difficiles à relire et à maintenir.

### [Apache Lucene : de l'indexation textuelle à l'intelligence artificielle] par Lucian Precup

En dehors du fait que c'était le dernier talk de Devoxx et que la fatigue s'est accumulée, je n'ai pas appris grand
chose dans ce talk sur Lucene.

### [Les Cast Codeurs en chair, en os et en béret] par Arnaud Héritier, Emmanuel Bernard, Katia Aresti et Antonio Goncalves

En tant que fidèle auditeur de [ce podcast] depuis ses débuts, assister à une session live est toujours un réel plaisir.
Les bières, les blagues et les anecdotes étaient au rendez-vous. Ils ont récupéré les perles du text-to-speech des
conférences, se sont eux-mêmes fait voler le flux audio pour être retranscrit en texte avec des blagues. Ça marquait la
fin de ce Devoxx 2024 qui s'est étalé du [mercredi] au vendredi en passant par [le jeudi]. Milles mercis à [Éric] pour
m'avoir offert le pass trois jours :heart: ainsi que [Zeenea] pour l'économie de congés associée :wink:.

[la première keynote]: https://www.devoxx.fr/schedule/talk/?id=74356
[la seconde keynote]: https://www.devoxx.fr/schedule/talk/?id=77117
[Au-delà du Craft : Revisitez Votre Relation avec le Software Craft]: https://www.devoxx.fr/schedule/talk/?id=47561
[Naviguer dans le Labyrinthe de la Gestion de Dépendances]: https://www.devoxx.fr/schedule/talk/?id=44907
[ce blog]: /2010/11/17/determiner-les-dependances-d-un-projet-maven.html#à-laide-doutils
[Apprivoiser la guitare ET la programmation fonctionnelle !]: https://www.devoxx.fr/schedule/talk/?id=27537
[De la couleurs dans nos Apps]: https://www.devoxx.fr/schedule/talk/?id=5283
[Netty, découvrez LE framework IO pour la JVM]: https://www.devoxx.fr/schedule/talk/?id=44030
[Apache Flink - Data Processing en temps réel]: https://www.devoxx.fr/schedule/talk/?id=40278
[J'ai terminé les 9 Advents of Code : Leçons Apprises]: https://www.devoxx.fr/schedule/talk/?id=44944
[pratiquant de l'Advent of Code]: /2021/12/14/advent-of-code-c-est-reparti-pour-l-edition-2021.html
[Apache Lucene : de l'indexation textuelle à l'intelligence artificielle]: https://www.devoxx.fr/schedule/talk/?id=29378
[Les Cast Codeurs en chair, en os et en béret]: https://www.devoxx.fr/schedule/talk/?id=27528
[ce podcat]: https://lescastcodeurs.com
[mercredi]: /2024/04/17/devoxx-2024-mercredi.html
[le jeudi]: /2024/04/18/devoxx-2024-jeudi.html
[Éric]: https://eric.lemerdy.name
[Zeenea]: https://www.zeenea.com
