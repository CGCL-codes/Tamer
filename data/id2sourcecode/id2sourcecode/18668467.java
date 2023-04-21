                        public void handleEvent(Event event) {
                            Sett.ings().serverAddress = LogInWindow.comboServer.getText();
                            Sett.ings().hosts.add(LogInWindow.comboServer.getText());
                            Sett.ings().userName = LogInWindow.textUsername.getText();
                            Sett.ings().passWord = LogInWindow.textPassword.getText();
                            try {
                                if (Startup.server().userCenter().authN(Sett.ings().userName, Sett.ings().passWord)) {
                                    Startup.isOffline = false;
                                    Startup.userID = Startup.server().userCenter().getUser(Sett.ings().userName).getUserID();
                                    Startup.isTeacher = Startup.server().userCenter().getUser(Startup.userID) instanceof Teacher;
                                    Startup.groupID = null;
                                    if (!Sett.ings().serverAddress.equals("localhost")) {
                                        Set<String> remoteParsers = Startup.server().networkCenter().avaibleParsers();
                                        for (String parserJar : new File("parsers").list(new FilenameFilter() {

                                            public boolean accept(File dir, String name) {
                                                return name.matches(".*\\.jar");
                                            }
                                        })) {
                                            File parserFile = new File("parsers" + File.separator + parserJar);
                                            byte[] buffer = new byte[(int) parserFile.length()];
                                            BufferedInputStream input = new BufferedInputStream(new FileInputStream(parserFile));
                                            input.read(buffer, 0, buffer.length);
                                            input.close();
                                            byte[] localhash = MessageDigest.getInstance("MD5").digest(buffer);
                                            if (remoteParsers.contains(parserJar)) {
                                                if (Startup.server().networkCenter().verifyFile(parserJar) != localhash) new FileOutputStream(parserFile).write(Startup.server().networkCenter().downloadFile(parserJar));
                                                remoteParsers.remove(parserJar);
                                            }
                                        }
                                        for (String newParser : remoteParsers) new FileOutputStream(new File("parsers" + File.separator + newParser)).write(Startup.server().networkCenter().downloadFile(newParser));
                                    }
                                    Sync.initSync();
                                    LogInWindow.close(false);
                                    EditWindow.run();
                                }
                            } catch (Exception e) {
                                Startup.clearServer();
                                System.err.println("Login failed");
                                System.err.println(e);
                                e.printStackTrace();
                            }
                        }
