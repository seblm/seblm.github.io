---
layout: post
title:  "How to master hibernatemode on your mac"
date:   2012-11-04 21:35:49
tags:   General
---

Do you know that you can turn your mac into hibernation mode whenever you want ? Not after it runs out of battery power.
With this blog entry I will describe how do I used to do this until Snow Leopard and how do I do it now with Mountain
Lion.

Before [OS X 10.8.2 Mountain Lion](http://www.apple.com/osx), I used to use
[DeepSleep widget](http://deepsleep.free.fr/). Despite it's installed and available on Dashboard and you have to switch
to this view to handle hibernation mode, it suited my needs.

But after upgrading to Mountain Lion, this piece of software doesn't works anymore. This is an open source tool. As a
result, source code is freely available to watch (and to contribute). We can see that DeepSeep try to set hibernatemode
setting of pmset utility to 0, 1, 3, 5 or 7.

{% highlight c %}
if (swap.xsu_encrypted) {                   /* Check if the virtual memory is encrypted */
	default_mode = 7;
	if (target_suspend == dump) {
		target_mode = default_mode; /* If so, we will sleep with hibernate mode 7 for safe hardware suspend */
	} else /*if (target_suspend == soft)*/ {
		target_mode = 5;            /* or with hibernate mode 5 for software suspend */
	}
	if (debug) printf("VM is encrypted\n");
}
else {
	default_mode = 3;
	if (target_suspend == dump) {
		target_mode = default_mode; /* else, we will use the regular mode 3 for safe hardware suspsend */
	} else /*if (target_suspend == soft)*/ {
		target_mode = 1;            /* or the regular mode 1 for software suspsend */
	}
	if (debug) printf("VM is not encrypted\n");
}
if (target_suspend == hard)                 /* If we only want to perform basic hardware suspend */
	target_mode = 0;                    /* we will sleep with hibernate mode 0 */
if (debug) printf("target mode: %d\n", target_mode);
{% endhighlight %}

According to `man pmset`, only 0, 3 or 25 value is relevant :

    We do not recommend modifying hibernation settings. Any changes you make are not supported. If you choose to do so
    anyway, we recommend using one of these three settings. For your sake and mine, please don't use anything other 0, 3,
    or 25.

    hibernatemode = 0 (binary 0000) by default on supported desktops. The system will not back memory up to persistent
    storage. The system must wake from the contents of memory; the system will lose context on power loss. This is, his-
    torically, plain old sleep.

    hibernatemode = 3 (binary 0011) by default on supported portables. The system will store a copy of memory to persis-
    tent storage (the disk), and will power memory during sleep. The system will wake from memory, unless a power loss
    forces it to restore from disk image.

    hibernatemode = 25 (binary 0001 1001) is only settable via pmset. The system will store a copy of memory to persistent
    storage (the disk), and will remove power to memory. The system will restore from disk image. If you want "hiberna-
    tion" - slower sleeps, slower wakes, and better battery life, you should use this setting.

Here is my solution : edit `~/.bash_profile` to add this functions and replace `**` by your account password :

{% highlight bash %}
function sleepfast() {
  echo "**" | sudo -S pmset -a hibernatemode 0 2> /dev/null
}

function sleepsafe() {
  echo "**" | sudo -S pmset -a hibernatemode 3 2> /dev/null
}

function hibernate() {
  echo "**" | sudo -S pmset -a hibernatemode 25 2> /dev/null
}

function hibernatemode() {
  hibernatemode=`pmset -g | grep em`
  if   [[ "${hibernatemode}" == " hibernatemode        0" ]]; then
    echo "sleepfast"
  elif [[ "${hibernatemode}" == " hibernatemode        3" ]]; then
    echo "sleepsafe"
  elif [[ "${hibernatemode}" == " hibernatemode        25" ]]; then
    echo "hibernate"
  else
    echo "unknown"
  fi
}
{% endhighlight %}

Start a new Terminal or `source ~/.bash_profile`. As this file will now contains your account password then
`chmod go-rwx` it. Now to know on which hibernatemode my machine is, I just have to type

{% highlight bash %}
$ hibernatemode
sleepfast
{% endhighlight %}

To switch to a given mode, type one of these commands

{% highlight bash %}
$ hibernate
$ sleepfast
$ sleepsafe
{% endhighlight %}
