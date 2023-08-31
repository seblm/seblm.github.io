---
layout: post
title:  "Pourquoi et comment a été corrigé NotSerializableException dans ScalaTest"
date:   2020-09-24 13:47:00 +0200
---

Qu’est-ce qui ne va pas avec [ScalaTest 3.2.2] ? Dans cet article je vais revenir sur un bug introduit dans [ScalaTest]
et comment le comprendre, l’analyser, le résoudre et éviter qu’il ne se reproduise à l’avenir.

## C’est quoi le problème ?

Retour aux fondamentaux : toujours essayer de décrire le problème jusqu’à sa cause racine afin d’être certain de bien le
comprendre pour pouvoir ensuite le résoudre correctement.

Lorsqu’on met à jour [ScalaTest] vers la [3.2.2][ScalaTest 3.2.2] et dans certaines conditions, on se retrouve à avoir
de nombreuses exceptions loggées sur la sortie standard. Voici un exemple pour illustrer :

{% highlight console %}
[info] welcome to sbt 1.3.13 (AdoptOpenJDK Java 1.8.0_252)
[info] loading settings for project try-values-not-serializable-build from plugins.sbt ...
[info] loading project definition from /home/runner/work/try-values-not-serializable/try-values-not-serializable/project
[info] loading settings for project root from build.sbt ...
[info] set current project to try-values-not-serializable (in build file:/home/runner/work/try-values-not-serializable/try-values-not-serializable/)
[info] Setting Scala version to 2.13.3 on 1 projects.
[info] Reapplying settings...
[info] set current project to try-values-not-serializable (in build file:/home/runner/work/try-values-not-serializable/try-values-not-serializable/)
[info] EitherRightSpec:
[info] The Hello object
[info] - should say hello *** FAILED ***
[info]   The Either on which right.value was invoked was not defined as a Right; it was Left(java.lang.Throwable: error). (EitherRightSpec.scala:9)
Reporter completed abruptly with an exception after receiving event: TestFailed(Ordinal(0, 4),The Either on which right.value was invoked was not defined as a Right; it was Left(java.lang.Throwable: error). ,EitherRightSpec,example.EitherRightSpec,Some(example.EitherRightSpec),The Hello object should say hello,should say hello,Vector(),Vector(),Some(org.scalatest.exceptions.TestFailedException: The Either on which right.value was invoked was not defined as a Right; it was Left(java.lang.Throwable: error). ),Some(30),Some(IndentedText(- should say hello,should say hello,1)),Some(SeeStackDepthException),Some(example.EitherRightSpec),None,pool-1-thread-1-ScalaTest-running-EitherRightSpec,1601352498885).
java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184)
	at java.io.ObjectOutputStream.writeArray(ObjectOutputStream.java:1378)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1174)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
	at org.scalatest.tools.SocketReporter.apply(SocketReporter.scala:31)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10(DispatchReporter.scala:249)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10$adapted(DispatchReporter.scala:248)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.scalatest.DispatchReporter$Propagator.run(DispatchReporter.scala:248)
	at java.lang.Thread.run(Thread.java:748)
Exception in thread "Thread-6" java.io.WriteAbortedException: writing aborted; java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1632)
	at java.io.ObjectInputStream.readArray(ObjectInputStream.java:2032)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1613)
	at java.io.ObjectInputStream.defaultReadFields(ObjectInputStream.java:2344)
	at java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:2268)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2126)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1625)
	at java.io.ObjectInputStream.defaultReadFields(ObjectInputStream.java:2344)
	at java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:2268)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2126)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1625)
	at java.io.ObjectInputStream.defaultReadFields(ObjectInputStream.java:2344)
	at java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:2268)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2126)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1625)
	at java.io.ObjectInputStream.defaultReadFields(ObjectInputStream.java:2344)
	at java.io.ObjectInputStream.readSerialData(ObjectInputStream.java:2268)
	at java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2126)
	at java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1625)
	at java.io.ObjectInputStream.readObject(ObjectInputStream.java:465)
	at java.io.ObjectInputStream.readObject(ObjectInputStream.java:423)
	at org.scalatest.tools.Framework$ScalaTestRunner$Skeleton$1$React.react(Framework.scala:839)
	at org.scalatest.tools.Framework$ScalaTestRunner$Skeleton$1.run(Framework.scala:828)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184)
	at java.io.ObjectOutputStream.writeArray(ObjectOutputStream.java:1378)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1174)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
	at org.scalatest.tools.SocketReporter.apply(SocketReporter.scala:31)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10(DispatchReporter.scala:249)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10$adapted(DispatchReporter.scala:248)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.scalatest.DispatchReporter$Propagator.run(DispatchReporter.scala:248)
	... 1 more
Reporter completed abruptly with an exception after receiving event: RunCompleted(Ordinal(0, 7),Some(476),Some(Summary(0,1,0,0,0,1,0,0)),None,None,None,main,1601352499046).
java.net.SocketException: Broken pipe (Write failed)
	at java.net.SocketOutputStream.socketWrite0(Native Method)
	at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:111)
	at java.net.SocketOutputStream.write(SocketOutputStream.java:155)
	at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1877)
	at java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(ObjectOutputStream.java:1786)
	at java.io.ObjectOutputStream.writeNonProxyDesc(ObjectOutputStream.java:1286)
	at java.io.ObjectOutputStream.writeClassDesc(ObjectOutputStream.java:1231)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1427)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.writeFatalException(ObjectOutputStream.java:1577)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:351)
	at org.scalatest.tools.SocketReporter.apply(SocketReporter.scala:31)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10(DispatchReporter.scala:249)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10$adapted(DispatchReporter.scala:248)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.scalatest.DispatchReporter$Propagator.run(DispatchReporter.scala:248)
	at java.lang.Thread.run(Thread.java:748)
Reporter completed abruptly with an exception on invocation of the dispose method.
java.net.SocketException: Broken pipe (Write failed)
	at java.net.SocketOutputStream.socketWrite0(Native Method)
	at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:111)
	at java.net.SocketOutputStream.write(SocketOutputStream.java:155)
	at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1877)
	at java.io.ObjectOutputStream$BlockDataOutputStream.flush(ObjectOutputStream.java:1822)
	at java.io.ObjectOutputStream.flush(ObjectOutputStream.java:719)
	at org.scalatest.tools.SocketReporter.dispose(SocketReporter.scala:37)
	at org.scalatest.Reporter$.propagateDispose(Reporter.scala:152)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$11(DispatchReporter.scala:260)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$11$adapted(DispatchReporter.scala:259)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.scalatest.DispatchReporter$Propagator.run(DispatchReporter.scala:259)
	at java.lang.Thread.run(Thread.java:748)
[info] Run completed in 1 second, 392 milliseconds.
[info] Total number of tests run: 0
[info] Suites: completed 0, aborted 0
[info] Tests: succeeded 0, failed 0, canceled 0, ignored 0, pending 0
[info] No tests were executed.
[error] Failed tests:
[error] 	example.EitherRightSpec
[error] (Test / testOnly) sbt.TestsFailedException: Tests unsuccessful
[error] Total time: 2 s, completed Sep 29, 2020 4:08:19 AM
{% endhighlight %}

Comme on peut le constater, c’est plutôt ennuyeux d’avoir un retour d’exécution de test aussi verbeux. D’autant que mon
projet d’exemple est minimaliste : il n’y a qu’un seul test de lancé. Il faut imaginer que pour des projets un peu
sérieux avec de nombreux tests, le log est extrêmement volumineux. Une stacktrace est ajoutée à chaque fois qu’un
évènement de reporting [ScalaTest] est émis. Et [ScalaTest] en émet de nombreux. Ce sont ces évènements qui permettent
par exemple à votre IDE de vous montrer un avancement synchronisé et cohérent de vos tests quand vous les faites
tourner.

<video width="1000" loop autoplay src="{{ '/img/launching-tests-ide.mp4' | relative_url }}">
 Affichage de l’exécution d’une suite de test ScalaTest dans un ide
</video>

Le problème introduit par la [3.2.2][ScalaTest 3.2.2] ne se produit que lorsque ces évènements sont sérialisés par un
_Reporter_ appelé `SocketReporter`. Il est fréquent que [ScalaTest] utilise ce _Reporter_ lorsque l’exécution des tests
est lancée par [sbt] en [mode _forké_][Sbt Forking] afin qu’il puisse, tout comme un ide, produire un avancement
synchronisé et cohérent des tests dans la jvm forkée.

<video width="1000" loop autoplay src="{{ '/img/launching-tests-sbt.mp4' | relative_url }}">
 Affichage de l’exécution d’une suite de test ScalaTest dans sbt
</video>

Ce `Reporter` utilise le mécanisme [Java Object Serialization][javase serialization] pour envoyer les évènements vers
un flux réseau.

C’est donc lors de cette sérialisation qu’une erreur se produit. Par la suite tout le flux est corrompu et de nombreuses
erreurs de sérialisation s’accumulent au moment où le `SocketReporter` sérialise ses objets.

## Opération reproduction

Pour tout bon bug qui se respecte, un test minimal permettant de le reproduire est essentiel pour bien le corriger. Nous
allons donc nous concentrer sur la lecture de la stack trace d’erreur (que j’ai raccourci pour l’exercice) :

{% highlight console %}
Reporter completed abruptly with an exception after receiving event: TestFailed(Ordinal(0, 4),The Either on which right.value was invoked was not defined as a Right; it was Left(java.lang.Throwable: error). ,EitherRightSpec,example.EitherRightSpec,Some(example.EitherRightSpec),The Hello object should say hello,should say hello,Vector(),Vector(),Some(org.scalatest.exceptions.TestFailedException: The Either on which right.value was invoked was not defined as a Right; it was Left(java.lang.Throwable: error). ),Some(30),Some(IndentedText(- should say hello,should say hello,1)),Some(SeeStackDepthException),Some(example.EitherRightSpec),None,pool-1-thread-1-ScalaTest-running-EitherRightSpec,1601352498885).
java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184)
	(...)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
	at org.scalatest.tools.SocketReporter.apply(SocketReporter.scala:31)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10(DispatchReporter.scala:249)
	at org.scalatest.DispatchReporter$Propagator.$anonfun$run$10$adapted(DispatchReporter.scala:248)
	at scala.collection.immutable.List.foreach(List.scala:333)
	at org.scalatest.DispatchReporter$Propagator.run(DispatchReporter.scala:248)
	at java.lang.Thread.run(Thread.java:748)
{% endhighlight %}

On comprend donc qu’un thread dédié propage les événements émis par [ScalaTest] (ici un `TestFailed`) vers les reporters
qui ont bien voulu s’enregister auprès du dispatcher. Ce dernier demande au `SocketReporter` de bien vouloir prendre en
compte l’évènement `TestFailed`. Et c’est sur ce groupe d’instances qu’on fini par tomber sur l’erreur racine :

{% highlight console %}
java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
{% endhighlight %}

Nous allons donc reproduire le problème en provoquant une erreur d’assertion : demander à `EitherValues` la valeur
`right` alors que la variable `Either` sera en réalité un `Left`.

{% highlight scala %}
val error: Either[String, Int] = Left("Error")

error.right.value shouldEqual 0
{% endhighlight %}

En lançant ce test avec sbt en mode forké, on a bien la même erreur que mentionnée plus haut.

Allons donc plus loin en récupérant l’instance de `TestFailedException` levée par ce _matcher_.

{% highlight scala %}
val error: Either[String, Int] = Left("error")
val caught = the[TestFailedException] thrownBy { error.right.value shouldEqual 0 }

caught.message.value shouldEqual "The Either on which right.value was invoked was not defined as a Right; it was Left(error). "
{% endhighlight %}

Il faut maintenant vérifier que cette exception est bien impossible à sérialiser.

{% highlight scala %}
val objectOutputStream: ObjectOutputStream = new ObjectOutputStream(_ => ())
val error: Either[String, Int] = Left("error")
val caught = the [TestFailedException] thrownBy { error.right.value shouldEqual 0 }

noException should be thrownBy objectOutputStream.writeObject(caught)
{% endhighlight %}

{% highlight console %}
An unexpected java.io.NotSerializableException was thrown.
ScalaTestFailureLocation: example.EitherRightSpec at (EitherRightSpec.scala:18)
org.scalatest.exceptions.TestFailedException: An unexpected java.io.NotSerializableException was thrown.
	at org.scalatest.matchers.MatchersHelper$.checkNoException(MatchersHelper.scala:328)
	at org.scalatest.matchers.dsl.ResultOfBeWordForNoException.thrownBy(ResultOfBeWordForNoException.scala:39)
	at example.EitherRightSpec.$anonfun$new$1(EitherRightSpec.scala:18)
(...)
Caused by: java.io.NotSerializableException: org.scalatest.EitherValues$RightValuable
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1184)
	at java.io.ObjectOutputStream.writeArray(ObjectOutputStream.java:1378)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1174)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1548)
	at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1509)
	at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1432)
	at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1178)
	at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:348)
	at example.EitherRightSpec.$anonfun$new$4(EitherRightSpec.scala:18)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	at org.scalatest.matchers.MatchersHelper$.checkNoException(MatchersHelper.scala:322)
	... 51 more
{% endhighlight %}

Cette fois on obtient un test unitaire qui échoue pour les bonnes raisons. C’est seulement à partir de ce test en échec
qu’on va pouvoir comprendre ce qui ne va pas avec la sérialisation de cette `TestFailedException`.

![capture d’écran d’IntelliJ IDEA en mode debug sur l’exception NotSerializedException]({{ '/img/debug-not-serializable-exception.png' | relative_url }})

Je ne conserve que les éléments de la stack intéressant dans cette chronologie d’appels en pseudo code :

 - `writeObject0(TestFailedException)` : écrit l’instance de `TestFailedException`
 - `defaultWriteFields(TestFailedException)` : sérialise tous les champs de `TestFailedException`
 - `writeObject0(TestFailedException.messageFun)` : sérialise l’instance `TestFailedException.messageFun` de type
   `scala.Function1` - la lambda qui dans le code prend la valeur
   `(_: StackDepthException) => Some(Resources.eitherRightValueNotDefined(rightProj.e))`. Oh tient c’est justement [un
   bout de code][changement cassant dans ScalaTest] qui a été [mergé dans la branche de scalatest]
   [pull request dans ScalaTest] il y a peu de temps.
   En réalité, `scala.Function1` est une vision simplifiée de l’instance en mémoire qui prend la valeur
   `org.scalatest.EitherValues$RightValuable$$Lambda$109/1907431275@78aab498`.
 - `defaultWriteFields(TestFailedException.messageFun)` : sérialise tous les champs de
   `(_: StackDepthException) => Some(Resources.eitherRightValueNotDefined(rightProj.e))`. Il s’agit d’une instance de
   `SerializedLambda`.
 - `writeObject0(TestFailedException.messageFun.asInstanceOf[SerializedLambda].capturedArgs)` : sérialise les
   `capturedArgs` de la lambda. Voici ce que dit la javadoc au sujet de ce field : _The dynamic arguments to the lambda
   factory site, which represent variables captured by the lambda_
 - `writeArray(TestFailedException.messageFun.asInstanceOf[SerializedLambda].capturedArgs)` : sérialise le seul élément
   du tableau des `capturedArgs` de la lambda qui est la fameuse instance de `EitherValues$RightValuable`
 - `writeObject0(TestFailedException.messageFun.asInstanceOf[SerializedLambda].capturedArgs[0])` : lance l’exception
   `NotSerializedException` à juste titre car les instances de `EitherValues$RightValuable` ne sont pas sérialisables.
   
En conclusion, il _suffit_ de rendre `EitherValues.RightValuable` et `EitherValues.LeftValuable` sérialisables pour ne
plus avoir de problèmes. Le problème vient donc de ce changement dans la base de code :

{% highlight scala %}
          throw new TestFailedException((_: StackDepthException) => Some(Resources.eitherRightValueNotDefined), Some(cause), pos)
          throw new TestFailedException((_: StackDepthException) => Some(Resources.eitherRightValueNotDefined(rightProj.e)), Some(cause), pos)
{% endhighlight %}

La différence se situe dans les paramètres que la lambda doit capturer pour invoquer l’évaluation de la fonction.
Auparavant, il n’y en avait aucun. Avec ce changement, la lambda doit capturer l’instance de `rightProj` qui n’est pas
sérialisable.

## Correction ##

La correction a consisté à rendre sérialisable les classes :
 - `org.scalatest.EitherValues`
 - `org.scalatest.EitherValues.LeftValuable`
 - `org.scalatest.EitherValues.RightValuable`
 - `org.scalatest.TryValues`
 - `org.scalatest.TryValues.SuccessOrFailure`
 
## Intervention inattendue d’un tiers

Peu après avoir soumis [la pull request de correction][pull request de correction] sur `EitherValues` et `TryValues`, un
utilisateur de [ScalaTest] s’est manifesté pour mentionner qu’il avait le même type d’erreur mais sur `FutureValues`.

Après avoir investigué et reproduit l’erreur, la correction fut apportée en rendant possible la sérialisation de
`org.scalatest.concurrent.AbstractPatienceConfiguration.PatienceConfig` par l’intermédiaire d’un proxy injecté grâce à
la fonction `writeReplace`. Voici l’explication ajoutée à cette fonction :

{% highlight scala %}
    /**
     * <code>PatienceConfig</code> is an inner class. For some reason (like using <code>SocketReporter</code> and having
     * a <code>ScalaFutures</code> test that fails), instance of this class will be serialized. But as an inner class,
     * scala will add an extra <code>$outer</code> field that references actual outer class: an instance that you don’t
     * want make serializable.
     * To avoid errors like <code>java.io.NotSerializableException: org.scalatest.verbs.BehaveWord</code>, this function
     * delegates serialization to a non inner class.
     */
{% endhighlight %}

## Validation de la pull request par un lieutenant

Classiquement dans un projet open source, les mainteneurs sont secondés par _des lieutenants_. J’ai constaté que c’était
le cas dans [ScalaTest]. Un développeur a fait tourner des tests supplémentaires que la CI ne fait pas tourner et m’a
proposé d’ajouter trois commits sur [ma pull request][pull request de correction] pour exclure certains bouts de code du
build ScalaJS. Il a ensuite donné le feu vert au mainteneur pour qu’il puisse effectuer le merge final.

## Merge final par le mainteneur

Après avoir posé une remarque judicieuse sur [la pull request][pull request de correction] il l’a finalement intégrée
dans la branche stable.

![capture d’écran du commit de merge de la pull request]({{ '/img/merged.png' | relative_url }})

## Attente de la release

On est maintenant en train d’attendre la release 3.2.3 de [ScalaTest].

## Conclusion

En conclusion : n'hésitez pas à contribuer. On apprend toujours plein de choses intéressantes et on participe à l'effort
commun pour que notre industrie soit plus fiable et plus qualitative.

[changement cassant dans ScalaTest]: https://github.com/scalatest/scalatest/commit/a4e261af5926e102f7530daac50e6aa4937bd191 "Changement cassant dans ScalaTest"
[javase serialization]: https://docs.oracle.com/en/java/javase/15/docs/specs/serialization/index.html "Spécification de la sérialisation Java"
[pull request dans ScalaTest]: https://github.com/scalatest/scalatest/pull/1878 "Pull request qui introduit le problème dans la branche stable"
[pull request de correction]: https://github.com/scalatest/scalatest/pull/1884 "Pull request qui corrige le problème"
[sbt]: https://www.scala-sbt.org "Site web de sbt"
[Sbt Forking]: https://www.scala-sbt.org/1.x/docs/Forking.html "Article qui décrit ce qu’est le mode d’exécution fork"
[ScalaTest]: https://github.com/scalatest/scalatest "Repository GitHub de ScalaTest"
[ScalaTest 3.2.2]: https://github.com/scalatest/scalatest/releases/tag/release-3.2.2 "Changelog de ScalaTest 3.2.2"
