---
layout:    post
title:     Comment tester en Java un client HTTP sans se connecter ?
date:      2011-02-06 20:52:49
tags:      Techno
noExcerpt: true
---

C'est [cet article](http://java.sun.com/developer/onlineTraining/protocolhandlers) datant d'octobre 2000 (!) qui m'a
montré la voie. Il explique en détail le mécanisme utilisé par la JVM pour invoquer le bon composant permettant
d'interpréter correctement un protocole décrit dans une `java.net.URL`.

Pour montrer l'utilité de ce mécanisme dans un cas de programmation orientée par les tests, je vais prendre l'exemple de
l'écriture d'une classe permettant de géocoder une adresse à travers
l'[API Google Maps](http://code.google.com/intl/fr/apis/maps/documentation/geocoding).
Cette API est accessible à travers le protocole HTTP. Pour ne pas solliciter constamment le service web nous allons donc
mocker l'appel en se basant sur
[la documentation du service](http://code.google.com/intl/fr/apis/maps/documentation/geocoding/#XML).

Remplacer le composant HTTP standard
------------------------------------

Lors de l'appel à `java.net.URL.openStream()` la JVM interprète le protocole contenu dans l'URL. Si c'est `"http"` la
JVM instanciera par défaut une `sun.net.www.protocol.http.HttpURLConnection` qui gèrera le dialogue avec le serveur. En
définissant la propriété `java.protocol.handler.pkgs` avec un nom de package, la JVM introspectera ce package suivi du
nom du protocole et de la classe nommée Handler et devant étendre `java.net.URLStreamHandler`.

Cela se traduit par le code suivant dans notre test unitaire :

{% highlight java %}
@BeforeClass
public static void setupMockHTTP() {
    System.setProperty("java.protocol.handler.pkgs", "name.lemerdy.sebastian.mock");
}
{% endhighlight %}

Et par les deux classes suivantes dans le package `name.lemerdy.sebastian.mock.http` :

{% highlight java %}
package name.lemerdy.sebastian.mock.http;

import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url);
    }

}
{% endhighlight %}

{% highlight java %}
package name.lemerdy.sebastian.mock.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class URLConnection extends java.net.URLConnection {

    protected URLConnection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!connected) {
            connect();
        }
        return this.getClass().getResourceAsStream(url.getPath());
    }

}
{% endhighlight %}

Cette seconde classe ouvre un fichier dont le chemin dans le classpath est la même que celle de l'URL.

Mise en oeuvre dans l'exemple
-----------------------------

Maintenant qu'on a remplacé l'envoi d'une requête HTTP par l'ouverture d'un fichier du classpath on peut donc tester
notre service dont voici un extrait du code :

{% highlight java %}
public static double[] geocode(String address) {
    double[] coordinates = null;
    if (address != null && !address.isEmpty()) {
        try {
            address = URLEncoder.encode(address, Charset.defaultCharset().name());
            final URL url = new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=" + address + "&sensor=false");
            final XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = xPath.compile("/GeocodeResponse/result/geometry/location/lat|/GeocodeResponse/result/geometry/location/lng");
            final NodeList location = (NodeList) xPathExpression.evaluate(new InputSource(url.openStream()), XPathConstants.NODESET);
            if (location != null) {
                coordinates = new double[2];
                for (int i = 0; i < 2; i++) {
                    if (location.item(i) != null) {
                        coordinates[i] = Double.parseDouble(location.item(i).getTextContent());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (XPathExpressionException e) {
        }
    }
    return coordinates;
}
{% endhighlight %}

Enfin voici le test du code ci-dessus :

{% highlight java %}
@Test
public void testGeocode() {
    assertThat(GeocodeService.geocode("1600 Amphitheatre Parkway, Mountain View, CA"), is(new double[] { 37.4217550d, -122.0846330d }));
}
{% endhighlight %}

Puisque le code à tester se connecte à l'URL `http://maps.googleapis.com/maps/api/geocode/xml` il suffit donc maintenant
de créer un fichier nommé `xml` dans le package `maps.api.geocode` et dont le contenu sera envoyé lors de l'éxécution du
test. Ce fichier XML pourra donc ressembler à ceci :

{% highlight xml %}
<GeocodeResponse>
    (...)
    <result>
    (...)
        <geometry>
            <location>
                <lat>37.4217550</lat>
                <lng>-122.0846330</lng>
            </location>
            (...)
        </geometry>
        (...)
    </result>
    (...)
</GeocodeResponse>
{% endhighlight %}
