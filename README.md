# HTML-Source-Code-Scanner
A java application that automatically checks a website's HTML source code for changes and alerts the user when changes have occurred.

## Example Use Cases
* Joe recently attended a try-out for a travel sports team. He want to be alerted when the team website is posts an update to the roster.
* As a software QA tester, Sallie wants to make sure that only certain pages are changed when new code is released.
*  Bill is a huge FatBurger fan. He recently heard that a FatBurger will be opening in a local shopping center. On FatBurger's website, the page for the new location says "Coming Soon!". Bill wants to be alerted when that website for the new location changes.

<img src="http://imgur.com/UoyYXEw.png" title="Version 1.0 Screenshot" width="500" height="700">

## How To Run The Application
* Download the JAR file included in the repository.
* Save the file to a known directory.
* Open the command line.
* Make the directory in which you saved the file the current directory.
<br>(For example, in Windows, enter this: "cd C:/Users/\<Your windows user profile name\>/Desktop/", if you saved it on your Desktop).
* Run the application via the following command: "java -jar VersionX.Xjar" (replace "X.X" with the version that you want to run).

##Current Functionality:
* The user can define a website to track and a scanning update interval (including indefinite scans).
* The user is provided with an activity log that displays the current progress of the application.

##Intended Improvements:
* Polish the user experience
* Allow the user to filter out certain HTML code changes from registering as a change.
* Generate a "diff" when changes are found.
* Record the timestamp for each record creation and record comparisons.
* Build a UI or enhance the application for command-line execution.

### Disclaimer:
Only to be used on your own websites, or websites in which you have permission to monitor the HTML code.

Please abide by the license.
