#include <stdio.h>
#include <stdlib.h>
int main(void)
{
  char *data;
  printf("%s%c%c\r\n", "Content-Type:text/html", 13, 10);
  printf("<!DOCTYPE html><html>\n");
  printf("<HEAD><TITLE>Multiplication results</TITLE></HEAD>\n");
  printf("<BODY><H3>Multiplication results</H3>\n");
  data = getenv("QUERY_STRING");
  if (data == NULL)
    printf("<P>Error! Error in passing data from form to script. %s\n", data);
  else
    printf("<P>Got data: %s.</P>", data);
  printf("</BODY></HTML>\n");
  return 0;
}