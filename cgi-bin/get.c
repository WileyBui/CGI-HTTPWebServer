#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(void)
{
  char *data, path[2048];

  data = getenv("QUERY_STRING");
  if (access(data, F_OK) < 0) {
    printf("HTTP/1.0 404 Not Found\r\n"
           "Content-Type: text/plain\n"
           "Connection: close\n\n"
           "HTTP 404 - File not found");
    return -1;
  }
  printf("HTTP/1.1 200 OK\r\n");
  printf("Content-Type:text/html\n\n");

  FILE *filePointer = fopen(data, "r");
  int bufferLength  = 255;
  char buffer[bufferLength];

  while (fgets(buffer, bufferLength, filePointer)) {
    printf("%s\n", buffer);
  }

  fclose(filePointer);
  return 0;
}