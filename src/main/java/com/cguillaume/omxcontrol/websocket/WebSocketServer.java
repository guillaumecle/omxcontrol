package com.cguillaume.omxcontrol.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketServer {
	
	public static void start(Integer port) {
		Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        
        @SuppressWarnings("serial")
		ServletHolder holder = new ServletHolder("ws", new WebSocketServlet() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.register(WebSocket.class);
			}
		});
        context.addServlet(holder, "/");
        try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void start() {
		start(8080);
	}
}
