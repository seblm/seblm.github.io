---
layout:    post
title:     Tester une expression régulière
date:      2011-02-04 08:17:49
tags:      General
noExcerpt: true
---

J'ai découvert assez récemment une nouvelle méthode de _JUnit 4_ :

{% highlight java %}
public static <T> void org.junit.Assert.assertThat(T actual, org.hamcrest.Matcher<T> matcher)
{% endhighlight %}

Elle permet d'écrire des tests assez lisibles du genre :

{% highlight java %}
assertThat("toto", notNullValue());
assertThat("toto", is("toto"));
{% endhighlight %}

Le propos ici est d'utiliser un `Matcher` spécifique qui valide que la valeur correspond au `Pattern`.

Exemple :

{% highlight java %}
assertThat("01 02 04 05 06", matchPattern(Pattern.compile("\\d{2}(?:\\s\\d{2}){4}"));
{% endhighlight %}

Voici donc le code de la classe de `Matcher` :

{% highlight java %}
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public final class MatchPattern extends BaseMatcher<String> {

    private final Pattern pattern;

    private MatchPattern(final Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern cannot be null");
        }
        this.pattern = pattern;
    }

    @Override
    public boolean matches(Object arg0) {
        return pattern.matcher((CharSequence) arg0).matches();
    }

    @Override
    public void describeTo(Description arg0) {
        arg0.appendText("String that matches pattern \"").appendText(pattern.pattern()).appendText("\"");
    }

    @Factory
    public static Matcher matchPattern(final Pattern pattern) {
        return new MatchPattern(pattern);
    }

}
{% endhighlight %}
