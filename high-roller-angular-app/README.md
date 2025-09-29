# High Roller Network Angular App

This is a Web application that contains the necessary components to interact with the Rest API of the [High Roller Network Spring Boot application](https://github.com/rauljvr/applications/tree/main/roller-network). 

## Septup

* ***Angular***: 20.3.2
* ***Node***: 22.19.0
* ***Package Manager (NPM)***: 10.9.3
* ***Bootstrap***: 5.3.8

## Build

To compile the project and store the build artifacts in the `dist/` directory. By default, the production build optimizes the application for performance.

```bash
ng build
```

## Build and start the project

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open the browser and navigate to http://localhost:4200/

### Starting the application locally with Docker

> ### Build
>```bash
>docker build -t high-roller-angular:1.0.0 .
>```
> ### Run
>```bash
>docker run --name high-roller-angular -dp 4200:80 high-roller-angular:1.0.0
>```
> This will run the application locally on port 4200. Then open the browser and navigate to http://localhost:4200/

### Starting the application locally with Docker Compose

> ### Build
>```bash
>docker compose build
>```

> ### Build and Run
>```bash
>docker compose up --detach
>```
> This will run the application locally on port 4200. Then open the browser and navigate to http://localhost:4200/
