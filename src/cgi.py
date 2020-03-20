import cgi

print("Content-type:text/html\r\n\r\n")
print("<html>")
print("<head><title>Testing 1 2 3...</title></head>")
print("<body>")
print("<p>it works!</p>")

for i in range(5):
  print("<h1>hello world!</h1>")

print("</body>")
print("</html>")