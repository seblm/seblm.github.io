---
date:      2024-10-20 14:58:00 +0200
layout:    post
title:     "Qu’est-ce qu’un store selon Svelte ?"
img_large: 2024-10-20-qu-est-ce-qu-un-store-selon-svelte.png
---

Selon [Svelte], il est très rare que l’état de l’application puisse résider entièrement à l’intérieur de la hiérarchie de composants de
l’application. L’usage de stores s’applique lorsque des valeurs doivent être accédés par :

 - des composants qui ne sont pas liés entre eux
 - des modules JavaScript externes

Mise-à-jour du lundi 28 octobre 2024 : avec la sortie de Svelte 5 et de la notion de runes, les stores ne sont plus la manière idiomatique
de gérer un état réactif en dehors des composants. Ils conservent toutefois un article dans le tutorial car ils avouent que cette notion
existe encore pour le moment dans SvelteKit.

Je commence à comprendre pourquoi les développeurs fronts dans ma boîte acceptent de ne jamais finir les refactos ou les migrations : ils
changent trop souvent d’avis.

[Svelte]: https://svelte.dev/tutorial/svelte/introducing-stores
