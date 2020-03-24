#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void)
{
  char *data, *first_line, *second_line, *fl_selector, *sl_selector,
      *first_word, *second_word, combined_words[2048];
  printf("HTTP/1.0 200 OK\r\n");
  printf("Content-Type:text/html\n\n");

  data = getenv("QUERY_STRING");

  char *entry_selector = strtok(data, "&");
  first_line           = entry_selector;

  entry_selector = strtok(NULL, "&");
  second_line    = entry_selector;

  fl_selector = strtok(first_line, "=");
  fl_selector = strtok(NULL, "=");
  first_word  = fl_selector;

  sl_selector = strtok(second_line, "=");
  sl_selector = strtok(NULL, "=");
  second_word = sl_selector;

  strcpy(combined_words, first_word);
  strcat(combined_words, " ");
  strcat(combined_words, second_word);

  printf("<HTML>");
  printf("<H1>POST: Add two words together!</H1>");
  printf("<FORM ACTION=http://localhost:8080/test.cgi METHOD=POST>");
  printf("First Word: <INPUT NAME=First Word>");
  printf("Second word: <INPUT NAME=Second word>");
  printf("<INPUT TYPE=SUBMIT NAME=GO>");
  printf("</FORM>");
  printf("<H2>%s + %s = %s</H2>", first_word, second_word, combined_words);
  printf("</HTML>");
  return 0;
}