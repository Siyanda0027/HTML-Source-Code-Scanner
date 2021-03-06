# HTML-Source-Code-Scanner
A java application that automatically checks a website's HTML source code for changes and alerts the user when changes have occurred.

<img src="http://imgur.com/UoyYXEw.png" title="Version 1.0 Screenshot" width="500" height="700">

## Example Use Cases
* Joe recently attended a try-out for a travel sports team. He want to be alerted when the team website is updated with the new roster.
* As a software QA tester, Sallie wants to make sure that only certain pages are changed when new code is released.
*  Bill is a huge FatBurger fan. He recently heard that a FatBurger will be opening in a local shopping center. On FatBurger's website, the page for the new location says "Coming Soon!". Bill wants to be alerted when that website for the new location changes.

## Features:
* The user can define a website to track and a scanning update interval (including indefinite scans).
* The user is provided with an activity log that displays the current progress of the application, as well as timestamps for each record creation.
* Timestamps are automatically set in relation the user's current timezone.
* When a source code change is detected, a source code change report is generated and saved to the user's desktop.

## How To Run The Application
* Download the current JAR file included in this repository.
* Save the file to a known directory.
* Open the command line.
* Make the directory in which you saved the file the current directory.
<br>(For example, in Windows, enter this: "cd C:/Users/\<Your windows user profile name\>/Desktop/", if you saved it on your Desktop).
* Run the application via the following command: "java -jar VersionX.X.jar" (replace "X.X" with the version that you want to run).

## Intended Improvements:
* Polish the user experience.
* Allow the user to filter out certain HTML code changes from registering as a change.
* Build a UI or enhance the application for command-line execution.
* Allow the user to create an alert notification (email/text message).
* Support parameterized execution (this allows for the user to run the application via an automation server i.e. Jenkins)

### Disclaimer:
Only to be used on your own websites, or websites in which you have permission to monitor the HTML code. Libraries used within this work are subject to the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) and [The MIT License](https://jsoup.org/license). Additionally, please abide by [the license of this work.](https://github.com/ZacharyDavidSaunders/HTML-Source-Code-Scanner/blob/master/LICENSE) Please review the aforementioned licenses before using, distributing, reproducing, or modifying this work.
