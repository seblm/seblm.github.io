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
$ gem update bundler
$ bundle update
```

In order to check from a fresh new site generation:

```bash
bundle exec jekyll _3.8.5_ new test
```

This way you can now compare your configuration with default one.

To check current theme source:

```bash
$ ls -l $(bundle show jekyll-theme-hacker)
total 32
-rw-r--r--  1 ***  staff  6555 Jun 13 22:27 LICENSE
-rw-r--r--  1 ***  staff  4814 Jun 13 22:27 README.md
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 _layouts
drwxr-xr-x  5 ***  staff   170 Jun 13 22:27 _sass
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 assets
```

At last to test your site locally:

```bash
bundle exec jekyll serve --drafts
```

## Prerequisites

Instead of relying on system ruby, I prefer upgrading to latest ruby with rvm. Once rvm is installed, be sure to have
latest rvm version with:

```bash
rvm get stable
```

Then check your ruby version:

```bash
rvm list
```

If it should be upgraded then type (2.6.4 is an example):

```bash
rvm install ruby-2.6.4
rvm use ruby-2.6.4
```

Remove previous ruby installation (2.4.1 is an example):

```bash
rvm remove ruby-2.4.1
```
