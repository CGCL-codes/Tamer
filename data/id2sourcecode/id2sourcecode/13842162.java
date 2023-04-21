        private void connect() throws IOException {
            urlConnInbound = urlInput.openConnection();
            print("returned from urlInput.openConnection() [message receiving thread]");
            urlConnInbound.setUseCaches(false);
            urlConnInbound.setDoInput(true);
            urlConnInbound.setDoOutput(false);
            urlConnInbound.setRequestProperty("Connection", "close");
            print("returned from setRequestProperty 'Connection' 'close'");
            urlConnInbound.connect();
            print("returned from urlConnInbound.connect()");
            ois = new ObjectInputStream(urlConnInbound.getInputStream());
            String strProtocol = urlConnInbound.getURL().getProtocol();
            boolean bSecure = strProtocol.indexOf("https") == 0;
            print("HTTPMessageRouter: " + strProtocol + " :: " + urlConnInbound.getURL());
            if (connectionEventListener != null) {
                connectionEventListener.connected(bSecure);
            }
            print("returned from urlConnInbound.getInputStream()");
        }
