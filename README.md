# Spring Music Player backend

Spring REST backend for the [JavaFX music player](https://github.com/benalcocer/music-player "player"). A proof of concept application.

Go to https://min.io/docs/minio/windows/index.html to setup MinIO for Windows.

### Features

- JWT bearer token Authentication
- MongoDB connections to retrieve, save, and delete data (users, playlists, and songs)
- Support for MinIO S3 storage (storage used for songs)
- AWS signing support for MinIO private access
- SMTP email server connection for sending verification codes