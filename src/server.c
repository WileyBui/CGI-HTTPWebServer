#include <ctype.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/sendfile.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/types.h>
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
    return "image/jpeg\n\n";
  }
  else if (strcmp(file_extension, "gif") == 0) {
    return "image/gif\n\n";
  }
  else if ((strcmp(file_extension, "html") == 0) ||
           (strcmp(file_extension, "htm") == 0)) {
    return "text/html\n\n";
  }
  else {
    printf(
        "Can't determine content type; received %s as file extension instead.",
        file_extension);
    return "undetermined";
  }
}

int sock_from_client(int sock_file_descriptor)
{
  char *data_to_client;
  char *method_type, *filename;
  char buffer[1024] = {0};
  read(sock_file_descriptor, buffer, 1024);

  char *get_first_line      = strtok(buffer, "\n");
  char *get_words_from_line = strtok(get_first_line, " ");

  // get method type & file name from first line of buffer
  // ex) "GET" type and "/index.htm" filename in GET /index.htm HTTP/1.1
  for (int i = 0; i < 2; i++) {
    if (i == 0) {
      method_type = get_words_from_line;
    }
    else { // redirect to index_filename if no filename is given
      filename = (strcmp(get_words_from_line, "/") == 0) ? index_filename
                                                         : get_words_from_line;
    }
    get_words_from_line = strtok(NULL, " "); // sets pointer to next "word"
  }

  strcat(root_directory, filename);
  printf("Searching for \"%s\" file: ", root_directory);
  if (access(root_directory, F_OK) < 0) {
    printf("does NOT exist.\n");

    data_to_client = "HTTP/1.1 404 Not Found\r\n"
                     "Content-Type: text/plain\n"
                     "Connection: close\n\n"
                     "HTTP 404 - File not found";
    send(sock_file_descriptor, data_to_client, strlen(data_to_client), 0);
  }
  else {
    printf("exists.\n");

    // char header[] = "HTTP/1.1 200 OK\r\n"
                    // "Content-Type: text/html\n\n";

    char header[128];
    char content_type[64];
    strcpy(header, "HTTP/1.1 200 OK\r\n"
                   "Content-Type: ");
    strcpy(content_type, get_content_type(filename));
    strcat(header, content_type);

    printf("\n\n\nHEADER: %s; CONTENT_TYPE: %s\n\n\n", header, content_type);

    // get file's size
    FILE *file = fopen(root_directory, "rb");
    fseek(file, 0, SEEK_END);
    long fsize = ftell(file);
    fseek(file, 0, SEEK_SET); /* same as rewind(file); */

    write(sock_file_descriptor, header, sizeof(header) - 1);
    int fd = open(root_directory, O_RDONLY); // fd = file descriptor
    sendfile(sock_file_descriptor, fd, NULL, fsize + sizeof(header) - 1);
    close(fd);
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

  return 0;
}