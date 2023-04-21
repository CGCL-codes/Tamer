    public void run() {
        try {
            final String threadName = Thread.currentThread().getName();
            logger.finest("Proxy Thread started for client: " + client);
            EventDispatcher.add(new ApplicationEvent(ApplicationEvent.CONNECTION_ESTABLISHED));
            boolean keepAlive = true;
            while (keepAlive) {
                HttpData httpData = new HttpData();
                httpData.inetAddress = client.getInetAddress();
                httpData.port = client.getLocalPort();
                BufferedInputStream clientIn = new BufferedInputStream(client.getInputStream());
                RequestHeader requestHeader = new RequestHeader(clientIn);
                httpData.req = requestHeader;
                requestHeader.removeHeader("proxy-connection");
                if (Configuration.getPrefs().getBoolean(ConfigConstants.PROXY_NO_CACHING_HEADERS, true)) {
                    removeCachingHeaders(requestHeader);
                }
                String host = requestHeader.getHost();
                logger.fine(threadName + " Opening connection to: " + requestHeader.getUrl());
                if (host == null) throw new BadRequest_400("Host missing");
                Integer port = requestHeader.getPort();
                if (port == null) port = new Integer(80);
                Socket server = new Socket(host, port.intValue());
                BufferedOutputStream serverOut = new BufferedOutputStream(server.getOutputStream());
                requestHeader.write(serverOut);
                serverOut.flush();
                logger.finest(threadName + " Request Header sent to server.");
                if (requestHeader.getContentLength() != null && requestHeader.getPostData() != null) {
                    logger.fine(threadName + " Request Content-Length=" + requestHeader.getContentLength());
                    serverOut.write(requestHeader.getPostData());
                    serverOut.flush();
                    logger.finest(threadName + " Content written to server.");
                }
                BufferedInputStream serverIn = new BufferedInputStream(server.getInputStream());
                ResponseHeader responseHeader = new ProxyResponse(serverIn);
                if (Configuration.getPrefs().getBoolean(ConfigConstants.PROXY_NO_CACHING_HEADERS, true)) {
                    removeCachingHeaders(responseHeader);
                }
                httpData.resp = responseHeader;
                if (responseHeader.getContentLength() != null) {
                    httpData.fileData.setContentLength(responseHeader.getContentLength().longValue());
                }
                httpData.fileData.setContentType(responseHeader.getHeader("Content-Type"));
                httpData.fileData.setEncoding(responseHeader.getHeader("Content-Encoding"));
                logger.finest(threadName + " Response headers read:" + httpData.resp);
                BufferedOutputStream clientOut = new BufferedOutputStream(client.getOutputStream());
                responseHeader.write(clientOut);
                clientOut.flush();
                logger.finest(threadName + " Response header written to client.");
                if (responseHeader.getContentLength() != null) {
                    ContentLengthInputStream clIn = new ContentLengthInputStream(serverIn, responseHeader.getContentLength().intValue());
                    File cacheFile = TempFileHandler.getTempFile();
                    httpData.fileData.setFile(cacheFile);
                    FileOutputStream fileOut = new FileOutputStream(cacheFile);
                    byte[] bytes = new byte[4800];
                    int read = -1;
                    logger.finest(threadName + " Writing contentLength input to file:" + cacheFile);
                    while ((read = clIn.read(bytes)) != -1) {
                        fileOut.write(bytes, 0, read);
                    }
                    fileOut.close();
                    logger.finest(threadName + " Writing contentLength input to client.");
                    slowOutput(clientOut, cacheFile);
                    clientOut.flush();
                } else if (responseHeader.getHeader("Transfer-encoding") != null && responseHeader.getHeader("Transfer-Encoding").equalsIgnoreCase("chunked")) {
                    ChunkedInputStream chIn = new ChunkedInputStream(serverIn);
                    File cacheFile = TempFileHandler.getTempFile();
                    httpData.fileData.setFile(cacheFile);
                    FileOutputStream fileOut = new FileOutputStream(cacheFile);
                    int read = -1;
                    logger.finest(threadName + " Writing Chunked to file: " + cacheFile);
                    while ((read = chIn.read()) != -1) {
                        fileOut.write(read);
                    }
                    fileOut.close();
                    logger.finest(threadName + " Writing Chunked to client.");
                    ChunkedOutputStream chOut = new ChunkedOutputStream(clientOut);
                    slowOutput(chOut, cacheFile);
                    chIn.close();
                    chOut.flush();
                    chOut.close();
                } else if ((responseHeader.getHeader("Connection") != null && responseHeader.getHeader("Connection").equalsIgnoreCase("close")) || (responseHeader.getProtocol().equalsIgnoreCase("http/1.0"))) {
                    keepAlive = false;
                    File cacheFile = TempFileHandler.getTempFile();
                    httpData.fileData.setFile(cacheFile);
                    FileOutputStream fileOut = new FileOutputStream(cacheFile);
                    int read = -1;
                    logger.finest(threadName + " Writing connectionClose to input to file:" + cacheFile);
                    while ((read = serverIn.read()) != -1) {
                        fileOut.write(read);
                    }
                    fileOut.close();
                    logger.finest(threadName + " Writing connectionClose to client.");
                    slowOutput(clientOut, cacheFile);
                }
                EventDispatcher.add(httpData.asEvent());
                logger.finest(threadName + " Request: " + requestHeader.getUrl() + " done.");
            }
        } catch (Throwable e) {
            logger.info(e + "");
            EventDispatcher.add(new ErrorEvent(e));
        } finally {
            try {
                client.close();
            } catch (IOException e1) {
                logger.info(e1 + "");
            }
            EventDispatcher.add(new ApplicationEvent(ApplicationEvent.CONNECTION_CLOSED));
        }
        logger.fine(Thread.currentThread().getName() + " terminated.");
    }
