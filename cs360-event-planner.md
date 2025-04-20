## Event Planner / Daily Planner App - Android Mobile Application<br/>CS 360 Mobile Architecture and Programming<br/>Software Design and Engineering Category<br/>Algorithms and Data Structure Category
<p>
    <img alt="Static Badge" src="https://img.shields.io/badge/Language-JAVA-11FF93?style=flat">
    <img alt="Static Badge" src="https://img.shields.io/badge/Tool-Android%20Studio-1193FF?style=flat">
</p>
<p>    
    <a href="https://github.com/klockhart99/ePortfolio/tree/main/CS 360 Before Enhancement" title="CS 340 Before"><img 
              alt="Static Badge"
              src="https://img.shields.io/badge/Source Code-Before-FF2211?style=flat"></a>
    <a href="https://github.com/klockhart99/ePortfolio/tree/main/CS 360 After Enhancement" title="CS 340 After"><img 
              alt="Static Badge"
              src="https://img.shields.io/badge/Source Code-After-2211FF?style=flat"></a>
    <a href="https://github.com/klockhart99/ePortfolio/blob/main/Archive/Documents/Lockhart%2C%20Kenneth%20-%20CS%20499%20Milestone%20Two(1).docx" title="CS 340 Narriative"><img 
              alt="Static Badge"
              src="https://img.shields.io/badge/Document-First%20Narrative-21DD12?style=flat"></a>
    <a href="https://github.com/klockhart99/ePortfolio/blob/main/Archive/Documents/Lockhart%2C%20Kenneth%20-%20CS%20499%20Milestone%20Three(1).docx" title="CS 340 Narriative"><img 
              alt="Static Badge"
              src="https://img.shields.io/badge/Document-Second%20Narrative-21DD12?style=flat"></a>
</p>

## Overview
In order to understand the effects of this enhancement it is important to look at the before and after of the project. By doing so we can make comparisons between the two to determine if the enhancement achieves its goals or if it still has more work to be done. In this project I focused on two main areas of improvement that being the Software Design and Engineering as well as the Algorithms and Data Structure of the application. The goal of the improvement was to improve the application through these two means to provide a product that actually felt finished.

### Before Enhancement
Let’s start by looking at what the application offered is before we take a look at the finished applications. This artifact was designed for CS 360 as an event planner, and it required basic handling and displaying of upcoming, past and future events. These events had to also be protected via a user management system as well.
Starting off with the login screen which would allow the user to sign in or register a new account with the application. The login screen was connected to a login database which was used to verify the user and to ensure that the email wasn’t already in use. The login screen can be seen in figure 1 below.

<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Login Screen.png" width="300px" title="Event Planner Login Screen"/>
    <p><em>Figure 1 - Login Screen</em></p>
</div>

Outside of that the log in screen serves to hide any data from the user until they signed in. Once a successful login occurs the user will be brought to the home screen which consists of two fragments the upcoming events fragment and the calendar fragment as shown in figures 2 and 3.

<div style="text-align: center;">
        <img src="assets/imgs/CS 360/Upcoming Events.png" width="300px" title="Event Planner Upcoming Events Screen" />
        <img src="assets/imgs/CS 360/Calandar View.png" width="300px" title="Event Planner Calandar View" />
        <p><em>Figure 2 - Upcoming Events and Figure 3 - Calendar View</em></p>
</div>


These fragments allowed the user to manage past, upcoming, and current events with ease by displaying lists of events. If the user had no events, then the lists would be empty until they created one. From the home screen the user could press the “+” button to add an event via the add event screen seen in figure 4.

<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Event Screen.png" width="300px" title="Event Planner Event Add Screen" />
    <p><em>Figure 4 - Event Add Screen</em></p>
</div>

Aside from this the left side bar allowed the user to access settings and give SMS permission for push notification for events or to log out. With that the full functionality of the application has been described. 
Overall, the application does its purpose but lacks some features that would be expected. Likewise, features such as end time and start time were practically useless outside of push notifications or displaying the event more accurately. In other words, it lacked a visual application to the product. Yet it did meet the requirements for a minimum viable product and as such on 12/13/2024 was submitted as complete.






### After Enhancement
However, I could not just leave the application as a minimum viable product it needs to be enhanced and to do that, I need to target two parts of the application, the Software Design and Engineering as well as the Algorithms and Data Structure of the application. First off, lets discuss design and engineering. The home screen felt empty with just two fragments so I wanted to add a third fragment. Additionally, it had features that were not well used so I want to encompass them into a new fragment. As such I created the Daily Planner fragment to display up two simultaneous events side by side in a block format that allow for visualization of start and end times. An example of this is shown in figure 5 below.

<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Daily Planner.png" width="300px" title="Event Planner Daily Planner" />
    <p><em>Figure 5 - Daily Planner View</em></p>
</div>

Likewise, Algorithms and Data Structures made the daily event planner functional. For example, the daily planner made use of the data structures that had next to no purpose in the application, the start and end times. However, in order to use these features of the data structures and to achieve the visual aspect of the application an algorithm needs to be created. The algorithm allows us to pull the correct event data from the database and then turn that information into custom views to be displayed to the user.
Overall, this improvement greatly improves the application in both categories, allowing the application to feel much more complete. The full narrative on this application is left below and contains more details about the journey to finishing the application.

## Narrative
### Purpose
I have chosen for both Software Design and Engineering and Algorithms and Data Structure to focus on an artifact from CS 360. This artifact from CS 360, as describe above, is an android application that was designed to be an Event Planner. This Event Planner was coded in Android Studio using Java as the coding language. The Event Planner had basic functionality in the form of being able to create, edit, and delete events as well as display, reminded, and store those events. In order to complete such a task, the program made use of two databases, one for users and one for events. These databases were then used to complete the as for mentioned tasks. This project was started at the tail end of 2024 and reached its minimal viable product on 12/13/2024 with a few cut features that were not required for submition.

The primary reason for choosing this artifact for my ePortfolio was to continue the original goal of the project. As stated during the process of developing the application had a few features cut to ensure that the minimal viable product could be delivered. As a result, I want to continue the design and engineering of the application to bring the project to a better closure point. In order to continue the design and engineering of the application, I had to expand the use of the current data structure and design and algorithm to present that data structure in a good format for the end user. Additionally, I wanted to gain additional practice in an unfamiliar environment of mobile development. I was able to through the creation of the daily planner portion of this artifact demonstrating my familiar skills of Java and stretch my knowledge mobile development. In order to complete the development of the daily planner fragment I had to completely create a new algorithm to display events by expanding the ability of the existing data structure. This improvement not only greatly improves the functionality of the end product, but it also brings the application to a much better closure and feeling of completeness. When I looked at the application before I always felt it was lacking and felt very unfinished. Now I look at with a different opinion, I can see a full functional product that is read for actual use.  

### Software Design and Engineering Outcomes
The primary outcome I set out to achieve with this enhancement was to demonstrate my ability to use my techniques, skills, and tools to deliver value and accomplished industry-specific goals. This outcome was by far the most important to this, as stated I felt that the application did not reach those goals when it was submitted in December. Now I feel like it finally reached that goal, and it was done through the use of well-found and innovative techniques, skills, and tools. However, it was not the only outcome reach, I had the opportunity to correct a few minor details within the code during process. During the process I design, develop, and deliver clear visual improvement to the daily planner which will allow it to be easily understood while being technically sound for different audiences or context. An example of this was making the format of the daily planner easy to read, it follows the current time zone no matter where you are, and it informs the user of overlapping events. Additionally, during the review of the code I noticed many details that could be adjusted to better improve the application, specifically in the name of security. Some variables were left accessible to other classes, or as a field when they could have been more locked down. I went through and fixed though variables to help ensure that those fields do not get exploited. 

Overall, I was surprised that despite focusing on one outcome just how much I was able to relate parts of it to other outcomes and while it might not be easy to pick out all the details, I can say a majority of the outcome can apply in some capacity to the artifact.

### Software Design and Engineering Enhancement
In summary of this enhancement and modification of the artifact there are several things I had to face and learn. First, it is important to note that I have a decent background of Java experience with little to no mobile development practice. This means that while I understand the language itself and how it functions, I struggle with understanding it in a mobile development mindset. The very first challenge is that not a whole lot of people have made a daily planner from scratch. This limited my ability to research ideas as all I got was to use some library someone else created. The biggest challenge was simply how do I make it look like a daily planner and how to make it updatable, scrollable, and editable. In the context of the Calendar view and Upcoming Event view I could use a recycler view to create a list of events. At first, this sounds like a great idea until I remember one key thing about a daily planner, events aren’t in a list they are spaced out and resized based on when they start and how long the event lasts. With practically no mobile development and only Java the thought of how to do in a JFrame was to just draw it overtop of the list came to mind. This ultimately leads me to my solution and how to research this topic, rather than look for a super specific example as a daily planner I looked into overlaying graphics. I learned that the containers can be accessed almost like a list and that you can set the locations of objects inside those containers. Basically, these containers worked similar to JPanels which set the whole project and enhancement forward. From that point it was about getting the right constraints on everything and generating events correctly to fit in with the view. However, one more challenge needs to be tackled before I can call the project finished and that is design. With events being drawn in two lists, allowing for two events to overlap and be displayed came the issue of making it look better then a handful of lines and boxes. I opted to overhaul them to improve the design and wrote custom XML designed backgrounds to improve the visuals. It took quite a bit of time to understand exactly how the XML designs worked and to be honest I overthought the details too much. As a result, I choose to opt for a simple but good looking style. 

Overall, in the end I think the improvements came out very well given the limited time spent on it. I believe it really shows how far I have come in mobile development since December 2024 and shows how I can overcome challenges and improve on Software Engineering and Design.

### Algorithms and Data Structure Outcomes
The primary outcome I set out to achieve with this enhancement was to demonstrate my ability to use my techniques, skills, and tools to deliver value and accomplished industry-specific goals. This outcome was by far the most important to this, as stated I felt that the application did not reach those goals when it was submitted in December. Now I feel like it finally reached that goal, and it was done through the use of well-found and innovative techniques, skills, and tools. However, it was not the only outcome reach, I had the opportunity to correct a few minor details within the code during process. During the process I design, develop, and deliver clear visual improvement to the daily planner which will allow it to be easily understood while being technically sound for different audiences or context. An example of this was making the format of the daily planner easy to read, it follows the current time zone no matter where you are, and it informs the user of overlapping events. Additionally, during the review of the code I noticed many details that could be adjusted to better improve the application, specifically in the name of security. Some variables were left accessible to other classes, or as a field when they could have been more locked down. I went through and fixed though variables to help ensure that those fields do not get exploited. 

Overall, I was surprised that despite focusing on one outcome just how much I was able to relate parts of it to other outcomes and while it might not be easy to pick out all the details, I can say a majority of the outcome can apply in some capacity to the artifact.

### Algorithms and Data Structure Enhancement
In summary of this enhancement and modification of the artifact there are several things I had to face and learn. As mentioned before, it is important to know that I have a decent background with Java but little to no mobile development practice outside of this project. This means that while I understand the language itself and how it functions, I struggle with understanding it in a mobile development mindset. The major challenge is that not a whole lot of people have made a daily planner from scratch. This limited my ability to research ideas as all I got was to use some library someone else created. Previously, I had mentioned this as part of my biggest challenge in design and engineering of the application which was to make it look like a daily planner and function like one.

While this is still the same for this enhancement, I saved talking about the algorithm that was needed to generate the view and overlay graphics for this category. First off, in order to optimize the algorithm, I only have it act on data that should be displayed rather than running it on all data. Like the calendar and upcoming events view I had a separate function store the events into arrays of data if they are relevant to the user and if the event time is part of the current date selected. Combining the use of the database and arrays allows for a quick way to not only clear data when needed but to also iterate over that data without querying the database repeatedly. With the data prepared we can then clear the views and lockout list to run the algorithm on the acquired data structure from the database. Starting by formatting the date into two categories, time and date. These are then used to determine 4 important flags for the algorithm so it can correctly generate time blocks. They are if it is the start day the selected, is a day after the start day, is a day before the end day, and lastly is the end day the current day. If the start day was the current day, we need to set the start of the block from 0 to current hour from the start time. If the end day is the current day, then we need to set the end block to the end hours instead of 24. In the event that neither is true given previous preparation the event should span the whole day but as a double check the algorithm verifies this by checking if both after start and before end flags are true and setting the start to 0 and end to 24. Finally with the start and end times we can figure out the difference which is used for the size of the block. From here the algorithm just scales the blocks based on the display and size between hours to get the positions. This portion of the code was the most difficult part of the process of developing this application as it required insuring that all the numbers lined up, the elimination of daylight-saving factors, time zone factors, and display scaling. 

<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Code 1.png" width="600px" title="Event Planner Example 1" />
</div>
<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Code 2.png" width="600px" title="Event Planner Example 2" />
</div>
<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Code 3.png" width="600px" title="Event Planner Example 3" />
</div>
<div style="text-align: center;">
    <img src="assets/imgs/CS 360/Code 4.png" width="600px" title="Event Planner Example 4" />
    <p><em>Algorithm Code Example</em></p>
</div>

One more part of the process of was adding the ability to display two events, which was an idea from the beginning and was rather simple. By adding a data structure in the form of two array list working as a lockout list by adding the hours consumed by the block and by multiplying the floats by 100, I was ability to make it place event very close to each other in a single list before moving to the second list. Ultimately, I know that the algorithm can still be enhanced and optimized as I did not add a feature to allow overlapping lists or more than two lists. However, given the limited details on this topic on the internet I was rather impressed with the solution I found. The biggest improvement I can see to this algorithm is in the multievent view as it takes time to perform lockouts while minor, I could see having a lot of events slowing down the process. Ideally, I would like to reduce the amount of nested loops to help produce a better algorithm but that felt outside the scope of this enhancement and would take far longer to design from scratch then this project has available. 

Overall, in the end I think the improvements came out very well given the limited time spent on it. I believe it really shows how far I have come in mobile development since December 2024 and shows how I can overcome challenges to design algorithm and data structures to solve problems. Additionally, it shows that I am always improving and thinking critically about my work.
