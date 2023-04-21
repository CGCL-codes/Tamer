        void menu(int choice) throws Exception {
            switch(choice) {
                case 5:
                    System.out.println(getString());
                    break;
                case 6:
                    hostname = readString("String hostname");
                    port = readInt("int port");
                    connectClient(new InetSocketAddress(hostname, port));
                    break;
                case 7:
                    checkClients();
                    break;
                case 8:
                    Util.debug("LISTING CLIENTS");
                    Util.debug("id\t|\tUUID\t|\tclosed\t|\tstayOpen\t|\taddress\t|\tremoteAddress\t|\tplayer");
                    int i = 0;
                    synchronized (clients) {
                        for (Client client : clients) {
                            Util.debug(i++ + ": " + client.id + " | " + !client.socket.isClosed() + " | " + client.stayOpen + " | " + client.address + " | " + client.socket.getRemoteSocketAddress() + " | " + client.getPlayer());
                        }
                    }
                    break;
                case 9:
                    System.out.println(getProxy());
                    break;
                case 10:
                    try {
                        sendMessage(readString("char command").charAt(0), readInts("int[] nums"), readString("String str"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    hostname = readString("String hostname");
                    port = readInt("int port");
                    synchronized (clients) {
                        for (Client client : clients) {
                            if (hostname.equals(client.address.getHostName()) && port == client.address.getPort()) {
                                writeClient(client, readString("char command").charAt(0), readInts("int[] nums"), readString("String str"));
                                return;
                            }
                        }
                    }
                    break;
                case 12:
                    writeClient(clients.get(readInt("int id")), readString("char command").charAt(0), readInts("int[] nums"), readString("String str"));
                    break;
                case 13:
                    final String uuid = readString("String uuid");
                    synchronized (clients) {
                        for (Client client : clients) {
                            if (uuid.equals(client.id.toString())) {
                                writeClient(client, readString("char command").charAt(0), readInts("int[] nums"), readString("String str"));
                                return;
                            }
                        }
                    }
                    break;
                default:
                    baseMenu(choice);
                    break;
            }
        }
