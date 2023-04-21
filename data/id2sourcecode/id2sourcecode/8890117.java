    public static void main(String[] args) throws IOException, InterruptedException {
        String serverURL = null;
        if ((args != null) && (args.length > 0)) {
            serverURL = args[0];
        }
        if (serverURL == null) {
            String[] searchArgs = null;
            ServicesSearch.servicios(searchArgs);
            if (ServicesSearch.serviceFound.size() == 0) {
                System.out.println("OBEX service not found");
                return;
            }
            serverURL = (String) ServicesSearch.serviceFound.elementAt(0);
        }
        System.out.println("Connecting to " + serverURL);
        ClientSession clientSession = (ClientSession) Connector.open(serverURL);
        HeaderSet hsConnectReply = clientSession.connect(null);
        if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
            System.out.println("Failed to connect");
            return;
        }
        HeaderSet hsOperation = clientSession.createHeaderSet();
        hsOperation.setHeader(HeaderSet.NAME, "Bienvenido.jpg");
        hsOperation.setHeader(HeaderSet.TYPE, "image");
        File file = new File("bluetooth-logo.jpg");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();
        Operation putOperation = clientSession.put(hsOperation);
        byte data[] = bytes;
        OutputStream os = putOperation.openOutputStream();
        os.write(data);
        os.close();
        putOperation.close();
        clientSession.disconnect(null);
        clientSession.close();
    }
