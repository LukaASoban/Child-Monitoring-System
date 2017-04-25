# Child-Monitoring-System
Android Application to pair with the raspberry pi system

## Release Notes
Version 1.0

### New software features for this release: 
- All users can now see contact information in the drawer menu.
- All users can now change their profile information
- All users will now be able to recieve administrator messages from their respective daycare facility.
- All users are now allowed to see the map of their assigned daycares. This will allow viewing of all the children and which room they are located in.

#### Parents
- Parents can now report bugs or complaints to the dev team under the menu.
- Parents will now be notified when their child is not in the building or have left specific areas.
- Parents can now recieve customized notes from their child's daycare instructors about their child's day.
- 

#### Employees
- Daycare employees can now report bugs or suggestions to the dev team.
- Employees are now given an interface for taking attendence and building their classes.
- Employees are now given the first alert as to when a child is missing or not in the correct room.

#### Administrators
- Administrators can enroll children into the system under the parent's account.
- Admins can authorize new bracelets to the system.
- Admins can give give or revoke access to other user accounts.
- Admins can now search for any parent or employee in the system on the search page.


Bug fixes made since the last release: No bug fixes.

Known bugs and defects:
- Time based location notifications to parents, and the ability to customize the frequency of the notifications, was not implemented.
- Two users cannot log into the system at the same time.
- The map page sometimes does not load properly, so the user has to go back to the previous page and try to access the map page again.


## Install Guide
Version 1.0

Pre-requisites:
- Target android build is v7 API level 25, minimum 16.
- Need bluetooth low-energy tracking bracelet(s).
- Raspberry Pi with bluetooth capabilities.

Dependencies: None.

Download instructions: Download the apk file by using the link below on an android mobile device.
https://github.com/LukaASoban/Child-Monitoring-System/blob/master/app/app-release.apk

Build instructions: None.

Installation of actual application: Go to the apk link on your Android device, and click download. Once the app has finished downloading, open it on your phone and follow the instructions. (When installing on an Android device you must allow installing from unknown sources. This option is located in the security settings, but your phone should prompt you to enable it when you try to install.)

Run instructions: Once installed, run as you would any other app.

Troubleshooting: If the map screen does not load properly the first time you go to it, just go to a different screen first, and when you go back the second time it should work.

## Raspberry Pi System Install Guide
Version 1.0

Pre-requisites:
-Raspberry Pi or equivalent system with Python preloaded

Dependencies:
-Pyrebase
-Bluepy

In a terminal you can use pip to install these. Run sudo pip3 install pyrebase and sudo pip3 install bluepy.

Download instructions: Download the folder named Raspberry Pi at https://github.com/LukaASoban/Child-Monitoring-System/blob/master/

Once this is done, place the folder in the home directory of your Raspberry Pi.

Build instructions: No build is required.

Installation: Once you have placed the folder in your root directory, that is it - there is no install necessary. 

Run instructions: Open up a terminal and move to the Raspberry Pi folder. From there, you can run the btLoc.py
    program by running the command $ sudo python3 btLoc.py. SUDO is required for the bluetooth location.
    
Troubleshooting: There is currently no need for troubleshooting as the script has no known bugs or issues. If anything, you can always cancel and re-run the program.
