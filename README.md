According to [documenation](https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll),
here are commands that needs to be run in order to get a functional jekyll local install:

```bash
$ gem install bundler
$ cat Gemfile
source 'https://rubygems.org'
gem 'github-pages', group: :jekyll_plugins
$ bundle install
```

In order to be up to date:

```bash
$ bundle update
```
