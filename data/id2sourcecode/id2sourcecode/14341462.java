    VisageOutputLink(String host, int port) throws Exception {
        try {
            log.info("Opening Connection to '" + host + ":" + port + "'");
            this.socket = new Socket(host, port);
            log.finer("Creating buffered reader & writers");
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.inputStrings = new ArrayList<String>();
        } catch (Exception e) {
            log.severe("Error setting up connection: " + e);
            close();
            throw e;
        }
    }
