---
layout: post
title:  "How to use Processing as a library"
date:   2023-04-27 01:14:00 +0200
img_large: 2023-04-27-processing-into-central.png
---

I'm a big fan of [Processing], but I'm disappointed with lack of a reusable java library. This post explain some details
about it.

I don't recall exactly why I've written [this code][1], but it is an interesting way of writing Scala 3 code with
[Processing]. Here is a simplified example:

```scala
package name.lemerdy.sebastian.processing

import processing.core.PApplet

class GraphicExperiment extends PApplet:

  override def settings(): Unit =
    size(640, 320)

  override def draw(): Unit =
    background(color(255))
    stroke(color(0))
    line(width / 2f, 0, width / 2f, height.toFloat)
    if mousePressed then point(45f, 33f)

@main def launch(): Unit = PApplet.main(classOf[GraphicExperiment])
```

Please note that `PApplet` is a legacy name that no longer refers to old Java Applet.

Here is a sample result of this code:

![GraphicExperiment running result](/img/2023-04-27-GraphicExperiment.png)

Unfortunately, I think they don't want to put too much effort on distributing [Processing] as a Java
library. It looks like they want users to use their sketchbook interface instead. As a consequence, `core.jar` is not
available into [maven central repository] and needs to be built with [ant]ique tool.

### Processing dependency

To build `lib/core.jar` I've installed [ant] with [homebrew] and then:

```shell
cd ..
git clone git@github.com:processing/processing4.git
cd processing4/build
ant build
cp ../core/library/core.jar ../../lib/
```

`lib` is a convenient sbt convention that allow any scala project to include theses files into compile and runtime
classpath. It is documented as [Unmanaged dependencies][2].

[1]: https://github.com/seblm/code-samples/blob/main/scala/src/main/scala/name/lemerdy/sebastian/processing/GraphicExperiment.scala "GraphicExperiment.scala"
[2]: https://www.scala-sbt.org/1.x/docs/Library-Dependencies.html#Unmanaged+dependencies "Sbt's unmanaged dependencies"
[ant]: https://ant.apache.org "Web site of the Apache Ant project"
[maven central repository]: https://central.sonatype.com
[homebrew]: https://brew.sh "Web site of Homebrew: The Missing Package Manager for macOS (or Linux)"
[Processing]: https://processing.org "Web site of Processing: a flexible software sketchbook and a language for learning how to code."