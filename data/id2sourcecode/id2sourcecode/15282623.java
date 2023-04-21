        public void run() {
            gameLog = new Vector();
            Debug.println("1");
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        getContentPane().removeAll();
                        mainPanel = new JPanel();
                        setMainPanel(mainPanel);
                        io = new ColoredSwingClient(SwingSDIApplet.this, gameLog);
                        CommonSwingFunctions.writeIntroductoryInfo(SwingSDIApplet.this);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("2");
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
                e.printStackTrace();
            }
            System.out.println("3");
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
            org.w3c.dom.Document d = null;
            try {
                d = theWorld.getXMLRepresentation();
                System.out.println("D=null?" + (d == null));
            } catch (javax.xml.parsers.ParserConfigurationException exc) {
                System.out.println(exc);
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
                    logStream.mark(100000);
                    theWorld.prepareLog(logStream);
                    logStream.reset();
                    theWorld.setRandomNumberSeed(logStream);
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
            mundo = theWorld;
            synchronized (mundoSemaphore) {
                mundoSemaphore.notifyAll();
            }
            maquinaEstados = new GameEngineThread(theWorld, SwingSDIApplet.this, false);
            maquinaEstados.start();
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        repaint();
                        updateNow();
                        setVisible(false);
                        setVisible(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (io instanceof ColoredSwingClient) ((ColoredSwingClient) io).refreshFocus();
        }
