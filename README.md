# CGI HTTP Web Server

### Authors
- [Wiley Bui](https://github.com/WileyBui) - 5368469 - buixx206@umn.edu
- [Gus Eggers](https://github.com/GusEggers) - 5220957 - egger235@umn.edu

### Project Goals
The main goal of this lab is to create a CGI HTTP web server based on the [RFC/1945](https://tools.ietf.org/html/rfc1945). The server is built on C socket programming and abstraction learned so far in INET 4021 - Network Programming. 

Additionally, the server should be efficient in regards to speed and resource allocation. 

### Algorithms & Abstractions
Our server uses CGI as an abstraction to take inputs from an HTML form and then return the output. The client has no idea the CGI is there but is happy when it receives a response, regardless of whom it's from. 

As for our algorithm, the server takes in input, then parses it to find if it's a POST/GET request, then sends the data to the CGI for processing.

### Content Description
- __*/cgi-bin -*__ This contains our CGI files used to handle POST and GET requests (where the executable webserver resides).

    When a client sends a POST/GET request to the server, the server will call the CGI executable to handle the request. The main use for this is to allow the executable to handle the work, while the server just passes it off and then can handle new requests. This is a good use of resource allocation and helps support more users concurrently.

- __*/conf -*__ The config folder contains httpd.conf file which is a configuration file for the server. It states the amount of concurrent users, root content location, index location, and port to be run on.

- __*/contents -*__ All of the .html, .gif, .jpg (contents) reside in this folder. When the index.html is received, it sends requests for all these files. These files are used to make the webpage that is displayed on the client's browser. 

- __*/logs -*__ This folder contains the log files for all access commands as well as errors. The standard out will go to the access log and all other error logs go to the error log.

- __*/src -*__ This is where all of our source code resides. This is essentially the folder where the server is located.

- __*index.html -*__ Webserver directory index

### Set-up & Usability
- To compile the GET and POST CGI, change to the `/src` directory. Compile the C file. This can be done by 

        gcc POST.c -o ../cgi-bin/POST.cgi && gcc GET.c -o ../cgi-bin/GET.cgi
- To test the server, go to the `/src` directory. Compile the C file and run it. This can be done by:

        gcc server.c && ./a.out

The server will run based on the contents specified in the config file. If unchanged, the terminal will return:

        Number of simultaneous connections: 10
        Root directory (to start looking for HTML files): ../contents/
        Index filename (if none given): /index.html
        Port to run on server: 8080

Now, using a web browser, enter "http://localhost:8080/". This will load the `index.html` file and display the webpage.
Additionally, you can load certain files by doing "http://localhost:8080/FILENAME". If the file exists, it will be displayed (shown below). Otherwise, there will be a `Error 404 - File Not Found` message. 

![Screnshot showing localhost:8080](contents/images/localhost_screenshot.png?raw=true "localhost:8080")

Before we begin, it is important to note that when the server sends a message to the web browser client, it is preceded by an HTTP header. An example of this is :

```
   HTTP/1.0 404 Not Found
   Content-Type: text/plain
   Content-Length: 25
   Connection: close
   Last-Modified: Mon, 23 Mar 2020 02:49:28 GMT
   Expires: Sun, 17 Jan 2038 19:14:07 GMT
   Date: Mon, 23 Mar 2020 04:49:28 GMT   
   
   HTTP 404 - File not found
```

This is sending an error back the HTTP client because the requested file can't be found. Other forms use a "200 OK", "500 Internal Server Error", and "501 Not Implemented", instead of "404 Not Found", but otherwise hold the same form. You edit the second line based on what content you're sending. You can then send data after two new lines.

When a client requests for a file, the server receives a GET request. Each request may have many iterative calls to the server for addition files, such as the `index.html` request to call for other images. The server then sends the request to the CGI (Common Gateway Interface). The goal of using CGI is for dynamic web interactions, such as a form, by adding backend applications that take data (inputs) from an HTML form. Another type of request is a POST. This is when the client sends data to the server. We'll provide an example of each.

__*POST Example:*__
If you go to "http://localhost:8080/form.htm", you can send a POST message to the server. The form will get sent to and handled by the executable CGI webserver as a POST request. Our example form takes in two word inputs, sends the form and data to CGI, and CGI sends back the form and the content (which is the string concatenated). This is an example of how CGI handles POST requests.

__*GET Example:*__
This is when the client is trying to get a file from the server. This happens frequently, especially when the <index.htm> file is requested. CGI tries to find the file, if it can't then it sends the 404 Not Found error, found above, to the client.

If the file is found, then the server sends a request back, such as
```
    HTTP/1.0 200 OK
    Content-Type: image/gif
    Content-Length: 1231 
    Cache-Control: no-cache
    Connection: Keep-Alive
    Last-Modified: Mon, 23 Mar 2020 02:49:28 GMT
    Expires: Sun, 17 Jan 2038 19:14:07 GMT
    Date: Mon, 23 Mar 2020 04:49:28 GMT
   
    CONTENT
```

The first chunk of text is the HTTP header. The CONTENT would be replaced by the contents of the file. The browser would then display this content. That's how CGI handles GET requests.

### Purpose
Assuming the client is the user and the admin is the server...

Admin - The purpose of this project for admins is to see how to handle POST and GET requests in an executable webserver. Programming the server also helps understand how to send HTTP headers and communicate with a webpage, as well as browsing for files and sending them to a webpage. 

User - The purpose of this project for users is to learn how to navigate webpages and what certain errors mean when received. For example, if trying to load a file from the server that doesn't exist, there is going to be a 404 File Not Found error.