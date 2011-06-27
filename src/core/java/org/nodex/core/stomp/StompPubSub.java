package org.nodex.core.stomp;

import org.nodex.core.Callback;
import org.nodex.core.net.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: timfox
 * Date: 28/06/2011
 * Time: 00:19
 */
public class StompPubSub {



  public static Server createServer() {
    return StompServer.createServer(new Callback<Connection>() {

      private ConcurrentMap<String, List<Connection>> subscriptions = new ConcurrentHashMap<String, List<Connection>>();

      private synchronized void subscribe(String dest, Connection conn) {
        List<Connection> conns = subscriptions.get(dest);
        if (conns == null) {
          conns = new ArrayList<Connection>();
          subscriptions.put(dest, conns);
        }
        conns.add(conn);
      }

      private synchronized void unsubscribe(String dest, Connection conn) {
        List<Connection> conns = subscriptions.get(dest);
        if (conns == null) {
          conns.remove(conn);
          if (conns.isEmpty()) {
            subscriptions.remove(dest);
          }
        }
      }

      public void onEvent(final Connection conn) {
        conn.data(new Callback<Frame>() {
          public void onEvent(Frame frame) {
            if ("subscribe".equals(frame.command)) {
              String dest = frame.headers.get("destination");
              subscribe(dest, conn);
            } else if ("unsubscribe".equals(frame.command)) {
              String dest = frame.headers.get("destination");
              unsubscribe(dest, conn);
            } else if ("message".equals(frame.command)) {
              String dest = frame.headers.get("destination");
              List<Connection> conns = subscriptions.get(dest);
              if (conns != null) {
                for (Connection conn : conns) {
                  conn.write(frame);
                }
              }
            }
          }
        });
      }


    });
  }


}