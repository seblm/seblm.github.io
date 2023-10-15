---
layout:    post
title:     "On devrait toujours savoir maîtriser une stack web"
date:      2023-10-15 16:01:00 +0200
img_large: 2023-10-15-extreme-startup-http4s.png
---

Un jour un homme sage m’a dit que tout bon développeur devrait toujours avoir un stack web favorite et maîtrisée du bout
des doigts. C’était il y a très longtemps. Cela dit, je pense que ce conseil est encore et toujours vrai en 2023.
N’importe quel produit aujourd’hui s’appuie sur le protocole http et sur des apis rest clientes et/ou serveur. Il est
temps pour moi de choisir à nouveau cette stack.

Nous allons nous intéresser à [http4s]. Le meilleur moyen de découvrir cette librairie est d’échafauder un projet grâce
à un template [gitter8]. J’ai choisi la branche Scala 3 car il faut bien s’y mettre un jour.

```shell
sbt new http4s/http4s.g8 --branch 0.23-scala3
```

Une fois avoir répondu à quelques questions sur le nom du projet et le package, voici ce qu’on obtient dans notre IDE :

![Résultat de la génération du projet d’exemple](/img/2023-10-15-skaffolded-htt4s.png)

J’applique à ce projet initial des petites marottes personnelles comme :

- l’utilisation de scalafmt dont voici le fichier de configuration à mettre à la racine du projet et à nommer
  `.scalafmt.conf` :
  ```properties
  maxColumn = 120
  runner.dialect = scala3
  version = 3.7.4
  ```
- l’utilisation du plugin `sbt-explicit-dependencies` qui permet de nous rendre compte que de nombreuses dépendances ont
  été _oubliées_ dans le projet initial :
  ```
  "co.fs2"        %% "fs2-io"             % "3.7.0",
  "com.comcast"   %% "ip4s-core"          % "3.3.0",
  "io.circe"      %% "circe-core"         % CirceVersion,
  "org.http4s"    %% "http4s-client"      % Http4sVersion,
  "org.http4s"    %% "http4s-core"        % Http4sVersion,
  "org.http4s"    %% "http4s-server"      % Http4sVersion,
  "org.typelevel" %% "case-insensitive"   % "1.4.0",
  "org.typelevel" %% "cats-core"          % "2.9.0",
  "org.typelevel" %% "cats-effect"        % "3.5.0",
  "org.typelevel" %% "cats-effect-kernel" % "3.5.1",
  ```
  Tandis que la dépendance à _logback_ aurait dû avoir un scope `Runtime` plutôt que `Compile`.

Mais rentrons dans le vif du sujet en commençant par faire une petite démo. Rien n’est plus intéressant selon moi que de
commencer par regarder comment se comporte un programme avant d’en étudier les détails internes. Pour cela, je clique
sur la petite flèche verte dans le fichier `Main.scala`. Je vous passe les logs d’initialisation de _logback_ et voici
ce qu’on obtient :

```
[io-compute-10] INFO  o.h.e.s.EmberServerBuilderCompanionPlatform - Ember-Server service bound to address: [::]:8080 
```

Mon serveur web est démarré, je peux tenter un `GET` à la racine :

```shell
curl --verbose localhost:8080
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET / HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.1.2
> Accept: */*
> 
< HTTP/1.1 404 Not Found
< Date: ***, ** *** **** **:**:** ***
< Connection: keep-alive
< Content-Type: text/plain; charset=UTF-8
< Content-Length: 9
< 
* Connection #0 to host localhost left intact
Not found%
```

Aucune route n’a été implémentée pour répondre à la requête racine.

Voici la classe qui démarre le serveur web:

```scala
import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = Extreme_startuphttp4sServer.run[IO]
```

On voit que _http4s_ utilise [_cats effect_][cats-effect] pour se lancer en héritant de `IOApp.Simple`. Elle réclame une
implémentation de la fonction `run`:

```scala
def run: IO[Unit]
```

On ne peut donc pas passer d’argument de lancement au programme en entrée et le code de retour une fois terminé sera
toujours `0`.

Allons maintenant regarder à quoi correspond cette fonction `run` dans notre cas:

```scala
def run[F[_]: Async: Network]: F[Nothing] = {
  for {
    client <- EmberClientBuilder.default[F].build
    helloWorldAlg = HelloWorld.impl[F]
    jokeAlg = Jokes.impl[F](client)

    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract segments not checked
    // in the underlying routes.
    httpApp = (
      Extreme_startuphttp4sRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        Extreme_startuphttp4sRoutes.jokeRoutes[F](jokeAlg)
    ).orNotFound

    // With Middlewares in place
    finalHttpApp = Logger.httpApp(true, true)(httpApp)

    _ <-
      EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build
  } yield ()
}.useForever
```

On peut donc voir ici que c’est `orNotFound` qui nous permet d’obtenir notre premier `404 Not Found`.

La fonction `run` est importante car elle décrit ce que nécessite _http4s_ pour démarrer :

1. une série de classes utilitaires utilisés par les routes pour implémenter concrètement les appels - ces classes
   dépendent généralement d’une _type class_ `F[_]` et de contraintes appliquées à cette _type class_ comme
   [`Applicative`][Applicative] ou bien [`Concurrent`][Concurrent]
2. le crible des routes composées les unes avec les autres et formant le type requis
   `Kleisli[F, Request[F], Response[F]]`
3. des [middlewares][Middleware] autour de ces routes - ici on aura toutes les entrées/sorties du serveur web qui seront
   loggées
4. enfin le démarrage du serveur web sous-jacent - ici Ember - qui prend en entrée certaines configurations ainsi que
   nos routes précédemment déclarées

Pour terminer de jouer avec les routes, on peut zoomer sur les deux routes proposées par le projet généré
automatiquement :

1. `helloWorldRoutes` se teste de cette manière :
   ```shell
   curl localhost:8080/hello/world
   {"message":"Hello, world"}%
   ```
2. tandis qu'on peut lancer `jokeRoutes` avec :
   ```shell
   curl localhost:8080/joke
   {"joke":"What animal is always at a game of cricket? A bat."}%
   ```

La première permet de retourner une réponse json contenant la concaténation de `Hello, ` et du second élément du path.
La seconde s’amuse à interroger [un service tiers public ouvert sur Internet][icanhazdadjoke] pour sérialiser la
réponse en json.

Il y aurait 10000 choses à dire de plus sur cette stack web assez complète. Ce que je retiens d’_http4s_ est qu’il est
profondément ancré dans l’écosystème [Typelevel] notamment [cats] et [cats-effect]. Ceci peut donc être un critère de
choix si les équipes sont habituées à écrire des programmes Scala avec ces librairies. En contrepartie cela peut être
un élément bloquant si on ne souhaite pas adhérer à cette manière d’envisager la programmation en Scala.

[Applicative]: https://typelevel.org/cats/typeclasses/applicative.html
[cats]: https://typelevel.org/cats
[cats-effect]: https://typelevel.org/cats-effect
[Concurrent]: https://typelevel.org/cats-effect/docs/typeclasses/concurrent
[gitter8]: https://www.foundweekends.org/giter8
[http4s]: https://http4s.org
[icanhazdadjoke]: https://icanhazdadjoke.com/about
[Middleware]: https://http4s.org/v0.23/docs/middleware.html
[Typelevel]: https://typelevel.org