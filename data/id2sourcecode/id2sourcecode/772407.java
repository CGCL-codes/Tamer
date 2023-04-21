    public Farmyard() {
        window.setSize(800, 600);
        Dimension ss = window.getToolkit().getScreenSize();
        Dimension ws = window.getSize();
        window.setLocation((ss.width - ws.width) / 2, (ss.height - ws.height) / 2);
        MenuBar mb = new MenuBar();
        Menu m = new Menu("File");
        MenuItem mi = new MenuItem("Exit");
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                window.dispose();
                try {
                    SmartCard.shutdown();
                } catch (CardTerminalException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        mi.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
        m.add(new MenuItem("-"));
        m.add(mi);
        mb.add(m);
        mb.add(locations_menu);
        window.setMenuBar(mb);
        window.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                try {
                    SmartCard.shutdown();
                } catch (CardTerminalException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        Container window_area = new Panel(new GridBagLayout());
        window_area.setBackground(Color.green);
        LiULogotype LiU = new LiULogotype();
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(15, 0, 0, 15);
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        window_area.add(LiU, c);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.weighty = 1.0;
        c1.fill = GridBagConstraints.BOTH;
        c1.anchor = GridBagConstraints.CENTER;
        window_area.add(location_area, c1);
        window.setLayout(new BorderLayout());
        window.add(window_area, BorderLayout.CENTER);
        try {
            SmartCard.start();
            CardTerminalRegistry registry = CardTerminalRegistry.getRegistry();
            registry.setPollInterval(200);
            JibMultiFactory factory = new JibMultiFactory();
            factory.addJiBListener(new JibMultiListener() {

                public void iButtonInserted(JibMultiEvent event) {
                    playSound("inserted.au");
                    int slot = event.getSlotID();
                    SlotChannel channel = event.getChannel();
                    iButtonCardTerminal terminal = (iButtonCardTerminal) channel.getCardTerminal();
                    int[] buttonId = terminal.getiButtonId(channel.getSlotNumber());
                    AnimalProxy re_ap = AnimalRegistry.getReusableAnimalProxy(buttonId);
                    if (re_ap == null) {
                        boolean selected = AnimalProxy.selectApplet(channel);
                        if (selected) {
                            LocationPanel lp = getSelectedLocationPanel();
                            AnimalProxy new_ap;
                            if (lp != null) new_ap = lp.getFactory().createAnimalProxy(channel, buttonId); else new_ap = new AnimalProxy(channel, buttonId);
                            addAnimal(new_ap);
                        } else {
                            System.err.println("iButton applet \"Animal\" not found");
                            playSound("failure.au");
                        }
                    } else {
                        re_ap.setChannel(channel);
                    }
                }

                public void iButtonRemoved(JibMultiEvent event) {
                    playSound("removed.au");
                }
            });
            registry.addCTListener(factory);
        } catch (CardTerminalException e) {
            e.printStackTrace();
        } catch (CardServiceException e) {
            e.printStackTrace();
        } catch (OpenCardPropertyLoadingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
