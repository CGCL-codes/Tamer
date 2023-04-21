    public void run() {
        while (running) {
            Socket conn = null;
            Request req = null;
            try {
                conn = listenPort.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            try {
                req = new Request(conn);
            } catch (IOException ie) {
                DefaultResponder.writeExceptionResponse(conn, ie, "Couldn't get Request in ServerThread");
                continue;
            } catch (MalformedHeaderException me) {
                DefaultResponder.writeExceptionResponse(conn, me, "Malformed headers in request");
                continue;
            }
            Action action = actionFactory.getSuitableAction(req);
            if (action != null) {
                req = action.filterRequest(req);
            }
            if (req == null) {
                try {
                    OutputStream outToClient = conn.getOutputStream();
                    DefaultResponder.writeDummyResponse(outToClient);
                    outToClient.close();
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            Socket connToServer = null;
            if (Main.passThruHost == null) {
                InetAddress[] hostAddrs = null;
                try {
                    hostAddrs = req.getHostAddrs();
                } catch (UnknownHostException e) {
                    DefaultResponder.writeExceptionResponse(conn, e, "Couldn't get host address");
                    continue;
                }
                int whichAddr = 0;
                if (hostAddrs.length > 1) {
                    whichAddr = (int) (Math.floor(Math.random() * hostAddrs.length));
                }
                try {
                    connToServer = new Socket(hostAddrs[whichAddr], req.getPort());
                } catch (IOException e) {
                    DefaultResponder.writeExceptionResponse(conn, e, "Couldn't get connection to server");
                    continue;
                }
            } else {
                try {
                    connToServer = new Socket(Main.passThruHost, Main.passThruPort);
                } catch (IOException e) {
                    DefaultResponder.writeExceptionResponse(conn, e, "Couldn't get connection to pass-thru server");
                    continue;
                }
            }
            if (req.getMethod().equalsIgnoreCase("CONNECT")) {
                InputStream inFromClient = req.getContent();
                OutputStream outToClient = null;
                InputStream inFromServer = null;
                OutputStream outToServer = null;
                try {
                    outToClient = conn.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inFromServer = connToServer.getInputStream();
                    outToServer = connToServer.getOutputStream();
                    if (Main.passThruHost == null) {
                        outToClient.write(("HTTP/1.0 200 OK\r\n" + "\r\n").getBytes());
                    } else {
                        req.write(outToServer);
                    }
                    Thread outboundThread = new Thread(new StreamCopier(inFromClient, outToServer, -1));
                    Thread inboundThread = new Thread(new StreamCopier(inFromServer, outToClient, -1));
                    outboundThread.start();
                    inboundThread.start();
                    outboundThread.join();
                    inboundThread.join();
                } catch (IOException ie) {
                    DefaultResponder.writeExceptionResponse(conn, ie, "I/O error in handling CONNECT method");
                } catch (InterruptedException ne) {
                }
                try {
                    conn.close();
                    connToServer.close();
                } catch (IOException e) {
                }
                continue;
            }
            OutputStream outToServer = null;
            try {
                outToServer = connToServer.getOutputStream();
                if (Main.passThruHost != null) {
                    req.writePassThru(outToServer);
                } else {
                    req.write(outToServer);
                }
                String contentLengthString = req.getHeaderValue("Content-Length");
                if (contentLengthString != null) {
                    int contentLength = -1;
                    try {
                        contentLength = Integer.parseInt(contentLengthString);
                    } catch (NumberFormatException ne) {
                    }
                    StreamCopier.copy(req.getContent(), outToServer, contentLength);
                } else if (req.getMethod().equalsIgnoreCase("POST")) {
                    StreamCopier.copy(req.getContent(), outToServer, -1);
                } else {
                }
            } catch (IOException e) {
                DefaultResponder.writeExceptionResponse(conn, e, "Couldn't send request to server");
                continue;
            }
            Response resp = null;
            try {
                resp = new Response(connToServer);
            } catch (IOException ie) {
                DefaultResponder.writeExceptionResponse(conn, ie, "Couldn't read Response from server");
                continue;
            } catch (MalformedHeaderException me) {
                DefaultResponder.writeExceptionResponse(conn, me, "Malformed HTTP response from server");
                continue;
            }
            if (action != null) {
                resp = action.filterResponse(resp);
            }
            OutputStream outToClient = null;
            try {
                outToClient = conn.getOutputStream();
                if (resp == null) {
                    DefaultResponder.writeDummyResponse(outToClient);
                    outToClient.close();
                    conn.close();
                    connToServer.close();
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            resp.clearHeaders("Proxy-Connection");
            resp.addHeaderValue("Proxy-Connection", "close");
            try {
                resp.write(outToClient);
                String respContentLengthString = resp.getHeaderValue("Content-Length");
                if (respContentLengthString != null) {
                    int contentLength = -1;
                    try {
                        contentLength = Integer.parseInt(respContentLengthString);
                    } catch (NumberFormatException ne) {
                    }
                    StreamCopier.copy(resp.getContent(), outToClient, contentLength);
                } else {
                    StreamCopier.copy(resp.getContent(), outToClient, -1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
                connToServer.close();
            } catch (IOException e) {
            }
        }
    }
