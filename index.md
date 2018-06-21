---
# You don't need to edit this file, it's empty on purpose.
# Edit theme's home layout instead if you wanna make some changes
# See: https://jekyllrb.com/docs/themes/#overriding-theme-defaults
layout: default
---

{% for post in site.posts %}<div class="post">
  <p class="day-date">{{ post.date | date_to_long_string }}</p>
  <h2 class="post-title"><a href="{{ site.url }}{{ post.url }}">{{ post.title }}</a></h2>
  <div class="post-content" lang="fr">
    {% if post.noExcerpt %}
      {{ post.content }}
    {% else %}
      {{ post.excerpt }}
      <p><a href="{{ site.url }}{{ post.url }}" title="Read {{ post.url }}">Read more</a></p>
    {% endif %}
  </div>
</div>{% endfor %}
