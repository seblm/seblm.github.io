---
layout: post
title:  "Architecture hexagonale et programmation fonctionnelle"
date:   2019-10-24 05:15:00 +0200
categories: Techno
---
Après avoir présenté l'architecture hexagonale, j'ai eu le droit à une question _piège_ de la part d'un de mes
collègues : est-ce que cette architecture est compatible avec la programmation fonctionnelle. Plus précisément : si
j'isole mes effets de bords, ai-je toujours besoin de la partie infrastructure ?

## À première vue on peut se passer de l'infrastructure

Cette question m'a pris de court. Ce qui m'a embrouillé est la description de ce que le programme allait faire :

 1. une application instancie le domaine
 2. elle reçoit une commande qu'elle traduit en un appel au domaine
 3. le domaine décrit des effets pour capturer un contexte
 4. il calcule un résultat en appliquant des fonctions pure
 5. il décrit d'autres effets pour sauvegarder le résultat
 6. l'application applique ces effets

Dans cet enchaînement, on pourrait croire que la partie infrastructure finie par ne plus être nécessaire du tout.

## Reformulons

Si on reformule les points 3. et 5. :

 3. le domaine **sollicite des ports d'infrastructure qui lui décrivent** des effets pour capturer un contexte
{:start="3"}
 5. il **sollicite des ports d'infrastructure qui lui décrivent** d'autres effets pour sauvegarder le résultat
{:start="5"}

On constate donc que le découplage de l'infrastructure reste nécessaire. La subtilité constiste à dire que ces adapters
ne vont plus exécuter les effets de bord mais seulement les décrire. C'est l'application qui finira par les exécuter.

## Conclusion

Si on se donne comme objectif d'écrire du code fonctionnel pur qui wrappe les effets de bord dans des constructions
adhoc, ce n'est pas pour ça que l'intérêt et la nécessité d'isoler ces parties dans du code d'infrastructure en dehors
du domaine disparait. 