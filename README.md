## Create from scratch

Install Jekyll with ruby:

```shell
docker compose --profile install up --detach
```

Create new Jekyll site:

```shell
docker run --name seblm.github.io --rm --volume seblmgithubio_gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site ruby:3.3.5 jekyll new /usr/site
```

Comment `gem "minima", "~> 2.0"` and uncomment `gem "github-pages", group: :jekyll_plugins` into `"$(pwd)"/Gemfile`.

## Install and run locally

```shell
docker compose up --detach
```

## Update dependencies before running

```shell
docker compose --profile update up --detach
```

## Compare with fresh new site generation

```shell
docker run --name seblm.github.io --rm --volume seblmgithubio_gem_home:/usr/local/bundle --volume "$(pwd)":/usr/site ruby:3.3.5 jekyll new /usr/site/test
```

This way you can now compare your configuration with default one.

## Theme

This site uses a [fork of minima](https://github.com/seblm/minima).
