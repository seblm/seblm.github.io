---
layout:    post
title:     Déterminer les dépendances d'un projet maven
date:      2010-11-17 21:46:49
noExcerpt: true
---

Rappel de l'importance des dépendances dans un projet
-----------------------------------------------------

Il fut un temps où gérer les dépendances d'un projet JEE consistait à copier tous les jars téléchargés au fil de l'eau
depuis sourceforge vers le répertoire magique de son application web : le fameux `WEB-INF/lib`. Le temps où les
`ClassNotFoundException` ou les `NoSuchMethodError` pouvaient arriver à tout moment (ou presque). Comment gérait-on les
migrations des librairies tierces ? Comment déterminions-nous le degré de dépendance de notre code avec les frameworks ?
Mon avis est qu'on ne s'en souciait peu – Java ayant une forte tradition de non-régression dans ses évolutions.

Tout le monde s'accorde pour dire que la puissance de la plateforme Java est justement la multitude de librairies
tierces et la réactivité potentielle d'une communauté très présente. Mais ce foisement implique de fait une gestion plus
rationnelle des liens de dépendances tissées entre ces différents produits. C'est entre autre ce que maven essaie de
faire en mettant en place des conventions dans la construction et le packaging des librairies.

Nous allons donc voir comment maven peut nous aider à optimiser voire minimiser les dépendances afin de consommer moins
d'espace mémoire dans la JVM pendant l'exécution d'un programme.

Les niveaux de dépendances définis par maven
--------------------------------------------

Dans tout langage il existe plusieurs niveaux de dépendance. Nous allons voir les principaux niveaux que maven défini
pour java :

- Le premier niveau est le plus important : `compile`. Si une dépendance n'est pas satisfaite à ce niveau, le projet ne
  compile pas.
- Le second niveau permet justement d'éviter les `ClassNotFoundException` et les `NoSuchMethodError` citées plus haut :
  `runtime`. Il indique que certaines librairies tout comme votre projet peut invoquer du code de manière dynamique
  (c'est la réflexion). C'est le cas de Spring-context par exemple qui essaie d'instancier des classes sur la base d'une
  chaîne de caractères qu'il aura parsé dans un fichier XML.
- Les troisième et quatrième niveaux ont été introduits suite aux différentes absorbtions de librairies vraiment
  centrales dans le cœur de la plateforme ou bien dans le classpath global d'une JVM : `system` et `provided`. Ce niveau
  ne nous intéresse pas vraiment.
- Enfin le dernier niveau permet de définir des dépendances qui ne sera jamais embarqués dans les applications de
  production mais sont nécessaires pour tester le logiciel : `test`. Le meilleur exemple de ce niveau est bien entendu
  la librairie _JUnit_.

Pour chaque librairie (appelons-les _artifact_), ces dépendances permettent de construire un graphe. Ce graphe permet de
gérer de manière cohérente et reproductible les classpath lors de la compilation, les tests, la construction et
l'exécution de l'artifact. L'ensemble des dépendances contenues dans le graphe sont déterminées en résolvant les
dépendances de chacuns des artifacts récursivement en commençant par l'artifact initial et cela récursivement jusqu'à ce
qu'il ne reste plus aucune dépendance transitive à résoudre.

![exemple de graphe de dépendances]({{ '/img/image_gallery.jpg' | relative_url }})

À la main
---------

Commencer par supprimer toutes les dépendances (les mettre en commentaires dans le pom). Le scope le plus proche du code
étant `compile`, la première étape est donc de pouvoir faire compiler le projet. Toute dépendance qui n'aide pas à la
compilation doit avoir un scope différent de `compile` ou supprimée si elle ne sert à rien.

Une fois que le projet compile on doit pouvoir valider le scope runtime. Pour cela deux solutions plus ou moins
élégantes :

1. Soit votre projet dispose d'une bonne couverture de tests unitaires et l'exécution de ces tests vous permet de
   valider qu'un maximum d'instanciations dynamiques seront réussies.
1. Soit votre projet ne dispose pas de tests unitaires auquel cas vous devrez exécuter le projet dans un environnement
   proche de la production. Vous noterez qu'en fonction de l'ordre avec lequel vous ajoutez au fur et à mesure vos
   dépendances il se peut que vous puissiez oublier des dépendances de niveau `compile` (utilisées directement dans
   votre code) mais résolues par une dépendance transitive. Or cela est problématique car rien ne vous garantie que la
   dépendance transitive ne soit pas un jour supprimée par la librairie qui la porte.

À l'aide d'outils
-----------------

Maven dispose d'un outil très utile pour minimiser les dépendances inutiles mais aussi pour garantir une bonne qualité
des dépendances de niveau compile : il s'agit de `maven-dependency-plugin`.

Placez-vous dans un projet mavenisé et exécutez le goal suivant :

{% highlight bash %}
mvn dependency:analyze
{% endhighlight %}

L'exécution doit vous indiquer :

- Les dépendances de niveau `compile` qui ne sont pas déclarées dans votre projet
- Les dépendances qui sont déclarées mais qui ne servent pas à la compilation

Ces informations vous seront très utiles pour optimiser les dépendances. Exemple pour un projet fwk :

{% highlight bash %}
[INFO] [dependency:analyze {execution: default-cli}]
[WARNING] Used undeclared dependencies found:
[WARNING]    org.springframework:spring-core:jar:2.0.8:compile
[WARNING]    org.hibernate:hibernate-core:jar:3.3.0.SP1:compile
[WARNING]    org.springframework:spring-dao:jar:2.0.8:compile
[WARNING]    aopalliance:aopalliance:jar:1.0:compile
[WARNING]    org.springframework:spring-beans:jar:2.0.8:compile
[WARNING]    org.apache.axis:axis-jaxrpc:jar:1.4:compile
[WARNING]    commons-logging:commons-logging:jar:1.0.4:compile
[WARNING]    org.hibernate:ejb3-persistence:jar:1.0.2.GA:compile
[WARNING] Unused declared dependencies found:
[WARNING]    javax.faces:jsf-impl:jar:1.2_04-p02:compile
[WARNING]    org.slf4j:slf4j-api:jar:1.5.8:compile
[WARNING]    org.springframework:spring-jpa:jar:2.0.8:compile
[WARNING]    net.sf.ehcache:ehcache:jar:1.5.0:compile
[WARNING]    org.hibernate:hibernate-validator:jar:3.1.0.GA:compile
[WARNING]    javax.servlet:jstl:jar:1.2:compile
[WARNING]    com.sun.facelets:jsf-facelets:jar:1.1.11:compile
[WARNING]    org.slf4j:slf4j-simple:jar:1.5.8:compile
[WARNING]    commons-collections:commons-collections:jar:3.2:compile
[WARNING]    org.slf4j:slf4j-log4j12:jar:1.4.2:test
[WARNING]    org.springframework.security:spring-security-acl:jar:2.0.4:compile
[WARNING]    javax.persistence:persistence-api:jar:1.0:provided
[WARNING]    org.hibernate:hibernate-entitymanager:jar:3.4.0.GA:compile
{% endhighlight %}

Dans ce rapport on voit que 8 dépendances sont utilisées par le code source sans être déclarées (le code compile car il
se repose sur les dépendances transitives). 13 dépendences sont déclarées alors qu'elles ne sont pas utilisées par le
code source. Il faut toutefois nuancer ce deuxième résultat : en effet seule le niveau `compile` est intéressant pour
cette mesure. Le niveau `provided` ou test reste dans bien des cas utile à déclarer.
