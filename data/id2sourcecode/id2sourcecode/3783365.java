            @Override
            public void run() {
                try {
                    System.out.println("Creating server...");
                    listener = new ServerSocket(40000);
                    servers = new Socket[PARTS];
                    System.out.println("Accept clients...");
                    for (int i = 0; i < PARTS; i++) {
                        servers[i] = listener.accept();
                    }
                    for (int j = 0; j < PARTS; j++) {
                        final int i = j;
                        new Thread() {

                            @Override
                            public void run() {
                                try {
                                    ShareFileWriter writer = new ShareFileWriter(shareFolders[i], new File("Downloads/" + shareFolders[i].getName()));
                                    byte[] b = new byte[servers[i].getReceiveBufferSize()];
                                    int read;
                                    int tot = 0;
                                    InputStream in = servers[i].getInputStream();
                                    while (tot < shareFolders[i].getSize()) {
                                        read = in.read(b);
                                        writer.write(b, read);
                                        tot += read;
                                    }
                                    done++;
                                    if (done == PARTS) {
                                        System.out.println("SERVER DONE!");
                                    }
                                    servers[i].close();
                                } catch (IOException ex) {
                                    Logger.getLogger(SocketTest.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    try {
                                        if (servers[i] != null) {
                                            servers[i].close();
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(SocketTest.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }.start();
                    }
                    listener.close();
                } catch (Exception ex) {
                    Logger.getLogger(SocketTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
