    public void execute() {
        if (withGui) {
            MasterGui dialog = new MasterGui();
        }
        RemoteInfoThread remoteInfoThread = new RemoteInfoThread();
        remoteInfoThread.start();
        ReceiveSlaveConnectionsThread receiveSlaveConnectionsThread = new ReceiveSlaveConnectionsThread();
        receiveSlaveConnectionsThread.start();
        if (slaveFilename != null) {
            BufferedReader bufferedReader;
            String nextLine;
            int lineNumber = 1;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(slaveFilename))));
                while ((nextLine = bufferedReader.readLine()) != null) {
                    String[] split = nextLine.split(" ");
                    if (split.length != 2) {
                        System.err.println("Error: cannot interpret slave file line " + lineNumber + ": " + nextLine + ", got the following array: " + split);
                    } else {
                        try {
                            SlaveThread newSlave = new SlaveThread(split[0], Integer.parseInt(split[1]));
                            newSlave.start();
                        } catch (NumberFormatException e) {
                            System.err.println("Error: cannot read port number in line " + lineNumber + ": " + nextLine + ", '" + split[1] + "'");
                        } catch (IOException e) {
                            System.err.println("Error: couldn't connect to (line " + lineNumber + "): " + nextLine);
                            e.printStackTrace();
                        }
                    }
                    lineNumber++;
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        synchronized (this) {
            while (!evolutionDone) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
