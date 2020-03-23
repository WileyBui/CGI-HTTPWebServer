#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void)
{
  char *data;
  printf("HTTP/1.1 200 OK\r\n");
  printf("Content-Type:text/html\n\n");

  data = getenv("QUERY_STRING");
  printf("QUERY_STRING : %s\n", data);

  char *line = strtok(data, "&");
  printf("%s\n", line);

  //while(line != NULL) {
  //  printf("%s\n", line);
  //  line = strtok(NULL, "&");
  //}

  // printf("<html>\n");
  // printf("<body>\n");
  // printf("<h1>%s + %s = %s</h1>\n", "hi", "hey", "hihey");
  // printf("</body>\n");
  // printf("</html>\n");
  
  return 0;
}