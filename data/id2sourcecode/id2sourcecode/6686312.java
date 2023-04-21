        @Override
        public void run() {
            try {
                while (!stop) {
                    Socket socket = server.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String request = reader.readLine();
                    if (!request.startsWith("GET ")) {
                        socket.close();
                        continue;
                    }
                    String handlerName = request.substring("GET ".length());
                    int index = handlerName.indexOf(" HTTP");
                    if (index < 0) {
                        socket.close();
                        continue;
                    }
                    handlerName = handlerName.substring(0, index).trim();
                    if (handlerName.length() == 0) {
                        socket.close();
                        continue;
                    }
                    if (handlerName.charAt(0) == '/' && handlerName.length() > 1) {
                        handlerName = handlerName.substring(1);
                    }
                    index = handlerName.indexOf("/");
                    String query = null;
                    if (index > 0) {
                        query = handlerName.substring(index + 1);
                        handlerName = handlerName.substring(0, index);
                    }
                    RPService handler = handlerMap.get(handlerName);
                    if (handler == null) {
                        log.error("Could not find handler for service: " + handlerName);
                        socket.close();
                        continue;
                    }
                    threadPool.submit(new HandleConnection(handler, query, socket, reader, writer));
                }
            } catch (IOException e) {
            } finally {
                server = null;
            }
        }
