    public void run() {
        try {
            in = new DataInputStream(con.getInputStream());
            out = new DataOutputStream(con.getOutputStream());
        } catch (Exception ex) {
            LOGIT.severe(ex, "No input/output stream can be created");
            return;
        }
        try {
            while (!finished) {
                Messages header = new Messages();
                String headerData = getHeaderData();
                header.Set(headerData);
                String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);
                HandleCommands(headerData + bodyData);
            }
        } catch (NetworkDataException ex) {
            LOGIT.severe(ex, "Thread ending because of network data read/write issues");
            return;
        } catch (NetServerException ex) {
            LOGIT.severe(ex, "Error processing request. Connection Closed");
            return;
        } catch (MessagesException ex) {
            LOGIT.severe(ex, "Unable to parse message from client");
            try {
                sendError(15, MESSAGENOTUNDERSTOOD, "");
            } catch (NetServerException ignored) {
            }
            return;
        }
        try {
            out.close();
            in.close();
            con.close();
        } catch (IOException ex) {
            return;
        }
    }
