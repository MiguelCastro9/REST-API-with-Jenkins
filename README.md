## Overview

An application developed using <strong>Jenkins</strong> to automate CI/CD.

## Execution

Tools:
<li> Java 17 </li>
<li> SpringBoot 3.2.0 </li>
<li> Jenkins </li>
<li> Docker </li>
<li> Tomcat 10.1.16 </li>
<li> MongoDB </li>
<li> TDD </li>
<br>
<strong>Back-end: </strong>
To test the developed application, you need to have Java, Maven, Docker, Tomcat, Git and Jenkins installed on your machine.
The machine on which the project was developed was MAC OS X, therefore, the steps below were carried out in <strong>MAC OS X</strong>:
<br><br>
<strong>1 -</strong> Open the terminal in the project root folder and run the mvn clean install command to build the application.
<br>
<strong>2 -</strong> In the same terminal, run the docker-compose up -d command to build the MongoDB docker image
<br>
<strong>3 -</strong> Then run the brew services start jenkins-lts command to start Jenkins
<br>
<strong>4 -</strong> Open a new terminal to start the tomcat server with the catalina run command
<br><br>
Now I will show the Jenkins configurations so that the application runs successfully in the CI/CD process.
<br><br>
Install <code>git plugin</code>
<i><h6>control panel >> manage jenkins >> plugins</h6></i>
<hr>

![image](https://github.com/MiguelCastro9/REST-API-with-Jenkins/assets/56695817/9f4d4533-5034-44ba-a802-79b15b69d5fa)

Config <code>JDK</code>
<i><h6>control panel >> manage jenkins >> tools</h6></i>
<hr>

![image](https://github.com/MiguelCastro9/REST-API-with-Jenkins/assets/56695817/bc12a681-d596-457b-8421-4ec4ec666b8d)

Config <code>Maven</code>
<i><h6>control panel >> manage jenkins >> tools</h6></i>
<hr>

![image](https://github.com/MiguelCastro9/REST-API-with-Jenkins/assets/56695817/665e6de1-e79d-4856-93f9-86027dbae8c4)

## Demo
