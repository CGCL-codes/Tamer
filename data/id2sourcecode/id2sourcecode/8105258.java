        public void run() {
            WebmeetMessage mess = null;
            try {
                Socket sock = new Socket(strHost, iPort);
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                ServerIDMessage serverID = (ServerIDMessage) ois.readObject();
                pi.setServerID(serverID.getServerID());
                pi.setLogonName(strName);
                pi.setConfID(strConfID);
                sendMessage(new RosterJoinMessage(strPassword));
                Object obj = null;
                do {
                    obj = ois.readObject();
                } while (!(obj instanceof RosterJoinAcceptMessage));
                Runnable writerThread = new Runnable() {

                    public void run() {
                        try {
                            while (true) {
                                sendMessage(new ChatMessage("test"));
                                Thread.currentThread().sleep(5000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread writer = new Thread(writerThread);
                writer.start();
                while (true) {
                    mess = (WebmeetMessage) ois.readObject();
                    mess = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
