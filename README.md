# Music API

A Clojure program designed to be an API with CRUD routes for musics that store all the data in a mongo databse.
All the project environment is set using docker images.

## Requirements
- [Docker Desktop](https://www.docker.com/)  4.19.0v

## Usage

Open the terminal in the root directory and run the following commands to start the API. **(Make sure docker service is running)**

    cd docker
    docker-compose up
    
## API Routes

| Method     | URI                                           | Action                                                      |
|------------|-----------------------------------------------|-------------------------------------------------------------|
|  GET       | `localhost:3000`                              |  Tests if server is running                                 |
|  GET       | `localhost:3000/music`                        |  Gets all musics in mongo database                          |
|  GET       | `localhost:3000/music/{id}`                   |  Gets the music in mongo database with the id specified     |
|  POST      | `localhost:3000/music`                        |  Adds music to mongo database                               |
|  PUT       | `localhost:3000/music`                        |  Updates music in mongo database                            |
|  DELETE    | `localhost:3000/music/{id}`                   |  Deletes the music in mongo database with the id specified  |


### *GET* and *DELETE* URI exemple:
    localhost:3000/music/1
    
### *POST* and *PUT* body exemple:
    {
        "id": 999,
        "title": "The title of the music",
        "artist": "The artist of the music",
        "year": 1999,
        "genre": "The genre of the music",
        "rating": 5
    }
