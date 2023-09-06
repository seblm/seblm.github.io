## Create from scratch

Create a docker volume to hold downloaded and built gems:

```shell
docker volume create gem_home
```

Install [GitHub Pages compliant versions](https://pages.github.com/versions) of Jekyll with ruby:

```shell
docker run --name seblm.github.io --rm --volume gem_home:/usr/local/bundle ruby:2.7.4 gem install jekyll --version 3.9.3
```

Create new Jekyll site:

```shell
docker run --name seblm.github.io --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site ruby:2.7.4 jekyll new --skip-bundle /usr/site
```

Comment `gem "minima", "~> 2.0"` and uncomment `gem "github-pages", group: :jekyll_plugins` into `"$(pwd)"/Gemfile`.

Install dependencies:

```shell
docker run --name seblm.github.io --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle install
```

## Run locally

```shell
docker run --name seblm.github.io --publish 4000:4000 --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle exec jekyll serve --drafts --host=0.0.0.0
```

## Update dependencies

```shell
docker run --name seblm.github.io --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle update
```

## Compare with fresh new site generation

```shell
docker run --name seblm.github.io --rm --volume gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site --workdir /usr/site ruby:2.7.4 bundle exec jekyll new test
```

This way you can now compare your configuration with default one.

## Theme

This site uses a [fork of minima](https://github.com/seblm/minima).
