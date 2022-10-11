## Create from scratch

Create a docker volume to hold downloaded and built gems:

```shell
docker volume create gem_home
```

Install [GitHub Pages compliant versions](https://pages.github.com/versions) of Jekyll with ruby:

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle ruby:2.7.4 gem install jekyll --version 3.9.2
```

Create new Jekyll site:

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site ruby:2.7.4 jekyll new --skip-bundle /usr/site
```

Comment `gem "minima", "~> 2.0"` and uncomment `gem "github-pages", group: :jekyll_plugins` into `"$(pwd)"/Gemfile`.

Install dependencies:

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle install
```

## Run locally

```shell
docker run --name ruby --publish 4000:4000 --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle exec jekyll serve --drafts --host=0.0.0.0
```

## Update dependencies

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle update
```

## Compare with fresh new site generation

```shell
docker run --name ruby --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle exec jekyll new test
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
