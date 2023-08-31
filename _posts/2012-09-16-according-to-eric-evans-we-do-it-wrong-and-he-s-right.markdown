---
layout:    post
title:     "According to Eric Evans we do it wrong and he's right"
date:      2012-09-16 09:33:49
noExcerpt: true
---

I just started to read _Domain-Driven Design_ by Eric Evans and some part of this book exactly describes what we do
wrong at work :

> Other projects use an iterative process, but they fail to build up knowledge because they don't abstract. Developers
> get the experts to describe a desired feature and then the go build it. They show the experts the result and ask
> what to do next. If the programmers practice refactoring, they can keep the software clean enough to continue
> extending it, but if programmers are not interested in the domain, they learn only what the application should do,
> not the principles behind it. Useful software can be built that way, but the project will never arrive at a point
> where powerful new features unfold as corollaries to older features.

To avoid what it is described it seems that we need to think about building a strong model with behaviors :

> Developing a knowledge-rich model. The objects had behavior and enforced rules. The model wasn't just a data schema;
> it was integral to solving a complex problem. It captured knowledge of various kinds.

It will be long and painful to catch up but I think that it worth it.
