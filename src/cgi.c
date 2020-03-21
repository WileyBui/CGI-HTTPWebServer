#include <stdio.h>

void main(int argc, char *argv[])
{
  printf("Content-type: text/html\n\n");
  printf("<html>");
  printf("<head>");
  printf("<title>Test</title>");
  printf("</head>");
  printf("<body>");
  printf("<p>Hello world!</p>");
  printf("</body>");
  printf("</html>");
  return;
}