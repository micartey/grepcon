# grepcon

<div align="center">
  <a href="https://www.oracle.com/java/">
    <img
        src="https://img.shields.io/badge/Made%20with-Java-red?style=for-the-badge"
        height="30"
    />
  </a>
  <a href="https://jitpack.io/#lukasl-dev/grepcon/1.0.6">
    <img
        src="https://img.shields.io/badge/Build-Jitpack-lgreen?style=for-the-badge"
        height="30"
    />
  </a>
  <a href="https://micartey.github.io/grepcon/docs" target="_blank">
    <img
        src="https://img.shields.io/badge/javadoc-reference-5272B4.svg?style=for-the-badge"
        height="30"
    />
    </a>
</div>

<br>

<p align="center">
  <a href="#-introduction">Introduction</a> •
  <a href="#-terms-of-use">Getting started</a> •
  <a href="https://github.com/micartey/grepcon/issues">Troubleshooting</a>
</p>

## 📚 Introduction

`grepcon` is a RESTapi to get the favicon from websites. This project was especially made for [nura-pwa](https://github.com/nura-vault/nura-pwa) to eventually replace the unofficial google api which is currently used. The google api sadly doesn't work for many websites and therefore is not sutible for my usecase.


## 📝 Getting started

Using `grepcon` is fairly easy. Firstly, clone the repository and use Docker to build and run the application

```shell
$ docker-compose up
```

Afterwards you can access the api via following RESTapi

```shell
$ curl https://localhost:8080/api/v1/favicon/list?url{URL}?fallback={FALLBACK_URL}
```

After invoking the endpoint, an array with urls to the different favicons will be returned.

Example:

```
[
    "https://www.google.com/favicon.ico"
]
```

### Get Image as Response

You can also get an image as an response. This is especially useful for saving images in a serviceworker or prevent cors errors.

```
$ curl https://localhost:8080/api/v1/favicon?url={URL}?fallback={FALLBACK_URL}
```

### Current Limitations

- Currently `grepcon` is not checking for resolution
- URLs are not always corrected
