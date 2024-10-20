---
date:      2024-10-20 14:58:00 +0200
layout:    post
title:     "Qu’est-ce qu’un store selon Svelte ?"
img_large: 2024-10-20-qu-est-ce-qu-un-store-selon-svelte.png
---

Selon [Svlete], il est très rare que l’état de l’application puisse résider entièrement à l’intérieur de la hiérarchie de composants de
l’application. L’usage de stores s'applique lorsque des valeurs doivent être accédés par :

 - des composants qui ne sont pas liés entre eux
 - des modules JavaScript externes

[Svlete]: https://learn.svelte.dev/tutorial/writable-stores
