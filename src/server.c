#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

int main(int argc, char const *argv[])
{
  // will be imported from httpd.conf file
  char root_directory[50];
  char index_filename[50];
  int simultaneous_connections, port;

  printf("\nImporting settings from /conf/httpd.conf...\n");
  FILE *ptr = fopen("../conf/httpd.conf", "r");
  if (ptr == NULL) {
    printf("Error opening up configuration file.\n");
    exit(EXIT_FAILURE);
  }

  // read information from httpd.conf line by line
  if ((fscanf(ptr, "%i", &simultaneous_connections) < 0) ||
      (fscanf(ptr, "%s", root_directory) < 0) ||
      (fscanf(ptr, "%s", index_filename) < 0) ||
      (fscanf(ptr, "%i", &port) < 0)) {
    printf(" Configuration failed - make sure you include:\n"
           "   - number of simultaneous connections\n"
           "   - root directory to start looking for HTML files\n"
           "   - index filename, if none given\n"
           "   - port number to run on the server\n");
    fclose(ptr);
    exit(EXIT_FAILURE);
  }
  fclose(ptr); // closing the file

  printf("  Number of simultaneous connections: %i\n",
         simultaneous_connections);
  printf("  Root directory (to start looking for HTML files): %s\n",
         root_directory);
  printf("  Index filename (if none given): %s\n", index_filename);
  printf("  Port to run on server: %i\n\n", port);

  char buffer[1024] = {0};
  int s, new_sock;            // socket descriptor, s
  struct sockaddr_in address; // an Internet endpoint address
  socklen_t sock_size = sizeof(struct sockaddr_in);

  address.sin_family      = AF_INET;
  address.sin_addr.s_addr = INADDR_ANY;
  address.sin_port        = htons(port); // byte order

  // Allocate a socket
  s = socket(AF_INET, SOCK_STREAM, 0); // TCP uses SOCK_STREAM
  if (s < 0) {
    printf("Socket failed - can't be created.\n");
    exit(EXIT_FAILURE);
  }

  // Bind the socket
  if (bind(s, (struct sockaddr *)&address, sizeof(address)) < 0) {
    printf("Socket binding failed.\n");
    exit(EXIT_FAILURE);
  }

  // Put server to passive mode
  if (listen(s, simultaneous_connections) < 0) {
    printf("Listening failed.\n");
    exit(EXIT_FAILURE);
  }

  // Accepting new connections
  while (1) {
    new_sock = accept(s, (struct sockaddr *)&address, &sock_size);

    if (new_sock < 0) {
      printf("New socket failed.\n");
      exit(EXIT_FAILURE);
    }
    else {
      // "Fork() can be used at the place where there is division of work like a
      // server has to handle multiple clients, So parent has to accept the
      // connection on regular basis, So server does fork for each client to
      // perform read-write.""
      int pid = fork();

      if (pid == 0) { // child
        close(s);     // I am now the client - close the listener: client doesnt
                      // need it
        read(new_sock, buffer, 1024);
        printf("Received from the client:\n%s\n", buffer);
        char *hello = "HTTP/1.1 200 OK\r\n"
                      "Content-Type: text/plain\n"
                      "Content-length: 5\n"
                      "Connection: close\n\n"
                      "Hello\n";
        send(new_sock, hello, strlen(hello), 0);
        printf("Server sent: %s", hello);

        exit(0);
      }
      else if (pid > 0) { // parent
        close(new_sock);  // I am still the server - close the accepted handle:
                          // server doesn't need it.
      }
      else {
        printf("fork() failed.\n");
        exit(EXIT_FAILURE);
      }
    }
  }
  // close(s);

  return 0;
}