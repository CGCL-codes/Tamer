    public void run() {
        ServerSocket mainServer = null;
        Socket incoming = null;
        try {
            mainServer = new ServerSocket(propertyList.getPortNumber());
        } catch (IOException e) {
            System.out.println(e);
        }
        while (true) {
            try {
                incoming = mainServer.accept();
                if (currentIncoming < maxIncoming) {
                    currentIncoming++;
                    IncomingConnectionHandler connection = new IncomingConnectionHandler(incoming);
                    connection.start();
                } else {
                    DataOutputStream outStream = new DataOutputStream(incoming.getOutputStream());
                    outStream.writeBytes("Sorry, maximum incoming connections (" + maxIncoming + ") already reached!\n");
                    outStream = null;
                    incoming.close();
                }
            } catch (Exception ei) {
                ei.printStackTrace();
            }
        }
    }
