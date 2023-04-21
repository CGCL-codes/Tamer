    public void init() {
        if (debug > 0) System.err.println("Applet: init()");
        if (pluginLoader == null) {
            try {
                options.load(Applet.class.getResourceAsStream("/de/mud/jta/default.conf"));
            } catch (Exception e) {
                try {
                    URL url = new URL(getCodeBase() + "default.conf");
                    options.load(url.openStream());
                } catch (Exception e1) {
                    System.err.println("jta: cannot load default.conf");
                    System.err.println("jta: try extracting it from the jar file");
                    System.err.println("jta: expected file here: " + getCodeBase() + "default.conf");
                }
            }
            String value;
            if ((value = getParameter("config")) != null) {
                Properties appletParams = new Properties();
                URL url = null;
                try {
                    url = new URL(value);
                } catch (Exception e) {
                    try {
                        url = new URL(getCodeBase() + value);
                    } catch (Exception ce) {
                        System.err.println("jta: could not find config file: " + ce);
                    }
                }
                if (url != null) {
                    try {
                        appletParams.load(Applet.class.getResourceAsStream("/de/mud/jta/" + value));
                        Enumeration ape = appletParams.keys();
                        while (ape.hasMoreElements()) {
                            String key = (String) ape.nextElement();
                            options.put(key, appletParams.getProperty(key));
                        }
                    } catch (Exception e) {
                        try {
                            appletParams.load(url.openStream());
                            Enumeration ape = appletParams.keys();
                            while (ape.hasMoreElements()) {
                                String key = (String) ape.nextElement();
                                options.put(key, appletParams.getProperty(key));
                            }
                        } catch (Exception e2) {
                            System.err.println("jta: could not load config file: " + e2);
                        }
                    }
                }
            }
            parameterOverride(options);
            pluginLoader = new Common(options);
            host = options.getProperty("Socket.host");
            if (host == null) host = getCodeBase().getHost();
            port = options.getProperty("Socket.port");
            if (port == null) port = "23";
            if ((new Boolean(options.getProperty("Applet.connect")).booleanValue())) connect = true;
            if (!(new Boolean(options.getProperty("Applet.disconnect")).booleanValue())) disconnect = false;
            if (!(new Boolean(options.getProperty("Applet.disconnect.closeWindow")).booleanValue())) disconnectCloseWindow = false;
            frameTitle = options.getProperty("Applet.detach.title");
            if ((new Boolean(options.getProperty("Applet.detach"))).booleanValue()) {
                if (frameTitle == null) {
                    appletFrame = (RootPaneContainer) new JFrame("jta: " + host + (port.equals("23") ? "" : " " + port));
                } else {
                    appletFrame = (RootPaneContainer) new JFrame(frameTitle);
                }
            } else {
                appletFrame = (RootPaneContainer) this;
            }
            appletFrame.getContentPane().setLayout(new BorderLayout());
            Map componentList = pluginLoader.getComponents();
            Iterator names = componentList.keySet().iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                Component c = (Component) componentList.get(name);
                if ((value = options.getProperty("layout." + name)) != null) {
                    appletFrame.getContentPane().add(value, c);
                } else {
                    System.err.println("jta: no layout property set for '" + name + "'");
                    System.err.println("jta: ignoring '" + name + "'");
                }
            }
            pluginLoader.registerPluginListener(new SoundListener() {

                public void playSound(URL audioClip) {
                    Applet.this.getAudioClip(audioClip).play();
                }
            });
            pluginLoader.broadcast(new AppletRequest(this));
            if (appletFrame != this) {
                final String startText = options.getProperty("Applet.detach.startText");
                final String stopText = options.getProperty("Applet.detach.stopText");
                final Button close = new Button();
                Vector privileges = Common.split(options.getProperty("Applet.Netscape.privilege"), ',');
                Class privilegeManager = null;
                Method enable = null;
                try {
                    privilegeManager = Class.forName("netscape.security.PrivilegeManager");
                    enable = privilegeManager.getMethod("enablePrivilege", new Class[] { String.class });
                } catch (Exception e) {
                    System.err.println("Applet: This is not Netscape ...");
                }
                if (privilegeManager != null && enable != null && privileges != null) for (int i = 0; i < privileges.size(); i++) try {
                    enable.invoke(privilegeManager, new Object[] { privileges.elementAt(i) });
                    System.out.println("Applet: access for '" + privileges.elementAt(i) + "' allowed");
                } catch (Exception e) {
                    System.err.println("Applet: access for '" + privileges.elementAt(i) + "' denied");
                }
                try {
                    clipboard = appletFrame.getContentPane().getToolkit().getSystemClipboard();
                    System.err.println("Applet: acquired system clipboard: " + clipboard);
                } catch (Exception e) {
                    System.err.println("Applet: system clipboard access denied: " + ((e instanceof InvocationTargetException) ? ((InvocationTargetException) e).getTargetException() : e));
                } finally {
                    if (clipboard == null) {
                        System.err.println("Applet: copy & paste only within the JTA");
                        clipboard = new Clipboard("de.mud.jta.Main");
                    }
                }
                if ((new Boolean(options.getProperty("Applet.detach.immediately")).booleanValue())) {
                    if ((new Boolean(options.getProperty("Applet.detach.fullscreen")).booleanValue())) ((JFrame) appletFrame).setSize(appletFrame.getContentPane().getToolkit().getScreenSize()); else ((JFrame) appletFrame).pack();
                    ((JFrame) appletFrame).show();
                    pluginLoader.broadcast(new SocketRequest(host, Integer.parseInt(port)));
                    pluginLoader.broadcast(new ReturnFocusRequest());
                    close.setLabel(startText != null ? stopText : "Disconnect");
                } else close.setLabel(startText != null ? startText : "Connect");
                close.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (((JFrame) appletFrame).isVisible()) {
                            pluginLoader.broadcast(new SocketRequest());
                            ((JFrame) appletFrame).setVisible(false);
                            close.setLabel(startText != null ? startText : "Connect");
                        } else {
                            if (frameTitle == null) ((JFrame) appletFrame).setTitle("jta: " + host + (port.equals("23") ? "" : " " + port));
                            if ((new Boolean(options.getProperty("Applet.detach.fullscreen")).booleanValue())) ((JFrame) appletFrame).setSize(appletFrame.getContentPane().getToolkit().getScreenSize()); else ((JFrame) appletFrame).pack();
                            ((JFrame) appletFrame).show();
                            if (port == null || port.length() <= 0) port = "23";
                            getAppletContext().showStatus("Trying " + host + " " + port + " ...");
                            pluginLoader.broadcast(new SocketRequest(host, Integer.parseInt(port)));
                            pluginLoader.broadcast(new ReturnFocusRequest());
                            close.setLabel(stopText != null ? stopText : "Disconnect");
                        }
                    }
                });
                getContentPane().setLayout(new BorderLayout());
                getContentPane().add("Center", close);
                MenuBar mb = new MenuBar();
                Menu file = new Menu("File");
                file.setShortcut(new MenuShortcut(KeyEvent.VK_F, true));
                MenuItem tmp;
                file.add(tmp = new MenuItem("Connect"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        pluginLoader.broadcast(new SocketRequest(host, Integer.parseInt(port)));
                    }
                });
                file.add(tmp = new MenuItem("Disconnect"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        pluginLoader.broadcast(new SocketRequest());
                    }
                });
                file.add(new MenuItem("-"));
                file.add(tmp = new MenuItem("Print"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (pluginLoader.getComponents().get("Terminal") != null) {
                            PrintJob printJob = appletFrame.getContentPane().getToolkit().getPrintJob((JFrame) appletFrame, "JTA Terminal", null);
                            ((Component) pluginLoader.getComponents().get("Terminal")).print(printJob.getGraphics());
                            printJob.end();
                        }
                    }
                });
                file.add(new MenuItem("-"));
                file.add(tmp = new MenuItem("Exit"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        ((JFrame) appletFrame).setVisible(false);
                        pluginLoader.broadcast(new SocketRequest());
                        close.setLabel(startText != null ? startText : "Connect");
                    }
                });
                mb.add(file);
                Menu edit = new Menu("Edit");
                edit.setShortcut(new MenuShortcut(KeyEvent.VK_E, true));
                edit.add(tmp = new MenuItem("Copy"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (debug > 2) System.err.println("Applet: copy: " + focussedPlugin);
                        if (focussedPlugin instanceof VisualTransferPlugin) ((VisualTransferPlugin) focussedPlugin).copy(clipboard);
                    }
                });
                edit.add(tmp = new MenuItem("Paste"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (debug > 2) System.err.println("Applet: paste: " + focussedPlugin);
                        if (focussedPlugin instanceof VisualTransferPlugin) ((VisualTransferPlugin) focussedPlugin).paste(clipboard);
                    }
                });
                mb.add(edit);
                Map menuList = pluginLoader.getMenus();
                names = menuList.keySet().iterator();
                while (names.hasNext()) {
                    String name = (String) names.next();
                    Object o = menuList.get(name);
                    if (o instanceof Menu) mb.add((Menu) o);
                }
                Menu help = new Menu("Help");
                help.setShortcut(new MenuShortcut(KeyEvent.VK_HELP, true));
                help.add(tmp = new MenuItem("General"));
                tmp.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Help.show(appletFrame.getContentPane(), options.getProperty("Help.url"));
                    }
                });
                mb.setHelpMenu(help);
                if ((new Boolean(options.getProperty("Applet.detach.menuBar")).booleanValue())) ((JFrame) appletFrame).setMenuBar(mb);
                try {
                    ((JFrame) appletFrame).addWindowListener(new WindowAdapter() {

                        public void windowClosing(WindowEvent evt) {
                            pluginLoader.broadcast(new SocketRequest());
                            ((JFrame) appletFrame).setVisible(false);
                            close.setLabel(startText != null ? startText : "Connect");
                        }
                    });
                } catch (Exception e) {
                    System.err.println("Applet: could not set up Window event listener");
                    System.err.println("Applet: you will not be able to close it");
                }
                pluginLoader.registerPluginListener(new OnlineStatusListener() {

                    public void online() {
                        if (debug > 0) System.err.println("Terminal: online");
                        online = true;
                        if (((JFrame) appletFrame).isVisible() == false) ((JFrame) appletFrame).setVisible(true);
                    }

                    public void offline() {
                        if (debug > 0) System.err.println("Terminal: offline");
                        online = false;
                        if (disconnectCloseWindow) {
                            ((JFrame) appletFrame).setVisible(false);
                            close.setLabel(startText != null ? startText : "Connect");
                        }
                    }
                });
                pluginLoader.registerPluginListener(new FocusStatusListener() {

                    public void pluginGainedFocus(Plugin plugin) {
                        if (Applet.debug > 0) System.err.println("Applet: " + plugin + " got focus");
                        focussedPlugin = plugin;
                    }

                    public void pluginLostFocus(Plugin plugin) {
                        if (Applet.debug > 0) System.err.println("Applet: " + plugin + " lost focus");
                    }
                });
            } else pluginLoader.registerPluginListener(new OnlineStatusListener() {

                public void online() {
                    if (debug > 0) System.err.println("Terminal: online");
                    online = true;
                }

                public void offline() {
                    if (debug > 0) System.err.println("Terminal: offline");
                    online = false;
                }
            });
        }
    }
