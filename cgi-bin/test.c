#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void)
{
  char *data, *first_line, *second_line, *fl_selector, *sl_selector, *first_word, *second_word;
  printf("HTTP/1.1 200 OK\r\n");
  printf("Content-Type:text/html\n\n");

  data = getenv("QUERY_STRING");

  char *entry_selector = strtok(data, "&");
  first_line = entry_selector;

  entry_selector = strtok(NULL, "&");
  second_line = entry_selector;

  fl_selector = strtok(first_line, "=");
  fl_selector = strtok(NULL, "=");
  first_word = fl_selector;

  sl_selector = strtok(second_line, "=");
  sl_selector = strtok(NULL, "=");
  second_word = sl_selector;

  //printf("First word : %s\5", first_word);
  //printf("Second word : %s\t", second_word);

  //printf("QUERY_STRING : %s\n\n", data2);
  //AT THIS POINT THE FIRST WORD TO BE CAT'D IS FOUND
  
  //entry_selector = strtok(data2, "&");
  //entry_selector = strtok(NULL, "&");
  //printf("%s", entry_selector);

  printf("<HTML>");
  printf("<H1> Add two words together!</H1>");
  printf("<FORM ACTION=http://localhost:8080/test.cgi METHOD=POST>");
  printf("First Word: <INPUT NAME=First Word>");
  printf("Second word: <INPUT NAME=Second word>");
  printf("<INPUT TYPE=SUBMIT NAME=GO>");
  printf("</FORM>");
  printf("<H2>%s + %s = %s</H2>", first_word, second_word, strcat(first_word, second_word));
  printf("</HTML>");
  return 0;
}