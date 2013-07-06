---
layout: post
title: Can regexp loop?
date: 2012-09-07 11:20:49
category: General
noExcerpt: true
---

I have a very simple use case and I've come to conclusion that it is not possible to use regular expression to achieve
it.

I want to parse this string and get **all** numbers in it :

	(string) "what is the largest number : 384, 90, 6, 15"

That is the best I can with regex:

	(regexp) "what is the largest number : ((?:\d+, )+ \d+)"
	(group1) "384, 90, 6, 15"

Then result have to be splitted **manually**. This it clearly not the best pattern matching I have ever seen.

The thing is if I reverse the first group by using a capturing group into a non-capturing group then this is the last
captured group that I just can get it back:

	(regexp) "what is the largest number : (?:(\d+), )+ \d"
	(group 1) "6"
	(group 2) "15"

Anyway.