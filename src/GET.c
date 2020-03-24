#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

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
    return "undetermined";
  }
}

int main(void)
{
  char *data, path[2048];

  data = getenv("QUERY_STRING");
  strcpy(path, data);

  if (access(data, F_OK) < 0) {
    printf("HTTP/1.0 404 Not Found\r\n"
           "Content-Type: text/plain\n\n"
           "HTTP 404 - File not found");
    return -1;
  }

  char header[64], time_buffer[256];
  char *content_type = get_content_type(data);
  time_t now         = time(0);
  struct tm tm       = *gmtime(&now);
  strftime(time_buffer, sizeof(time_buffer), "%a, %d %b %Y %H:%M:%S %Z", &tm);

  FILE *filePointer = fopen(path, "rb"); // open existing binary picture
  unsigned long file_length;

  fseek(filePointer, 0, SEEK_END);
  file_length = ftell(filePointer); // get the exact size of the file

  printf("HTTP/1.0 200 OK\n");
  printf("Content-Type: %s\n", content_type);
  printf("Content-Length: %li\n", file_length);
  printf("Connection: Keep-Alive\n");
  printf("Last-Modified: Mon, 23 Mar 2020 02:49:28 GMT\n");
  printf("Expires: Sun, 17 Jan 2038 19:14:07 GMT\n");
  printf("Date: %s\n\n", time_buffer);

  fseek(filePointer, 0, SEEK_SET);

  for (unsigned long i = 0; i < file_length; i++)
    printf("%c", fgetc(filePointer)); // read each byte and print it out

  fclose(filePointer);
  return 0;
}