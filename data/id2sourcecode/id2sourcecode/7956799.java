        public void run() {
            gameLog = new Vector();
            Debug.println("1");
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getContentPane().removeAll();
                        mainPanel = new JPanel();
                        setMainPanel(mainPanel);
                        io = new ColoredSwingClient(SwingSDIInterface.this, gameLog);
                        if (logFile != null) {
                            ((ColoredSwingClient) io).hideForLogLoad();
                            if (((ColoredSwingClient) io).getSoundClient() instanceof AGESoundClient) {
                                AGESoundClient asc = (AGESoundClient) ((ColoredSwingClient) io).getSoundClient();
                                asc.deactivate();
                            }
                        }
                        CommonSwingFunctions.writeIntroductoryInfo(SwingSDIInterface.this);
                    }
                });
            } catch (Exception e) {
                ((ColoredSwingClient) io).showAfterLogLoad();
                e.printStackTrace();
            }
            String worldName;
            World theWorld = null;
            if (moduledir == null || moduledir.length() == 0) moduledir = "aetherworld";
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        repaint();
                        updateNow();
                    }
                });
            } catch (Exception e) {
                ((ColoredSwingClient) io).showAfterLogLoad();
                e.printStackTrace();
            }
            try {
                theWorld = WorldLoader.loadWorld(moduledir, gameLog, io, mundoSemaphore);
            } catch (Exception e) {
                ((ColoredSwingClient) io).showAfterLogLoad();
                write("Exception on loading world: " + e);
                e.printStackTrace();
            }
            if (theWorld == null || io.isDisconnected()) {
                ((ColoredSwingClient) io).showAfterLogLoad();
                return;
            }
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
                ((ColoredSwingClient) io).showAfterLogLoad();
                e.printStackTrace();
            }
            if (new VersionComparator().compare(GameEngineThread.getVersionNumber(), theWorld.getRequiredAGEVersion()) < 0) {
                String mess = UIMessages.getInstance().getMessage("age.version.warning", "$curversion", GameEngineThread.getVersionNumber(), "$reqversion", theWorld.getRequiredAGEVersion(), "$world", theWorld.getModuleName());
                mess = mess + " " + UIMessages.getInstance().getMessage("age.download.url");
                JOptionPane.showMessageDialog(SwingSDIInterface.this, mess, UIMessages.getInstance().getMessage("age.version.warning.title"), JOptionPane.WARNING_MESSAGE);
            }
            if (stateFile != null) {
                try {
                    theWorld.loadState(stateFile);
                } catch (Exception exc) {
                    ((ColoredSwingClient) io).showAfterLogLoad();
                    write(UIMessages.getInstance().getMessage("swing.cannot.read.state", "$file", stateFile));
                    write(exc.toString());
                    exc.printStackTrace();
                }
            }
            if (usarLog) {
                try {
                    theWorld.prepareLog(logFile);
                    theWorld.setRandomNumberSeed(logFile);
                } catch (Exception exc) {
                    ((ColoredSwingClient) io).showAfterLogLoad();
                    write(UIMessages.getInstance().getMessage("swing.cannot.read.log", "$exc", exc.toString()));
                    exc.printStackTrace();
                    return;
                }
            } else {
                theWorld.setRandomNumberSeed();
            }
            gameLog.addElement(String.valueOf(theWorld.getRandomNumberSeed()));
            setVisible(true);
            mundo = theWorld;
            synchronized (mundoSemaphore) {
                mundoSemaphore.notifyAll();
            }
            maquinaEstados = new GameEngineThread(theWorld, SwingSDIInterface.this, false);
            maquinaEstados.start();
            System.out.println("noSerCliente = " + false);
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
            if (io instanceof ColoredSwingClient) ((ColoredSwingClient) io).refreshFocus();
        }
