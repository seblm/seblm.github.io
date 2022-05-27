According to [documentation](https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll),
here are commands that needs to be run in order to get a functional jekyll local install:

```shell
$ gem install bundler
$ cat Gemfile
source 'https://rubygems.org'
gem 'github-pages', group: :jekyll_plugins
$ bundle install
```

In order to be up to date:

```shell
$ gem update bundler
$ bundle update
```

What is current version of jekyll?

```shell
bundle info jekyll
  * jekyll (3.9.0)
        Summary: A simple, blog aware, static site generator.
        Homepage: https://github.com/jekyll/jekyll
        Path: /Library/Ruby/Gems/2.6.0/gems/jekyll-3.9.0
```

In order to check from a fresh new site generation:

```shell
bundle exec jekyll _3.9.0_ new test
```

This way you can now compare your configuration with default one.

To check current theme source:

```shell
$ ls -l $(bundle info jekyll-theme-hacker | grep "Path:" | grep --only-matching "/.*")
total 32
-rw-r--r--  1 ***  staff  6555 Jun 13 22:27 LICENSE
-rw-r--r--  1 ***  staff  4814 Jun 13 22:27 README.md
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 _layouts
drwxr-xr-x  5 ***  staff   170 Jun 13 22:27 _sass
drwxr-xr-x  4 ***  staff   136 Jun 13 22:27 assets
```

At last to test your site locally:

```shell
bundle exec jekyll serve --drafts
```

## Prerequisites

According to [Jekyll documentation](https://jekyllrb.com/docs/installation/macos), you can rely on `chruby`:

```shell
brew install chruby ruby-install
ruby-install ruby
```

Then check your ruby version:

```shell
ruby --version
```

You can fix your ruby version (3.1.2 is an example):

```shell
echo "ruby-3.1.2" > .ruby-version
```

If it should be upgraded then type (3.1.2 is an example):

```shell
ruby-install ruby-3.1.2
chruby ruby-3.1.2
```

Remove previous ruby installation (2.6.4 is an example):

```shell
rm -rf ~/.rubies/ruby-2.6.4
```
