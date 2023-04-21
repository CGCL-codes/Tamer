    JMenuBar createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu, menu2;
        JMenuItem item;
        menubar.add(menu = new JMenu(AddressBookResources.MENU_FILE));
        createNewMenu(menu, this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_PROPERTIES));
        item.addActionListener(this);
        menu.add(item = new JMenuItem(AddressBookResources.MENU_DELETE));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(menu2 = new JMenu(AddressBookResources.MENU_IMPORT));
        menu2.add(item = new JMenuItem(AddressBookResources.MENU_ADDRBOOK));
        menu2.add(item = new JMenuItem(AddressBookResources.MENU_BUSCARD));
        menu2.add(new AbstractAction(AddressBookResources.MENU_OTHERADDRBOOK) {

            public void actionPerformed(ActionEvent ae) {
                Object importType = JOptionPane.showInputDialog(AddressBookFrame.this, AddressBookResources.LABEL_SELECT_IMPORT_FORMAT, AddressBookResources.TITLE_IMPORT_TYPE, JOptionPane.QUESTION_MESSAGE, null, AddressBookResources.LABELS_IMP_EXP_FMT_NAME, "Outlook .CSV format");
                if ("Outlook .CSV format".equals(importType)) {
                    JFileChooser chooser = new JFileChooser() {

                        public boolean accept(File f) {
                            return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                        }
                    };
                    if (chooser.CANCEL_OPTION != chooser.showOpenDialog(AddressBookFrame.this)) {
                        InputStream fis = null;
                        try {
                            if (folders == null) folders = new ArrayList();
                            if (folders.size() == 0) folders.add(new Folder<Contact>(AddressBookResources.LABEL_PERSONS));
                            Folder contacts = (Folder) folders.get(0);
                            Csv csv = new Csv(fis = new FileInputStream(chooser.getSelectedFile()), false, "utf-8", ',', true);
                            Map header = csv.getMetaData();
                            int[] names = new int[4];
                            Arrays.fill(names, 0, names.length - 1, -1);
                            int titleIdx = -1;
                            for (Object fn : header.keySet()) {
                                String s = ((String) fn).toLowerCase();
                                if (s.indexOf("name") >= 0) {
                                    if (s.indexOf("last") >= 0) names[1] = (Integer) header.get(fn); else if (s.indexOf("first") >= 0) names[0] = (Integer) header.get(fn); else if (s.indexOf("middle") >= 0) names[2] = (Integer) header.get(fn); else if (names[0] < 0) names[0] = (Integer) header.get(fn);
                                } else if (s.indexOf("suff") >= 0) names[3] = (Integer) header.get(fn); else if (s.indexOf("title") >= 0) titleIdx = (Integer) header.get(fn);
                            }
                            while (csv.next()) {
                                try {
                                    Contact c = new Contact(new Name((names[0] > 0 ? csv.getString(names[0]) : "") + " " + (names[1] > 0 ? csv.getString(names[1]) : "") + ", " + (names[2] > 0 ? csv.getString(names[2]) : "") + (names[3] > 0 ? "(" + csv.getString(names[0]) + ")" : "")));
                                    if (titleIdx > 0) c.setTitle(csv.getString(titleIdx));
                                    for (String fn : (Set<String>) header.keySet()) {
                                        String s = fn.toLowerCase();
                                        if (s.indexOf("e-mail") >= 0) {
                                            String e = csv.getString(fn);
                                            if (e != null) {
                                                String dn = fn;
                                                try {
                                                    dn = csv.getString(fn + " Display Name");
                                                } catch (IOException ioe) {
                                                }
                                                c.add(new EMail(e, dn, s));
                                            }
                                        } else if (s.indexOf("phone") >= 0 || s.indexOf("fax") >= 0 || s.indexOf("pager") >= 0) {
                                            String n = csv.getString(fn);
                                            if (n != null) c.add(new Telephone(n, fn, s));
                                        } else if (s.indexOf("address") >= 0 && s.indexOf("e-mail") < 0) {
                                            String a = csv.getString(fn);
                                            if (a != null) c.add(new Address(a, fn, s));
                                        } else if (s.indexOf("street") >= 0) {
                                            String pr = fn.substring(0, s.indexOf("street"));
                                            String a = csv.getString(pr + "Street");
                                            if (a != null && a.length() > 0) {
                                                a += csv.getString(pr + "Street 2") + csv.getString(pr + "Street 3") + '\n' + csv.getString(pr + "City") + ',' + csv.getString(pr + "Postal Code") + '\n' + csv.getString(pr + "Country");
                                                c.add(new Address(a, fn, s));
                                            }
                                        } else if (s.indexOf("web") >= 0) {
                                            String l = csv.getString(fn);
                                            if (l != null) c.add(new Link(l, fn));
                                        } else if (s.indexOf("birthday") >= 0) {
                                            String b = csv.getString(fn);
                                            if (b != null) try {
                                                c.setDOB(new SimpleDateFormat("MM/dd/yy").parse(b));
                                            } catch (ParseException pe) {
                                            }
                                        }
                                    }
                                    contacts.add(c);
                                } catch (java.text.ParseException pe) {
                                    System.err.println("Couldn't parse");
                                }
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } finally {
                            try {
                                fis.close();
                            } catch (Exception e) {
                            }
                        }
                        tree.setModel(new DefaultTreeModel(createTreeModel(folders), false));
                    }
                }
            }
        });
        menu.add(menu2 = new JMenu(AddressBookResources.MENU_EXPORT));
        menu2.add(new AbstractAction(AddressBookResources.MENU_ADDRBOOK) {

            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser() {

                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
                    }
                };
                if (chooser.CANCEL_OPTION != chooser.showOpenDialog(AddressBookFrame.this)) {
                }
            }
        });
        menu2.add(item = new JMenuItem(AddressBookResources.MENU_BUSCARD));
        menu2.add(new AbstractAction(AddressBookResources.MENU_IPODCONTACTS) {

            public void actionPerformed(ActionEvent a) {
                OutputStream out = null;
                try {
                    Serializer s = controller.getSerializer();
                    boolean locAcc = s.getInt(s.getProperty(OptionsFrame.OPTIONS, OptionsFrame.ACCESS), 0) == 0;
                    if (locAcc) {
                        String loc = (String) s.getProperty("IpodOptionsTab", OptionsFrame.IPOD_DEVICE);
                        if (loc != null && loc.length() > 0) {
                            Folder f = (Folder) folders.get(0);
                            List<XMLSaver> l = f.getContent();
                            for (XMLSaver saver : l) {
                                try {
                                    out = new FileOutputStream(loc + "/Contacts/" + saver + ".vcf");
                                    saver.saveVCard(out, "UTF-8", 0);
                                } finally {
                                    if (out != null) try {
                                        out.close();
                                    } catch (IOException ioe) {
                                    }
                                    out = null;
                                }
                            }
                        } else {
                            JFileChooser fc = new JFileChooser();
                            fc.setDialogType(JFileChooser.SAVE_DIALOG);
                            fc.showSaveDialog(controller.getMainFrame());
                            File targetPath = fc.getSelectedFile();
                            if (targetPath != null) out = new FileOutputStream(targetPath);
                            ((XMLSaver) folders.get(0)).saveVCard(out, "UTF-8", 0);
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    if (out != null) try {
                        out.close();
                    } catch (IOException ioe) {
                    }
                }
            }
        });
        menu2.add(new AbstractAction(AddressBookResources.MENU_OTHERADDRBOOK) {

            public void actionPerformed(ActionEvent ae) {
                CSVOptionPanel.doExport((Folder) folders.get(0), AddressBookFrame.this);
            }
        });
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_PRINT));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(standalone ? AddressBookResources.MENU_EXIT : AddressBookResources.MENU_CLOSE));
        item.addActionListener(this);
        menubar.add(menu = new JMenu(AddressBookResources.MENU_EDIT));
        menu.add(item = new JMenuItem(AddressBookResources.MENU_COPY));
        item.setAccelerator(KeyStroke.getKeyStroke("control C"));
        item.addActionListener(this);
        menu.add(item = new JMenuItem(AddressBookResources.MENU_PASTE));
        item.setAccelerator(KeyStroke.getKeyStroke("control V"));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_SELECTALL));
        item.setAccelerator(KeyStroke.getKeyStroke("control A"));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_PROFILE));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_FINDPEOPLE));
        item.setAccelerator(KeyStroke.getKeyStroke("control F"));
        item.addActionListener(this);
        menubar.add(menu = new JMenu(AddressBookResources.MENU_VIEW));
        menu.add(m_toolBar = new JCheckBoxMenuItem(AddressBookResources.MENU_TOOLBAR));
        m_toolBar.addActionListener(this);
        m_toolBar.setSelected((view & TOOL) != 0);
        menu.add(m_statusBar = new JCheckBoxMenuItem(AddressBookResources.MENU_STATUSBAR));
        m_statusBar.addActionListener(this);
        m_statusBar.setSelected((view & STATUS) != 0);
        menu.add(m_folder = new JCheckBoxMenuItem(AddressBookResources.MENU_FOLDERGROUP));
        m_folder.addActionListener(this);
        m_folder.setSelected((view & FOLDER) != 0);
        menu.addSeparator();
        menu.add(menu2 = new JMenu(AddressBookResources.MENU_SORTBY));
        ButtonGroup bg = new ButtonGroup();
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_NAME)));
        bg.add(item);
        item.setSelected(BaseAttrTableModel.NAME_SORT == sortField);
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_EMAILADDR)));
        bg.add(item);
        item.setSelected(BaseAttrTableModel.E_MAIL_SORT == sortField);
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_PHONE)));
        bg.add(item);
        item.setSelected(BaseAttrTableModel.TPHONE_SORT == sortField);
        menu2.addSeparator();
        bg = new ButtonGroup();
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_FIRSTNAME)));
        bg.add(item);
        item.setSelected(BaseAttrTableModel.FIRSTNAME_SORT == subSortField);
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_LASTNAME)));
        bg.add(item);
        item.setSelected(BaseAttrTableModel.LASTNAME_SORT == subSortField);
        menu2.addSeparator();
        bg = new ButtonGroup();
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_ASC)));
        bg.add(item);
        item.setSelected(sortDir);
        menu2.add(item = new JRadioButtonMenuItemEx(new RadioAction(AddressBookResources.MENU_DESC)));
        bg.add(item);
        item.setSelected(sortDir);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_REFRESH));
        item.setAccelerator(KeyStroke.getKeyStroke("F5"));
        item.addActionListener(this);
        menubar.add(menu = new JMenu(AddressBookResources.MENU_TOOLS));
        menu.add(item = new JMenuItem(AddressBookResources.MENU_ACCOUNTS));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_OPTIONS));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(menu2 = new JMenu(AddressBookResources.MENU_ACTION));
        createActionMenu(menu2, this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_SYNCHRONIZE));
        item.addActionListener(this);
        menubar.add(menu = new JMenu(AddressBookResources.MENU_HELP));
        menu.add(item = new JMenuItem(AddressBookResources.MENU_CONTENTS));
        item.setAccelerator(KeyStroke.getKeyStroke("F1"));
        item.addActionListener(this);
        menu.addSeparator();
        menu.add(item = new JMenuItem(AddressBookResources.MENU_ABOUT + AddressBookFrame.PROGRAMNAME));
        item.addActionListener(this);
        return menubar;
    }
