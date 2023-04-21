    private void takeInput() {
        try {
            System.out.println(socket.getInetAddress() + ":" + socket.getPort());
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String requestMessageLine = inFromClient.readLine();
                System.out.println(requestMessageLine);
                if (requestMessageLine != null) {
                    if (requestMessageLine.equals("GETCHANNELS")) {
                        getChannels(outToClient);
                    } else if (requestMessageLine.startsWith("ADDCHANNEL ")) {
                        addChannel(requestMessageLine.substring(11), outToClient);
                    } else if (requestMessageLine.startsWith("SETSERVERS ")) {
                        setServerCount(requestMessageLine.substring(11));
                    } else if (requestMessageLine.startsWith("SETMOTD ")) {
                        setServerMOTD(requestMessageLine.substring(8));
                    } else if (requestMessageLine.startsWith("GETMOTD")) {
                        getMOTD(outToClient);
                    }
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
