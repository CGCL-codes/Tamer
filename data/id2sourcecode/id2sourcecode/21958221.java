                public void run() {
                    if (table1 != null && !table1.isDisposed()) {
                        table1.dispose();
                    }
                    table1 = new Table(group1, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.MULTI);
                    GridData gridData = new GridData(GridData.FILL_BOTH);
                    gridData.horizontalSpan = 1;
                    gridData.verticalSpan = 5;
                    table1.setLayoutData(gridData);
                    table1.setHeaderVisible(true);
                    final TableColumn number = new TableColumn(table1, SWT.CENTER);
                    number.setText("#");
                    number.setWidth(50);
                    final TableColumn date = new TableColumn(table1, SWT.LEFT);
                    date.setText("Time");
                    date.setWidth(130);
                    final TableColumn peer_IP = new TableColumn(table1, SWT.LEFT);
                    peer_IP.setText("IP");
                    peer_IP.setWidth(100);
                    final TableColumn peer_clientName = new TableColumn(table1, SWT.LEFT);
                    peer_clientName.setText("Client");
                    peer_clientName.setWidth(150);
                    final TableColumn peer_ID = new TableColumn(table1, SWT.LEFT);
                    peer_ID.setText("ID");
                    peer_ID.setWidth(100);
                    final TableColumn download = new TableColumn(table1, SWT.LEFT);
                    download.setText("Torrent");
                    download.setWidth(200);
                    try {
                        HashMap map = table1ColumnWidthUtility.getMap();
                        if (map != null) {
                            TableColumn[] columns = table1.getColumns();
                            for (int i = 0; i < columns.length; i++) {
                                String value = (String) map.get(columns[i].getText());
                                if (value != null) {
                                    int tempNum = Integer.parseInt((String) map.get(columns[i].getText()));
                                    if (tempNum > 0) columns[i].setWidth(tempNum);
                                } else {
                                    if (columns[i].getText().equalsIgnoreCase("Filter")) {
                                        columns[i].setWidth(200);
                                    } else {
                                        columns[i].pack();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T1_Numbers", true)) {
                        number.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T1_Time", true)) {
                        date.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T1_Client", true)) {
                        peer_clientName.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T1_Client_id", false)) {
                        peer_ID.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T1_Torrent", true)) {
                        download.dispose();
                    }
                    Plugin.getDisplay().syncExec(new Runnable() {

                        public void run() {
                            if (table1.isDisposed()) return;
                            table1.setItemCount(Plugin.table1_set.getNum());
                            table1.clearAll();
                        }
                    });
                    TableColumn[] table_columns = table1.getColumns();
                    for (int i = 0; i < table_columns.length; i++) {
                        if (table_columns[i].getText().equalsIgnoreCase("#")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortByIndex(true));
                        } else if (table_columns[i].getText().equalsIgnoreCase("IP")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortByIP(true));
                        } else if (table_columns[i].getText().equalsIgnoreCase("Time")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortTable1ByDate());
                        } else if (table_columns[i].getText().equalsIgnoreCase("Torrent")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortTable1ByTorrent());
                        } else if (table_columns[i].getText().equalsIgnoreCase("Client")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortTable1ByClient());
                        } else if (table_columns[i].getText().equalsIgnoreCase("ID")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table1_set.sortTable1ByID());
                        }
                        table_columns[i].addControlListener(getResizeListener(1));
                    }
                    table1.addListener(SWT.SetData, new Listener() {

                        public void handleEvent(Event e) {
                            try {
                                TableItem item = (TableItem) e.item;
                                int index = table1.indexOf(item);
                                if (index % 2 == 0) {
                                    item.setBackground(ColorUtilities.getBackgroundColor());
                                }
                                if (table1 == null || table1.isDisposed()) return;
                                TableColumn[] columns = table1.getColumns();
                                String[] stringItems;
                                try {
                                    stringItems = Plugin.table1_set.getTable1ContainerArray()[index].getTableItemsAsString();
                                } catch (ArrayIndexOutOfBoundsException e2) {
                                    return;
                                }
                                for (int i = 0; i < columns.length; i++) {
                                    String columnName = columns[i].getText();
                                    if (columnName.equalsIgnoreCase("#")) {
                                        item.setText(i, stringItems[0]);
                                    } else if (columnName.equalsIgnoreCase("Time")) {
                                        item.setText(i, stringItems[1]);
                                    } else if (columnName.equalsIgnoreCase("IP")) {
                                        item.setText(i, stringItems[2]);
                                    } else if (columnName.equalsIgnoreCase("Client")) {
                                        item.setText(i, stringItems[3]);
                                    } else if (columnName.equalsIgnoreCase("ID")) {
                                        item.setText(i, stringItems[4]);
                                    } else if (columnName.equalsIgnoreCase("Torrent")) {
                                        item.setText(i, stringItems[5]);
                                    }
                                }
                                Color temp_color = new Color(Plugin.getDisplay(), Utils.getRGB(stringItems[6]));
                                item.setForeground(temp_color);
                                temp_color.dispose();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    table1.addMouseListener(new MouseAdapter() {

                        public void mouseDown(MouseEvent e) {
                            if (e.button == 1) {
                                if (table1.getItem(new Point(e.x, e.y)) == null) {
                                    table1.deselectAll();
                                }
                            }
                        }
                    });
                    Menu popupmenu_table = new Menu(group1);
                    final MenuItem dump = new MenuItem(popupmenu_table, SWT.PUSH);
                    dump.setText("Save table contents to a file");
                    dump.setEnabled(false);
                    dump.addListener(SWT.Selection, new Listener() {

                        public void handleEvent(Event e) {
                            try {
                                TableItem[] items = table1.getItems();
                                if (items.length == 0) {
                                    MessageBox messageBox = new MessageBox(peerRemoveGroup.getShell(), SWT.ICON_ERROR | SWT.OK);
                                    messageBox.setText("Table is Empty");
                                    messageBox.setMessage("The table is empty, therefore nothing can be written to a file.");
                                    messageBox.open();
                                    return;
                                }
                                FileDialog fileDialog = new FileDialog(group1.getShell(), SWT.SAVE);
                                fileDialog.setText("Please choose a file to save the information to");
                                String[] filterExtensions = { "*.txt", "*.log", "*.*" };
                                fileDialog.setFilterExtensions(filterExtensions);
                                if (defaultPath == null) {
                                    defaultPath = Plugin.getPluginInterface().getPluginDirectoryName();
                                }
                                fileDialog.setFilterPath(defaultPath);
                                String selectedFile = fileDialog.open();
                                if (selectedFile != null) {
                                    final File fileToSave = new File(selectedFile);
                                    defaultPath = fileToSave.getParent();
                                    if (fileToSave.exists()) {
                                        if (!fileToSave.canWrite()) {
                                            MessageBox messageBox = new MessageBox(group1.getShell(), SWT.ICON_ERROR | SWT.OK);
                                            messageBox.setText("Error writing to file");
                                            messageBox.setMessage("Your computer is reporting that the selected file cannot be written to, please retry this operation and select a different file");
                                            messageBox.open();
                                            return;
                                        }
                                        final Shell shell = new Shell(SWT.DIALOG_TRIM);
                                        shell.setLayout(new GridLayout(3, false));
                                        shell.setText("File Exists");
                                        Label message = new Label(shell, SWT.NULL);
                                        message.setText("Your selected file already exists. \n" + "Choose 'Overwrite' to overwrite it, deleting the original contents \n" + "Choose 'Append' to append the information to the existing file \n" + "Choose 'Cancel' to abort this action all together\n\n");
                                        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
                                        gridData.horizontalSpan = 3;
                                        message.setLayoutData(gridData);
                                        Button overwrite = new Button(shell, SWT.PUSH);
                                        overwrite.setText("Overwrite");
                                        overwrite.addListener(SWT.Selection, new Listener() {

                                            public void handleEvent(Event e) {
                                                shell.close();
                                                shell.dispose();
                                                FileUtilities.writeToLog(Plugin.table1_set, fileToSave, false);
                                            }
                                        });
                                        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
                                        overwrite.setLayoutData(gridData);
                                        Button append = new Button(shell, SWT.PUSH);
                                        append.setText("Append");
                                        append.addListener(SWT.Selection, new Listener() {

                                            public void handleEvent(Event e) {
                                                shell.close();
                                                shell.dispose();
                                                FileUtilities.writeToLog(Plugin.table1_set, fileToSave, true);
                                            }
                                        });
                                        Button cancel = new Button(shell, SWT.PUSH);
                                        cancel.setText("Cancel");
                                        cancel.addListener(SWT.Selection, new Listener() {

                                            public void handleEvent(Event e) {
                                                shell.close();
                                                shell.dispose();
                                            }
                                        });
                                        gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
                                        cancel.setLayoutData(gridData);
                                        overwrite.addKeyListener(new KeyListener() {

                                            public void keyPressed(KeyEvent e) {
                                                switch(e.character) {
                                                    case SWT.ESC:
                                                        escPressed = 1;
                                                        break;
                                                }
                                            }

                                            public void keyReleased(KeyEvent e) {
                                                if (escPressed == 1) {
                                                    escPressed = 0;
                                                    shell.close();
                                                    shell.dispose();
                                                }
                                            }
                                        });
                                        Utils.centerShellandOpen(shell);
                                    } else {
                                        fileToSave.createNewFile();
                                        FileUtilities.writeToLog(Plugin.table1_set, fileToSave, true);
                                    }
                                }
                            } catch (Exception f) {
                                f.printStackTrace();
                                MessageBox messageBox = new MessageBox(group1.getShell(), SWT.ICON_ERROR | SWT.OK);
                                messageBox.setText("Error writing to file");
                                messageBox.setMessage("Your computer is reporting that the selected file cannot be written to, please retry this operation and select a different file");
                                messageBox.open();
                            }
                        }
                    });
                    final MenuItem copyClip = new MenuItem(popupmenu_table, SWT.PUSH);
                    copyClip.setText("Copy selected line(s) to clipboard");
                    copyClip.setEnabled(false);
                    copyClip.addListener(SWT.Selection, new Listener() {

                        public void handleEvent(Event e) {
                            try {
                                String item_text = new String();
                                TableItem[] item = table1.getSelection();
                                int[] indices = table1.getSelectionIndices();
                                if (item.length == 0) {
                                    return;
                                } else if (item.length > 0) {
                                    for (int i = 0; i < item.length; i++) {
                                        String[] itemString = Plugin.table1_set.getTable1ContainerArray()[indices[i]].getTableItemsAsString();
                                        for (int j = 0; j < 6; j++) {
                                            item_text = item_text + " | " + itemString[j];
                                        }
                                        item_text = item_text + "\n";
                                    }
                                }
                                Plugin.getPluginInterface().getUIManager().copyToClipBoard(item_text);
                            } catch (UIException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    MenuItem seperator = new MenuItem(popupmenu_table, SWT.SEPARATOR);
                    seperator.setText("null");
                    MenuItem setup = new MenuItem(popupmenu_table, SWT.PUSH);
                    setup.setText("Table Setup");
                    setup.addListener(SWT.Selection, new Listener() {

                        public void handleEvent(Event e) {
                            try {
                                Tab1Customization tab1cust = new Tab1Customization();
                                tab1cust.clientBlockingCustomizationOpen();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    table1.setMenu(popupmenu_table);
                    popupmenu_table.addMenuListener(new MenuListener() {

                        public void menuHidden(MenuEvent arg0) {
                        }

                        public void menuShown(MenuEvent arg0) {
                            dump.setEnabled(false);
                            copyClip.setEnabled(false);
                            TableItem[] item = table1.getSelection();
                            if (item.length > 0) {
                                copyClip.setEnabled(true);
                            }
                            if (table1.getItemCount() > 0) {
                                dump.setEnabled(true);
                            }
                        }
                    });
                    group1.layout();
                }
