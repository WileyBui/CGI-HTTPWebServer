// Server side C program to demonstrate Socket programming
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

#define PORT 8080
#define QLEN 10 // maximum server request queue length

int main(int argc, char const *argv[])
{
  int s, new_socket, client_header_value; // socket descriptor
  char buffer[1024] = {0};
  struct sockaddr_in address; // an Internet endpoint address
  socklen_t sock_size = sizeof(struct sockaddr_in);

  address.sin_family      = AF_INET;
  address.sin_addr.s_addr = INADDR_ANY;
  address.sin_port        = htons(PORT); // byte order

  // Allocate a socket
  s = socket(AF_INET, SOCK_STREAM, 0); // TCP uses SOCK_STREAM
  if (s < 0) {
    printf("Socket failed - can't be created.");
    exit(EXIT_FAILURE);
  }

  // Bind the socket
  if (bind(s, (struct sockaddr *)&address, sizeof(address)) < 0) {
    printf("Socket binding failed.");
    exit(EXIT_FAILURE);
  }

  // Put server to passive mode
  if (listen(s, QLEN) < 0) {
    printf("Listening failed.");
    exit(EXIT_FAILURE);
  }

  while (1) {
    new_socket = accept(s, (struct sockaddr *)&address, &sock_size);

    if (new_socket < 0) {
      printf("New socket failed.");
      exit(EXIT_FAILURE);
    }
    else {
      client_header_value = read(new_socket, buffer, 1024);
      printf("Received from the client: %s\n", buffer);
      close(new_socket);
    }
  }
  close(s);

  return 0;
}