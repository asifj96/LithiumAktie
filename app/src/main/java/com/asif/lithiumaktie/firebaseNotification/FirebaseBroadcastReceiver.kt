package com.asif.lithiumaktie.firebaseNotification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FirebaseBroadcastReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.e("onMessageReceived:: $message")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Timber.e("onNewToken:: $token")


    }
    /*  override fun onReceive(context: Context?, intent: Intent?) {

          context?.let {

              val dataBundle = intent!!.extras
              if (dataBundle != null) {
                  if (dataBundle.getString("body") != null &&
                      dataBundle.getString("body")!!.isNotEmpty()
                  ) {
                      Timber.e("onReceive:: $dataBundle")
                  }
              }
          }
      }*/
}



