#include <stdio.h>
#include <stdlib.h>
int main(void)
{
  char *data;
  printf("HTTP/1.1 200 OK\r\n");
  printf("Content-Type:text/html\n\n");

  printf("<html>\n");
  printf("<body>\n");
  printf("<h1>Hello there!</h1>\n");
  printf("</body>\n");
  printf("</html>\n");

  //printf("<!DOCTYPE html><html>");
  //printf("<HEAD><TITLE>Multiplication results</TITLE></HEAD>\n");
  //printf("<BODY><H3>Multiplication results</H3>\n");

  //data = getenv("QUERY_STRING");
  //data = "LOGIN=hi&PASSWORD=hey&GO=Submit";

  //if (data == NULL)
  //  printf("<P>Error! Error in passing data from form to script. %s\n", data);
  //else
  //  printf("<P>Got data: %s.</P>", data);
  //printf("</BODY></HTML>\n");
  return 0;
}