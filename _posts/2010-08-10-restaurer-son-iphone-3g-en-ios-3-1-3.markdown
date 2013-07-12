---
layout: post
title: Restaurer son iPhone 3G en iOS 3.1.3
date: 2010-08-10 20:58:49
category: General
noExcerpt: true
---

Je viens de suivre
[cette procédure](http://www.01net.com/contenu/2562/ta_fiches/ios-4--repasser-son-iphone-sous-os-313-944-1). Ça a
fonctionné parfaitement pour moi.

![capture d'écran de iTunes montrant le numéro de version de l'iPhone]({{ site.url }}/img/iPhoneDowngrade.png)

J'apporte quelques précisions par rapport à l'article :

1. J'ai téléchargé
   [iOS 3.1.3](http://appldnld.apple.com.edgesuite.net/content.info.apple.com/iPhone/061-7468.20100202.pbnrt/iPhone1,2_3.1.3_7E18_Restore.ipsw)
   grâce à [cette page](http://www.hackint0sh.org/f127/22056.htm). Le md5 du fichier est
   `82e93e4e51b0e4503a8165507b8a3df2`.
1. Une fois dézippé, [RecBoot](http://www.hack2learn.org/downloads/RecBoot+-+Recovery+Boot+Tool+-+MacOSX) propose deux
   binaires. Il faut évidemment exécuter _RecBoot exit only_.
1. Ajouter les droits d'écriture de mon compte sur `/etc/hosts` n'a pas suffit à pouvoir l'enregistrer après
   modifications avec _TextEdit_. J'ai dû faire un `$ sudo vi /etc/hosts...`
1. Une fois l'iPhone débloqué j'ai supprimé cette entrée dans mon fichier hosts sans que cela ne pose problème.

J'espère que ces indications supplémentaires vous permettrons enfin de récupérer votre iPhone 3G dans le meilleur de sa
forme. J'attends maintenant une mise à jour de iOS 4 en tenant compte du processeur et de la RAM vieillissants de mon
iPhone 3G.

Cette expérience de mise à jour m'aura au moins appris à ne pas faire une confiance aveugle dans les logiciels d'Apple -
confiance qu'ils avaient pourtant su gagner assez facilement lors de mon passage sur Mac OS X.
