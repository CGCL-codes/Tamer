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
