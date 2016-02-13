package com.serli.orea.fakeservermail;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by gpoulet on 11/02/2016.
 */
public class App {

    int port;

    public App(String[] args) throws IOException {

        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        } else {
            this.port = 8000;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Fake com.serli.orea.fakeservermail.MailService Server Start");
    }

    private class MyHandler implements HttpHandler {

        public void handle(HttpExchange he) throws IOException {


            System.out.println(he.getRequestHeaders());
            System.out.println(he.getRequestBody());

            MailService mailService = new MailService();
            mailService.send(he.getRequestBody());

            String response = "<h1>Server start success if you see this message</h1>"
                    + "<h1>Port: " + port + "</h1>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
