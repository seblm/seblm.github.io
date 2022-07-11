---
layout: post
title:  "Découverte d’OpenFeature et implémentation d’un provider pour Unleash"
date:   2022-07-11 06:05:00 +0200
tags:   Techno
---

La nécessité de créer des standards s’est souvent révélée utile même si des expériences passées (comme JEE par exemple)
nous montrent qu’il faut savoir rester raisonnable sur la portée d’une spécification. [OpenFeature][openfeature]
applique bien ce principe car il s’agit de standardiser les _if_ de nos applications. Tous ceux qui ont pratiqué un peu
sérieusement le feature switching savent que c’est plus compliqué que ça et que ça offre une souplesse très avantageuse
aux systèmes complexes. Voici le résumé de ce qu’est [OpenFeature][openfeature] et comment on met en œuvre un
_Provider_ en prenant en exemple [Unleash][unleash]

## Feature switch en deux mots

Quand on déploie des applications, on souhaite parfois les rendre souples sur certaines fonctionnalités :

 - pouvoir décorréler déploiement et activation de nouvelles fonctionnalités
 - pouvoir tester dans le contexte de production une nouvelle fonctionnalité à un petit groupe de bêta testeurs
 - pouvoir activer une fonctionnalité sur des critères complexes et dynamiques
 - pouvoir activer une fonctionnalité progressivement
 - pouvoir synchroniser l’activation d’une fonctionnalité sur plusieurs systèmes en même temps

Tous ces cas d’usages se révèlent extrêmement utiles. Pour y parvenir sans réinventer la roue, on peut utiliser des
outils ou des plateformes existantes mais aucun standard ouvert n’était pour l’instant défini. C’est ce
qu’[OpenFeature][openfeature] tente de proposer.

Nous n’en sommes qu’aux prémices mais cela semble intéressant.

## Définition d’un _Provider_ pour Unleash

[Unleash][unleash] est la solution que j’utilise au boulot. À priori il n’existe pas encore de _Provider_
[OpenFeature][openfeature] pour ce fournisseur.

Voici [un repository][unleash-provider] qui en défini un.

Un _Provider_ est un fournisseur de feature switch qu’on instancie à l’intérieur du client OpenFeature afin d’abstraire
les appels de son code au fournisseur tiers. Classique avec un standard : on ne dépend plus que de l’implémentation
custom mais bien de la librairie standard.

Voici un exemple d’utilisation possible pour le [Provider Unleash][unleash-provider] :

```java
// Unleash initialization
var unleash = new DefaultUnleash(UnleashConfig.builder()
                                              .appName("seblm-meals")
                                              .instanceId(InetAddress.getLocalHost().getHostName())
                                              .unleashAPI("https://feature-toggle.seblm-meals.net")
                                              .customHttpHeader("Authorization", "s3cr37-k3y==")
                                              .build());

// OpenFeature initialization
var api = OpenFeatureAPI.getInstance();
api.setProvider(new UnleashProvider(unleash));
var client = api.getClient();

// feature switch usage
var shouldDisplayNewMsg = client.getBooleanValue("new-welcome-message", false);
```

[openfeature]: https://openfeature.dev
[unleash]: https://getunleash.io
[unleash-provider]: https://github.com/seblm/unleash-openfeature-provider-java