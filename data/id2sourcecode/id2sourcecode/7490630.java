    public PeripheralPanel(PeripheralBearer pTarget) {
        super(new BorderLayout());
        target = pTarget;
        peripheralList = new ModuleList();
        peripheralList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        peripheralList.setListData(target.getPeripherals().toArray());
        ((Module) target).addPropertyListener(PeripheralBearer.class, this);
        peripheralList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent lse) {
                if (peripheralList.getSelectedValue() != null) {
                    propertiesButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    propertiesButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });
        peripheralList.setTransferHandler(new TransferHandler() {

            public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                for (int i = 0; i < transferFlavors.length; i++) {
                    if (transferFlavors[i].equals(TransferableModule.prototypeFlavor) || transferFlavors[i].equals(TransferableResourceDescriptor.descriptorFlavor)) {
                        return true;
                    }
                }
                return false;
            }

            protected Transferable createTransferable(JComponent c) {
                Object obj = peripheralList.getSelectedValue();
                if (obj != null) {
                    return new TransferableModule((Module) obj);
                } else {
                    return null;
                }
            }

            protected void exportDone(JComponent source, Transferable data, int action) {
                if (action == MOVE) {
                    Module m = ((TransferableModule) data).getModule();
                    m.getContainer().deregisterModule(m);
                }
            }

            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY_OR_MOVE;
            }

            public boolean importData(JComponent comp, Transferable t) {
                try {
                    ModuleContainer mc = ((Module) target).getContainer();
                    if (t.isDataFlavorSupported(TransferableModule.prototypeFlavor)) {
                        InputStream is = (InputStream) t.getTransferData(TransferableModule.prototypeFlavor);
                        Module m = mc.createModule(null, is);
                        ((EntityPeripheral) m).setTarget((Module) target);
                        return true;
                    } else if (t.isDataFlavorSupported(TransferableResourceDescriptor.descriptorFlavor)) {
                        ResourceDescriptor rd = (ResourceDescriptor) t.getTransferData(TransferableResourceDescriptor.descriptorFlavor);
                        if (rd instanceof ModuleClassDescriptor) {
                            Module m = mc.newModule((ModuleClassDescriptor) rd);
                            mc.registerModule(m);
                            ((EntityPeripheral) m).setTarget((Module) target);
                            return true;
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PeripheralPanel.this, e, "Error Transferring Peripheral", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
        });
        peripheralList.setBorder(new EtchedBorder());
        add(peripheralList, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton newButton = new JButton("New...");
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Window win = SwingUtilities.getWindowAncestor(PeripheralPanel.this);
                URLChooser uc;
                if (win instanceof Frame) {
                    uc = new URLChooser((Frame) win);
                } else {
                    uc = new URLChooser((Dialog) win);
                }
                JButton src = (JButton) ae.getSource();
                GUIUtilities.positionDialog(src, new Point(src.getWidth() / 2, src.getHeight() / 2), uc);
                uc.setTitle("New Peripheral");
                uc.setLabelText("  Class/Prototype URL:  ");
                uc.setFileFilter(new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File f) {
                        return f.getName().endsWith(".xml") || f.isDirectory();
                    }

                    public String getDescription() {
                        return "XML Prototype Files";
                    }
                });
                uc.setResourceFilter(new ResourceFilter() {

                    public boolean accept(ResourceDescriptor rd) {
                        if (rd.getType().equals(ResourceDescriptor.NPSNETV_PROTOTYPE)) {
                            return true;
                        } else if (rd instanceof ModuleClassDescriptor) {
                            String[] properties = ((ModuleClassDescriptor) rd).getProperties();
                            for (int i = 0; i < properties.length; i++) {
                                if (properties[i].equals(EntityPeripheral.class.getName())) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                if (uc.showDialog(null) && uc.getSelectedURL() != null) {
                    URL url = uc.getSelectedURL();
                    try {
                        ModuleContainer mc = ((Module) target).getContainer();
                        EntityPeripheral ep;
                        if (url.getProtocol().equals("resource") && url.getPath().endsWith(".class")) {
                            ModuleClassDescriptor mcd = NewModuleItemFactory.getModuleClassDescriptor(url);
                            if (mcd != null) {
                                Module m = mc.newModule(mcd);
                                mc.registerModule(m);
                                ep = (EntityPeripheral) m;
                            } else {
                                throw new Exception("Module class " + url + " not found");
                            }
                        } else {
                            ep = (EntityPeripheral) mc.createModule(url);
                        }
                        ep.setTarget((Module) target);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(PeripheralPanel.this, e, "Error Creating Peripheral", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        propertiesButton = new JButton("Properties...");
        propertiesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Module m = (Module) peripheralList.getSelectedValue();
                ModulePropertiesDialog mpd;
                Window win = SwingUtilities.windowForComponent(PeripheralPanel.this);
                if (win instanceof Frame) {
                    mpd = new ModulePropertiesDialog((Frame) win, m);
                } else {
                    mpd = new ModulePropertiesDialog((Dialog) win, m);
                }
                mpd.setLocation(PeripheralPanel.this.getLocationOnScreen().x + 16, PeripheralPanel.this.getLocationOnScreen().y + 16);
                mpd.show();
            }
        });
        propertiesButton.setEnabled(false);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Module m = (Module) peripheralList.getSelectedValue();
                m.getContainer().deregisterModule(m);
            }
        });
        deleteButton.setEnabled(false);
        buttonPanel.add(newButton);
        buttonPanel.add(propertiesButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
        setBorder(new TitledBorder("Peripherals"));
    }
