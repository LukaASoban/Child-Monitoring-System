var functions = require('firebase-functions');

const admin = require('firebase-admin');
//var mes

admin.initializeApp(functions.config().firebase);


exports.sendNotification = functions.database.ref('/messages/{mesgID}').onWrite(event => {
  const mesgID = event.params.mesgID;
  if (!event.data.val()) {
    return console.log('No message added');
  }

  const getDeviceTokensPromise = admin.database().ref(`/messages/${mesgID}/tokens`).once('value');
  const messageTitlePromise = admin.database().ref(`/messages/${mesgID}/messageTitle`).once('value');
  const messageBodyPromise = admin.database().ref(`/messages/${mesgID}/messageBody`).once('value');

  return Promise.all([getDeviceTokensPromise, messageTitlePromise, messageBodyPromise]).then(results => {
    const tokensSnapshot = results[0];
    const messageTitle = results[1];
    const messageBody = results[2];

    const payload = {
      notification: {
        title: messageTitle.val(),
        body: messageBody.val(),
      }
    };

    return admin.messaging().sendToDevice(tokensSnapshot.val(), payload).then(response => {
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {

          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {

          }
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});


exports.checkForLocationUpdate = functions.database.ref('/daycare/Georgia Tech/children/')
    .onWrite(event => {
      // Grab the current value of what was written to the Realtime Database.
      var missing = [];
      var currentTimestamp = new Date();
      var count = event.data.numChildren();
      var missingcount = 0;
      event.data.forEach(function(childSnapshot) {
	      var childMac = childSnapshot.child('macAddress').val();
	      var childName = childSnapshot.child('name').val();
	      var childTimestamp = new Date(Date.parse(childSnapshot.child('timestamp').val()));
	      var childLocation = childSnapshot.child('location').val();
	      var childPresent = childSnapshot.child('present').val();
	      // childData will be the actual contents of the child
	      //macs += childMac + " ";
//	      console.log("Time Diff for: " + childMac + " is: " + ((currentTimestamp.getTime() - childTimestamp.getTime()) > 10000));
//	      console.log("childPresent: " + childPresent);
//	      console.log("childTimestamp: " + childTimestamp);
//	      console.log("childTimestamp.getTime(): " + childTimestamp.getTime());
	      currentTimestamp = new Date();
//	      console.log("currentTimestamp: " + currentTimestamp);
//          console.log("currentTimestamp.getTime(): " + currentTimestamp.getTime());
//          console.log("present and true equal? " + (new String(childPresent).valueOf() == new String("true").valueOf()));
//          console.log("TimeDiff: " + (currentTimestamp.getTime() - childTimestamp.getTime()));
	      if (((currentTimestamp.getTime() - childTimestamp.getTime()) > 10000) && (new String(childPresent).valueOf() == new String("true").valueOf())) {
	      	//child is gone for more than 10 sec, so we need to add a notification in db
	      	//var child = [childMac, childLocation, currentTimestamp, childTimestamp];
	      	missingcount++;

	        missing.push({
                             mac: childMac,
                             name: childName,
                             timestamp: childTimestamp.toString(),
                             location: childLocation
                         });
             console.log(childMac + " is missing");
	      }
	      count--;
	      if (count == 0) {
              missing.forEach(function(element) {
                    console.log("Adding to missingChildren node");
                    event.data.ref.parent.child('missingChildren').child(element.mac).set(element);;
                });
          }
        });
      
    });

exports.sendMissingNotification = functions.database.ref('/daycare/Georgia Tech/missingChildren').onWrite(event => {
  var snapshot = event.data;
  var newMissingChildren = [];
  var count = event.data.numChildren();
  event.data.forEach(function(childSnapshot) {
    if (childSnapshot.changed()) { //only send notification for new missing children
        count--;
        newMissingChildren.push({
                                     mac: childSnapshot.child('mac').val(),
                                     name: childSnapshot.child('name').val(),
                                     timestamp: childSnapshot.child('timestamp').val(),
                                     location: childSnapshot.child('location').val()
                                 });


        if (count == 0) {
            newMissingChildren.forEach(function(element) {
                findTeacher(element.mac, event.data.ref.parent.child('classrooms'), function(promise) {
                    promise.then(token => {
                        token = token.A.B
                        console.log("token found: " + token);
                        var mac = element.mac;
                        var loc = element.loc;
                        var timestamp = element.timestamp;
                        var name = element.name;

                        var title = name + " is missing";
                        var body = name + " is missing. Last location: " + loc + " at: " + timestamp;
                        var message = {
                                messageBody: body,
                                messageTitle: title,
                                tokens: [token]
                            };
                        console.log("Message: ", message);
                        var newKey = admin.database().ref('/messages/').push().key;
                        admin.database().ref('/messages/' + newKey).set(message);

                    });
                });
            });
        }
    }
  });


    function findTeacher(childMac, reference, callback) {
        console.log("Looking for: ", childMac);
        reference.once('value', function(dataSnapshot) {
            dataSnapshot.forEach(function(childSnapshot) {
                var emplID = childSnapshot.key;
                //each teacher
                console.log(emplID + " :key");
                childSnapshot.forEach(function(eachChild) {
                    console.log("Checking childMAC: " + eachChild.key);
                    if (new String(childMac).valueOf() == new String(eachChild.key).valueOf()) { //see if child exists in this teachers class
                        console.log("Match");
                        return callback(reference.root.child('users').child(emplID).child('token').once('value'));
                    }
                });
            });
        });
    }
});
