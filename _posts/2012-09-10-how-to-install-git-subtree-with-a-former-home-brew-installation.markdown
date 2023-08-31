---
layout:    post
title:     How to install git subtree with a former home brew installation
date:      2012-09-10 20:55:49
noExcerpt: true
---

Since 1.7.11 [git subtree](https://github.com/apenwarr/git-subtree/commit/d7965e88248bd38415e271eee3480394626dd3d6) has
been released as an official _contrib_. Here are commands that I had to type in into my Terminal in order to makes this
script available:

{% highlight bash %}
$ cd /usr/local/share/git-core/contrib/subtree
$ make
$ sudo make prefix=/usr/local/Cellar/git/1.7.12 install
{% endhighlight %}

I think that theses commands may be more accurate (prefix is dependent to current version of git that is currently
installed) but does the job. Hopes this help.