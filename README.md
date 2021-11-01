# CoviCheck
An android application that collects COVID-19 related symptoms and stores them in a database in the smartphone.

It has two screens. The first screen should present the user with two sign measurement technique: a) heart rate sensing, and b) respiratory rate sensing. We will use the following methods for each sensing operation.

Heart rate sensing:
For heart rate sensing we will utilize the back camera of the smartphone with flash enabled. We will take 45 s video from the back camera with the flash on. While taking the video the user should softly press their index finger on the camera lens while covering the flash light. From the variation of the red coloration in the image we will derive the heart rate of the subject.

Respiratory rate sensing:
For respiratory rate sensing we will utilize the accelerometer or orientation sensor of the smartphone. The user will be asked to lay down and place the smartphone on their chest for a period of 45 seconds. The respiratory rate will be computed from the accelerometer or orientation data.

The user will be asked to click on the measure heart rate button and measure respiratory rate button to collect data from the smartphone sensors. The numbers will be stored in a database corresponding to the user. In this assignment we will assume only a single user. The user will then hit upload signs button which will create a database with the user’s lastname in the smartphone. The entry of the database will be a table with the first two columns heart rate and respiratory rate respectively. Each entry of the database will have 10 additional columns which will be filled in the next screen.

Once the user is done collecting signs data, the user will be taken to the second screen to collect symptoms data. The user will select a symptom and then select a rating out of 5. The user does not need to select all the symptoms. Whichever symptoms the user has not reported will be marked with 0 rating. After this the user will click a upload symptoms button. At this point a database table entry with 12 entries will be created and stored in the database in the smartphone.

Project demo : https://youtu.be/dYIH5S1067g
