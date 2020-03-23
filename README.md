# INET4021 Lab 1

### Authors
- Wiley Bui - 5368469 - buixx206@umn.edu
- Gus Eggers - 5220957 - egger235@umn.edu

### Project Goals
The main goal of this lab is to create a HTTP web server based on RFC/1945 (see Communication and Tasks). The server is built on C socket programming and abstraction learned so far in class. 

Additionally, the server should be efficient in regards to speed and resource allocation. 

### Content Description
/cgi-bin - This contains our CGI files used to handle POST and GET requests (where the executable webserver resides).

When a client sends a POST/GET request to the server, the server will call the CGI executable to handle the request. The main use for this is to allow the executable to handle the work, while the server just passes it off and then can handle new requests. This is a good use of resource allocation and helps support more users concurrently.

/conf - The config folder contains one .conf file which is a configuration file for the server. It states the amount of concurrent users, root content location, index location, and port to be run on.

/contents - All of the .html, .gif, .jgp (contents) reside in this folder. When the index.html is received, it sends requests for all these files. These files are used to make the webpage that is displayed on the client's browser. 

/logs - This folder contains the log files for all access commands as well as errors.

/src - This is where all of our source code resides. This is essentially the folder where the server is located.

### Set-up
1. Clone the repository: `https://github.com/WileyBui/INET4021Labs.git`

2. Create your own branch: `git checkout -b CHANGEYOURBRANCHNAMEHERE`
    - The purpose of this is to work and test on __*your own code/branch*__ as well as not to have too many conflicts when you're pushing the code when working with multiple people.
    - Push your code to GitHub like before: `git add . && git commit "YOURMESSAGEHERE" && git push`
	
3. If everything works correctly, we can update & push it to the master branch for anyone to use.
    - `git checkout master`
    - `git merge CHANGEYOURBRANCHNAMEHERE`
    - `git push`
    - Notice, merge conflicts may arise when 2 people are working on the same file.
	
4. The master branch should always have the latest working version
- To see which branch you're on, type `git branch`
- If you're on the master branch, continue to work on your branch by switching it from the master branch to your branch
    - `git checkout CHANGEYOURBRANCHNAMEHERE`
- If you want to see other branches or see if your code has pushed through your branch, click the `Branch: master` button to see the list of branches.

  ![Image](https://i.snipboard.io/sP0yGQ.jpg)
  
5. To test the server, change to the /src directory.

### Communication and Tasks
- [See here](https://docs.google.com/document/d/1b5y2U4AuAkZoI1Iazu_hnQO1CJy929CKRletR1ds0Uc/edit).