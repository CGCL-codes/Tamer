            @Override
            public void run() {
                LineIterator it = new LineIterator(is, false, true);
                try {
                    String key = it.next();
                    if (!key.startsWith("##GenomeView session")) {
                        JOptionPane.showMessageDialog(model.getGUIManager().getParent(), "The selected file is not a GenomeView session");
                    } else {
                        model.clearEntries();
                        for (String line : it) {
                            char c = line.charAt(0);
                            line = line.substring(2);
                            model.messageModel().setStatusBarMessage("Loading session, current file: " + line + "...");
                            switch(c) {
                                case 'U':
                                case 'F':
                                    try {
                                        DataSourceHelper.load(model, new Locator(line));
                                    } catch (RuntimeException re) {
                                        log.log(Level.SEVERE, "Something went wrong while loading line: " + line + "\n\tfrom the session file.\n\tTo recover GenomeView skipped this file.", re);
                                    }
                                    break;
                                case 'C':
                                    Configuration.loadExtra(URIFactory.url(line).openStream());
                                default:
                                    log.info("Could not load session line: " + line);
                                    break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    CrashHandler.crash(Level.SEVERE, "Could not load session", ex);
                }
                it.close();
                model.messageModel().setStatusBarMessage(null);
            }
