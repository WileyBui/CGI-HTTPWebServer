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

  FILE *ptr = fopen("../conf/httpd.conf", "r");
  if (ptr == NULL) {
    printf("Error opening up configuration file.\n");
    exit(EXIT_FAILURE);
  }

  fscanf(ptr, "%i", &simultaneous_connections);
  fscanf(ptr, "%s", root_directory);
  fscanf(ptr, "%s", index_filename);
  fscanf(ptr, "%i", &port);
  fclose(ptr); // closing the file

  printf("Server settings from /conf/httpd.conf:\n");
  printf("  Number of simultaneous connections: %i\n",
         simultaneous_connections);
  printf("  Root directory (to start looking for HTML files): %s\n",
         root_directory);
  printf("  Index filename (if none given): %s\n", index_filename);
  printf("  Port to run on server: %i\n", port);

  char buffer[1024] = {0};
  int s, new_sock, client_header_value; // socket descriptor, s
  struct sockaddr_in address;           // an Internet endpoint address
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
      client_header_value = read(new_sock, buffer, 1024);
      printf("Received from the client: %s\n", buffer);
      close(new_sock);
    }
  }
  close(s);

  return 0;
}