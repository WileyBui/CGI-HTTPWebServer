# INET4021 Lab 1

### Authors
- Wiley Bui - 5368469 - buixx206@umn.edu
- Gus Eggers - 5220957 - egger235@umn.edu

### Project Goals
The main goal of this lab is to create a HTTP web server based on RFC/1945 (see Communication and Tasks). The server is built on C socket programming and abstraction learned so far in class. 

Additionally, the server should be efficient in regards to speed and resource allocation. 

### Content Description
__*/cgi-bin -*__ This contains our CGI files used to handle POST and GET requests (where the executable webserver resides).

When a client sends a POST/GET request to the server, the server will call the CGI executable to handle the request. The main use for this is to allow the executable to handle the work, while the server just passes it off and then can handle new requests. This is a good use of resource allocation and helps support more users concurrently.

__*/conf -*__ The config folder contains one .conf file which is a configuration file for the server. It states the amount of concurrent users, root content location, index location, and port to be run on.

__*/contents -*__ All of the .html, .gif, .jgp (contents) reside in this folder. When the index.html is received, it sends requests for all these files. These files are used to make the webpage that is displayed on the client's browser. 

__*/logs -*__ This folder contains the log files for all access commands as well as errors. The standard out will go to the access log and all other error logs go to the error log.

__*/src -*__ This is where all of our source code resides. This is essentially the folder where the server is located.

### Set-up & Usability
To test the server, change to the /src directory. Compile the c file and run it. This can be done by "gcc server.c && ./a.out"
The server will run on the port specified in the config file. If unchanged, it will be port 8080. 

Now, using a web browser, enter "http://localhost:8080/". This will load the index.htm file and display the webpage.
Additionally, you can load certain files by doing "http://localhost:8080/FILENAME". If the file exists, it will be displayed. 
If it doesn't exist, there will be a Error 404 - File Not Found

Before we begin, it is important to note that when the server sends a message to the web browser client, it is preceded by an HTTP header. An example of this is :

```
   "HTTP/1.0 404 Not Found\r\n"
   "Content-Type: text/plain\n"
   "Connection: close\n\n"
   "HTTP 404 - File not found";
```

This is sending an error back the HTTP client because the requested file can't be found. Other forms use a "200 OK", instead of "404 Not Found", but otherwise hold the same form. You edit the second line based on what content you're sending. You can then send data after two new lines.

When a client requests for a file, the server receives a GET request. The server then sends the request to the CGI (common gateway interface). The goal of using CGI is for dynamic web interactions, such as a form, by adding backend applications that take data (inputs) from an HTML form. Another type of request is a POST. This is when the client sends data to the server. We'll provide an example of each. 




__*POST Example:*__
If you go to "http://localhost:8080/form.htm", you can send a POST message to the server. The form will get sent to and handled by the executable CGI webserver as a POST request. Our example form takes in two word inputs, sends the form and data to CGI, and CGI sends back the form an the content (which is the string concatenated. This is how CGI handles POST requests.

__*GET Example:*__
This is when the client is trying to get a file from the sever. This happens frequently, especially when the <index.htm> file is requested. CGI tries to find the file, if it can't then it sends the 404 Not Found error, found above, to the client.

```
If the file is found, then we'd send something back like : 
   "HTTP/1.0 200 OK\r\n"
   "Content-Type: %s\n\n"
   
   CONTENT
```

The CONTENT would be replaced by the contents of the file. The browser would then display this content. That's how CGI handles Get requests.

### Communication and Tasks
- [See here](https://docs.google.com/document/d/1b5y2U4AuAkZoI1Iazu_hnQO1CJy929CKRletR1ds0Uc/edit).