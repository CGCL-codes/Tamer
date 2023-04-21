    private InputStream dataInStreamCommand(String cmd) {
        InputStream dataStream;
        if (passiveMode) {
            printCommand("PASV");
            waitForResult();
            getLastResult();
            int[] i = parsePassiveParentheses((String) lastlines.get(lastlines.size() - 1));
            String datahost = i[0] + "." + i[1] + "." + i[2] + "." + i[3];
            int dataport = (i[4] * 256) + i[5];
            try {
                dataPassiveConnection = new FTPPassiveConnection(datahost, dataport);
                dataStream = dataPassiveConnection.getIn();
            } catch (IOException e) {
                System.out.println(e);
                return null;
            }
            printCommand(cmd);
            waitForResult();
            getLastResult();
        } else {
            int port = portrange_lower + random.nextInt(portrange_upper - portrange_lower);
            System.out.println("port " + port);
            try {
                dataPortConnection = new FTPPortConnection(port);
            } catch (IOException e) {
                System.out.println(e);
                return null;
            }
            printCommand("PORT " + localip + "," + port2ascii(port));
            waitForResult();
            getLastResult();
            printCommand(cmd);
            waitForResult();
            getLastResult();
            try {
                dataPortConnection.waitForConnection();
            } catch (InterruptedException e) {
                System.out.println("Interrupted in join(): " + e);
            }
            try {
                dataStream = dataPortConnection.getIn();
            } catch (IOException e) {
                System.out.println("Can not establish connection :" + e);
                return null;
            }
        }
        return dataStream;
    }
