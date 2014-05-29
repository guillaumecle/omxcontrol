package com.cguillaume.omxcontrol.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class EventServer {
	
    public static void main(String[] args) {
		Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        
        ServletHolder holderEvents = new ServletHolder("ws", new WebSocketServlet() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				System.out.println("registred");
				factory.register(EventSocket.class);
			}
		});
        context.addServlet(holderEvents, "/");
        try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
