        public void run() {
            gameLog = new Vector();
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getContentPane().removeAll();
                        mainPanel = new JPanel();
                        setMainPanel(mainPanel);
                        io = new ColoredSwingClient(esto, gameLog);
                        if (logFile != null) {
                            if (((ColoredSwingClient) io).getSoundClient() instanceof AGESoundClient) {
                                AGESoundClient asc = (AGESoundClient) ((ColoredSwingClient) io).getSoundClient();
                                asc.deactivate();
                            }
                        }
                        CommonSwingFunctions.writeIntroductoryInfo(SwingAetheriaGameLoader.this);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            String worldName;
            World theWorld;
            if (moduledir == null || moduledir.length() == 0) moduledir = "aetherworld";
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        repaint();
                        updateNow();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            theWorld = WorldLoader.loadWorld(moduledir, gameLog, io, mundoSemaphore);
            if (theWorld == null || io.isDisconnected()) return;
            mundo = theWorld;
            final World theFinalWorld = theWorld;
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        updateNow();
                        if (theFinalWorld.getModuleName() != null && theFinalWorld.getModuleName().length() > 0) setTitle(theFinalWorld.getModuleName());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (new VersionComparator().compare(GameEngineThread.getVersionNumber(), theWorld.getRequiredAGEVersion()) < 0) {
                String mess = UIMessages.getInstance().getMessage("age.version.warning", "$curversion", GameEngineThread.getVersionNumber(), "$reqversion", theWorld.getRequiredAGEVersion(), "$world", theWorld.getModuleName());
                mess = mess + " " + UIMessages.getInstance().getMessage("age.download.url");
                JOptionPane.showMessageDialog(SwingAetheriaGameLoader.this, mess, UIMessages.getInstance().getMessage("age.version.warning.title"), JOptionPane.WARNING_MESSAGE);
            }
            if (Debug.DEBUG_OUTPUT) {
                org.w3c.dom.Document d = null;
                try {
                    d = theWorld.getXMLRepresentation();
                } catch (javax.xml.parsers.ParserConfigurationException exc) {
                    System.out.println(exc);
                }
                javax.xml.transform.stream.StreamResult sr = null;
                try {
                    sr = new javax.xml.transform.stream.StreamResult(new FileOutputStream("theworld.xml"));
                } catch (FileNotFoundException fnfe) {
                    System.out.println(fnfe);
                }
                try {
                    javax.xml.transform.Transformer tr = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "ISO-8859-1");
                    javax.xml.transform.Source s = new javax.xml.transform.dom.DOMSource(d);
                    tr.transform(s, sr);
                } catch (javax.xml.transform.TransformerConfigurationException tfe) {
                    System.out.println(tfe);
                } catch (javax.xml.transform.TransformerException te) {
                    System.out.println(te);
                }
            }
            if (stateFile != null) {
                try {
                    theWorld.loadState(stateFile);
                } catch (Exception exc) {
                    write(UIMessages.getInstance().getMessage("swing.cannot.read.state", "$file", stateFile));
                    write(exc.toString());
                    exc.printStackTrace();
                }
            }
            if (usarLog) {
                try {
                    System.out.println("RLTLTL");
                    System.out.println("Player list is " + theWorld.getPlayerList());
                    System.out.println("PECADORL");
                    theWorld.prepareLog(logFile);
                    theWorld.setRandomNumberSeed(logFile);
                } catch (Exception exc) {
                    write(UIMessages.getInstance().getMessage("swing.cannot.read.log", "$exc", exc.toString()));
                    exc.printStackTrace();
                    return;
                }
            } else {
                theWorld.setRandomNumberSeed();
            }
            gameLog.addElement(String.valueOf(theWorld.getRandomNumberSeed()));
            setVisible(true);
            timeCount = 0;
            mundo = theWorld;
            synchronized (mundoSemaphore) {
                mundoSemaphore.notifyAll();
            }
            maquinaEstados = new GameEngineThread(theWorld, esto, false);
            maquinaEstados.start();
            System.out.println("noSerCliente = " + noSerCliente);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        repaint();
                        updateNow();
                        if (!fullScreenMode) {
                            setVisible(false);
                            setVisible(true);
                        } else {
                            fullScreenFrame.setVisible(false);
                            fullScreenFrame.setVisible(true);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (io instanceof ColoredSwingClient) ((ColoredSwingClient) io).refreshFocus();
        }
