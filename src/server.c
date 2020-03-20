#include <ctype.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

// will be imported from httpd.conf file in main()
char root_directory[50];
char index_filename[50];

char *get_content_type(char *filename)
{
  char *file_path = strtok(filename, ".");
  char *file_extension;
  while (file_path != NULL) { // find the file extension
    file_extension = file_path;
    file_path      = strtok(NULL, " ");
  }

  // sets file extension to lowercase in order to compare strings
  for (int i = 0; file_extension[i]; i++) {
    file_extension[i] = tolower(file_extension[i]);
  }

  // comparing the strings to match with its corresponding type
  if ((strcmp(file_extension, "jpeg") == 0) ||
      (strcmp(file_extension, "jpg") == 0)) {
    return "image/jpeg";
  }
  else if (strcmp(file_extension, "gif") == 0) {
    return "image/gif";
  }
  else if ((strcmp(file_extension, "html") == 0) ||
           (strcmp(file_extension, "htm") == 0)) {
    return "text/html";
  }
  else {
    printf(
        "Can't determine content type; received %s as file extension instead.",
        file_extension);
    return "undetermined";
  }
}

int sock_from_client(int soc_file_descriptor)
{
  char *data_to_client;
  char *method_type, *filename;
  char buffer[1024] = {0};
  read(soc_file_descriptor, buffer, 1024);

  char *get_first_line      = strtok(buffer, "\n");
  char *get_words_from_line = strtok(get_first_line, " ");

  // going through a list of words on the very first line
  for (int i = 0; i < 2; i++) {
    if (i == 0) {
      // retrieve the header's method: GET, POST, ...
      // ex) GET ----------------> GET /favicon.ico HTTP/1.1
      method_type = get_words_from_line;
    }
    else {
      // retrieve the header's filename: index.htm, home.htm, ...
      // ex) /favicon.ico in ----> GET /favicon.ico HTTP/1.1
      filename = get_words_from_line;
    }
    get_words_from_line = strtok(NULL, " "); // sets pointer to next word
  }

  printf("Searching for \"%s\" file: ", filename);
  strcat(root_directory, filename);
  if (access(root_directory, F_OK) < 0) {
    printf("does NOT exist.\n");

    data_to_client = "HTTP/1.1 200 OK\r\n"
                     "Content-Type: text/plain\n"
                     "Connection: close\n\n"
                     "HTTP 404 - File not found";
    send(soc_file_descriptor, data_to_client, strlen(data_to_client), 0);
  }
  else {
    printf("does exist.\n");

    FILE *file = fopen(root_directory, "r");
    fseek(file, 0, SEEK_END);
    long fsize = ftell(file);
    fseek(file, 0, SEEK_SET); /* same as rewind(file); */

    char *string = malloc(fsize + 1);
    fread(string, 1, fsize, file);
    fclose(file);

    char send_to_client[10000];
    char *content_type = get_content_type(filename);
    sprintf(send_to_client,
            "HTTP/1.1 200 OK\r\n"
            "Content-Type: %s\n"
            "Connection: close\n\n"
            "%s",
            content_type, string);
    printf("============\nTHE FOLLOW HAS SENT TO THE CLIENT:\n%s; %s\n============\n\n", send_to_client,
           content_type);
    send(soc_file_descriptor, send_to_client, strlen(send_to_client), 0);
  }

  if (strcmp(method_type, "GET") == 0) {
    printf("Received GET method\n");
  }
  else if (strcmp(method_type, "POST") == 0) {
    printf("Received POST method\n");
  }
  else {
    printf("Received some other methods: %s\n", method_type);
  }

  return 0;
}

int main(int argc, char const *argv[])
{
  // will be imported from httpd.conf file
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

  int s, new_sock;            // socket descriptor
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
        sock_from_client(new_sock);
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