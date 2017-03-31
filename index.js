var functions = require('firebase-functions');
const admin = require('firebase-admin');
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