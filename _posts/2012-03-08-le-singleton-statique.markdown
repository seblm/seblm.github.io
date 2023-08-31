---
layout:    post
title:     Le singleton statique
date:      2012-03-08 20:59:49
noExcerpt: true
---

J'aime bien les singletons et je n'ai jamais vraiment pris le temps de comprendre pourquoi tant de gens déconseillent ce
design pattern (d'ailleurs
[le singleton est-il un design pattern ?](http://stackoverflow.com/questions/7262217/singleton-usage)). J'ai eu un
exemple très récemment des dangers que cela peut provoquer.

Le projet sur lequel je travaille a une base de code plutôt impressionnante et plutôt pas trop mal testée (de l'ordre de
13000 classes et 56% de couverture de code). Tous ces tests exécutés chaque jour n'auront finalement permi de détecter
ce problème de singleton qu'aujourd'hui. Je résume la situation : une classe héritant de `ArrayList<MaClasse>` a été
écrite il y a plusieurs années afin d'ajouter des comportements à ce type de listes bien particulières. Jusque là :
pourquoi pas. Problème : cette classe déclare _une variable de classe_ (donc `static`) nommée `EMPTY`. Elle permettait à
l'origine d'économiser les instanciations pour l'utilisation de la liste vide à l'extérieur. Cela ne choque pas plus de
ça - d'autant plus que des
[classes du JDK](http://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#emptyList()) offrent ce genre de
fonctionnalités.

Une chose primordiale à noter dans la documentation est la notion d'immuabilité. La liste `EMPTY` découverte dans le
code n'est pas immuable et n'interdit donc pas ce type d'instructions :

{% highlight java %}
MaListe.EMPTY.add(new MonObjet());
{% endhighlight %}

Inévitablement, ce code a fini par être écrit (en fait, l'ajout dans la liste vide était fait beaucoup _plus loin_ que
son initialisation). Et les effets de bord néfastes se sont donc produits.
Pour corriger ce problème, il est nécessaire de créer une implémentation spécifique de `MaListe<MaClasse>` qui soit
immuable.

__Moralité__ : _de grands pouvoirs impliquent de grandes responsabilités_. Il faut toujours bien savoir ce que l'ont
fait quand on parle de singleton, surtout dans un environnement concurrent.

Un article qui couvre une autre dimension du singleton :
[le lazy singleton](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html).