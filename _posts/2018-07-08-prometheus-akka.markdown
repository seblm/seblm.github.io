---
layout: post
title:  "Prometheus et akka-http"
date:   2018-07-08 05:40:00 +0200
tags:   Techno
---
Prometheus est une base de données timeseries qui permet de stocker des informations de métriques afin de les restituer
dans un grafana. akka-http est une librairie qui permet d'écrire des serveurs web en scala. Le but de cet article est de
montrer comment faire fonctionner ces deux technologies ensemble.

# Utilisation des projets existants

## prometheus-akka-http-example

[Ce projet][prometheus-akka-http-example] est un exemple permettant d'utiliser [prometheus-akka-http]. Nous allons dans
un premier temps cloner le projet et le démarrer :

{% highlight bash %}
$ git clone git@github.com:wlk/prometheus-akka-http-example.git
Cloning into 'prometheus-akka-http-example'...
remote: Counting objects: 24, done.
remote: Compressing objects: 100% (12/12), done.
remote: Total 24 (delta 4), reused 24 (delta 4), pack-reused 0
Receiving objects: 100% (24/24), 8.53 KiB | 8.53 MiB/s, done.
Resolving deltas: 100% (4/4), done.
$ cd prometheus-akka-http-example
$ sbt run
(...)
[info] Running com.wlangiewicz.prometheusexample.Main
{% endhighlight %}

Il faut maintenant lancer quelques requêtes afin de générer des informations sur les métriques qui seront récupérées par
Prometheus :

{% highlight bash %}
$ curl localhost:8080/example
OK%
$ curl localhost:8080/example
OK%
$ curl localhost:8080/example
OK%
{% endhighlight %}

Voici le résultat d'une requête de récupération des métriques de notre serveur web akka :

{% highlight bash %}
$ curl localhost:8080/metrics
# HELP request_processing_seconds Time spent processing request
# TYPE request_processing_seconds histogram
request_processing_seconds_bucket{endpoint="/example",le="0.01",} 0.0
(...)
request_processing_seconds_bucket{endpoint="/example",le="0.175",} 0.0
request_processing_seconds_bucket{endpoint="/example",le="0.2",} 1.0
(...)
request_processing_seconds_bucket{endpoint="/example",le="0.45",} 1.0
request_processing_seconds_bucket{endpoint="/example",le="0.5",} 2.0
request_processing_seconds_bucket{endpoint="/example",le="0.6",} 2.0
request_processing_seconds_bucket{endpoint="/example",le="0.7",} 3.0
(...)
request_processing_seconds_bucket{endpoint="/example",le="+Inf",} 3.0
request_processing_seconds_count{endpoint="/example",} 3.0
request_processing_seconds_sum{endpoint="/example",} 1.2735661459999998
{% endhighlight %}

On constate donc que le projet va générer trois métriques :

 1. `request_processing_seconds_bucket` : un histograme des temps de réponse du serveur par endpoint
 2. `request_processing_seconds_count` : un compteur du nombre de requêtes par endpoint
 3. `request_processing_seconds_sum` : un compteur du temps passé total par endpoint

Voyons ce que ça donne dans prometheus. Commençons par déclarer Prometheus, Grafana et l'[application d'exemple]
[prometheus-akka-http-example] dans le fichier `docker-compose.yaml` :

{% highlight yaml %}
version: '3'
services:
  prometheus:
    image: prom/prometheus
    volumes:
     - ./prometheus.yml:/etc/prometheus/prometheus.yml
    command: '--config.file=/etc/prometheus/prometheus.yml --storage.tsdb.path=/prometheus'
    ports:
     - 9090:9090
    depends_on:
     - prometheus-akka-http-example
  grafana:
    image: grafana/grafana
    ports:
     - 3000:3000
    depends_on:
     - prometheus
  prometheus-akka-http-example:
    image: hseeberger/scala-sbt
    entrypoint: sbt run
    volumes:
     - ./prometheus-akka-http-example:/root
    ports:
     - 8080:8080
{% endhighlight %}

Et son fichier de configuration associé `prometheus.yml` :

{% highlight yaml %}
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.

scrape_configs:
  - job_name: 'prometheus-akka-http-example'
    static_configs:
      - targets: ['prometheus-akka-http-example:8080']
{% endhighlight %}

Il faut ensuite démarrer les services :

{% highlight bash %}
$ docker-compose up
{% endhighlight %}

Puis configurer les dashboards et commencer à utiliser l'[application d'exemple][prometheus-akka-http-example]

[prometheus-akka-http-example]: https://github.com/wlk/prometheus-akka-http-example
[prometheus-akka-http]: https://github.com/lonelyplanet/prometheus-akka-http