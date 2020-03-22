## Lab #1 - HTTP Web Server

## Authors
- Wiley Bui - 5368469 - buixx206@umn.edu
- Gus Eggers - - egger235@umn.edu

## Description
The purpose of this lab is to have a better understanding of socket programming as well as HTTP web server based on [RFC/1945](https://tools.ietf.org/html/rfc1945) (HTTP/1.0).

## Requirements
- A web browser for the client
  - Firefox, Google Chrome, or others
- Linux/Ubuntu terminal
  - C
  - gcc

## Usage
- The server requires the following options, go to `/conf/httpd.conf` to change its options:
    - Number of simultaneous connections
    - ROOT directory to start looking for html files
    - INDEX filename if none given (e.g. index.html)
    - PORT to run your server on

## How to Run the Program
1. Go to the `src` directory
    - To compile, open a terminal, type: `gcc server.c`
    - To start up the server, type: `./a.out`
1. Open up a web browser
    - Type `http://localhost:8080` to connect to the server