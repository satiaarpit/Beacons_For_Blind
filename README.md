# Beacons For Blind


## *A supermarket navigation system for visually impaired people*



####What is it : 

I have developed a supermarket navigation Android app that uses Beacons to help the visually impaired people navigate through a
supermarket freely via simple voice commands. I have used Estimote Beacons that uses Bluetooth Low Energy protocol
to broadcast signals continuously over a radius of 40 meters.



####Motivation : 
Visually impaired people struggle a lot while they are navigating in the supermarket. To tackle the challenge of independent travel for visually impaired people, our app is setting the standard for audio navigation using your smartphone.

####Application Usage :

Consider a hypothetical supermarket with four sections. Each section is installed with a specific beacon. When a visually impaired 
customer arrives at the supermarket, this application will guide him through the available sections in the supermarket using voice 
commands. The customer can use gestures to select a particular section. The mobile application will sense the beacons in the supermarket 
creating an optimal route for the customer to reach the selected section. It will also navigate him/her using the intermediate beacons.

Beacons for blind is an Android application that can be used by visually impaired people to navigate 
freely in a supermarket and shop independently. It uses Beacons – a class of Bluetooth low energy devices 
that broadcast their identifier to nearby portable electronic devices. 



####Technical Description :

Beacons – a class of Bluetooth low energy devices that broadcast their identifier to nearby portable electronic devices. It works on the protocol iBeacons developed by Apple Inc. in  2013. With iBeacon, Apple has standardized the format of Bluetooth Low Energy advertising packet. It consists of three main pieces of information :
	1. UDID : It is a 16 byte string used to differentiate a large group of related beacons. 
	2. Major : It is 2 byte string used to distinguish a smaller subset of beacons within a larger group.
	3. Minor : It is 2 byte string used to identify individual beacons.
This application uses the above beacon information to identify individual beacons.

#### Demonstration :
Visit the following link : (https://www.youtube.com/watch?v=me5m9VgSLes&feature=youtu.be)

####Challenges :
*Uniquely identify a beacon
*Beacon range entry and exit detection
*Location Triangulation

