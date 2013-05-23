import com.dukascopy.api.Instrument
import com.dukascopy.api.system.IClient
import com.dukascopy.api.system.ClientFactory
import com.dukascopy.api.system.ISystemListener

import java.util.HashSet

object SForex {

  def main(args: Array[String]) {

    // Arguments
    var username = "DEMO2VRsvV"
    var password = "VRsvV"

    // Get default instance of Dukascopy IClient
    val client = ClientFactory.getDefaultInstance

    // Create system listener
    val systemListener = new ISystemListener {

      var maxReconnects = 3

      override def onConnect {

        println("[network] connected on dukascopy server")
        maxReconnects = 3

      }

      override def onDisconnect {

        println("[network] disconnected from dukascopy server")
        if (maxReconnects > 0) {

          client.reconnect
          maxReconnects = maxReconnects - 1

        } else {

          try {
            Thread.sleep(10000)
          } catch {
            case e: InterruptedException => //ignore
          }

          try {
            client.connect("https://www.dukascopy.com/client/demo/jclient/jforex.jnlp", username, password)
          } catch {
            case e: Exception => {
              println(e.getMessage(), e)
            }
          }

        }

      }

      override def onStart(processId: Long) {

        println("[strategy] started process id: " + processId)

      }

      override def onStop(processId: Long) {

        println("[strategy] stopped process id: " + processId)
        if (client.getStartedStrategies.size == 0)
          exit(0)

      }

    }

    // Set system listener
    client.setSystemListener(systemListener)

    // Connect to Dukascopy
    println("[network] connecting to dukascopy server...")

    client.connect("https://www.dukascopy.com/client/demo/jclient/jforex.jnlp", username, password)

    // Wait of Dukascopy response
    var maxSeconds = 10
    while (maxSeconds > 0 && !client.isConnected) {

      Thread.sleep(1000)
      maxSeconds = maxSeconds - 1

    }

    // If not connected, exit
    if (!client.isConnected()) {
      println("[network] failed to connect dukascopy servers")
      exit(1)
    }

    //now it's running
    exit(0)

  }

}