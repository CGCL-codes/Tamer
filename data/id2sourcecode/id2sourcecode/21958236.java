                public void run() {
                    if (peer_remove != null && !peer_remove.isDisposed()) {
                        peer_remove.dispose();
                    }
                    peer_remove = new Table(peerRemoveGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.MULTI);
                    GridData gridData = new GridData(GridData.FILL_BOTH);
                    gridData.horizontalSpan = 1;
                    gridData.verticalSpan = 5;
                    peer_remove.setLayoutData(gridData);
                    peer_remove.setHeaderVisible(true);
                    final TableColumn number = new TableColumn(peer_remove, SWT.CENTER);
                    number.setText("#");
                    number.setWidth(50);
                    final TableColumn date = new TableColumn(peer_remove, SWT.LEFT);
                    date.setText("Time");
                    date.setWidth(130);
                    final TableColumn type = new TableColumn(peer_remove, SWT.LEFT);
                    type.setText("Type");
                    type.setWidth(100);
                    final TableColumn peer_clientName = new TableColumn(peer_remove, SWT.LEFT);
                    peer_clientName.setText("Client");
                    peer_clientName.setWidth(150);
                    final TableColumn peer_IP = new TableColumn(peer_remove, SWT.LEFT);
                    peer_IP.setText("IP");
                    peer_IP.setWidth(100);
                    try {
                        HashMap map = table2ColumnWidthUtility.getMap();
                        if (map != null) {
                            TableColumn[] columns = peer_remove.getColumns();
                            for (int i = 0; i < columns.length; i++) {
                                String value = (String) map.get(columns[i].getText());
                                if (value != null) {
                                    int tempNum = Integer.parseInt((String) map.get(columns[i].getText()));
                                    if (tempNum > 0) columns[i].setWidth(tempNum);
                                } else {
                                    columns[i].setWidth(150);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T2_Numbers", true)) {
                        number.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T2_Time", true)) {
                        date.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T2_Manual", true)) {
                        type.dispose();
                    }
                    if (!Plugin.getPluginInterface().getPluginconfig().getPluginBooleanParameter("Stuffer_T2_Client_ip", true)) {
                        peer_IP.dispose();
                    }
                    Plugin.getDisplay().syncExec(new Runnable() {

                        public void run() {
                            if (peer_remove.isDisposed()) return;
                            peer_remove.setItemCount(Plugin.table2_set.getNum());
                            peer_remove.clearAll();
                        }
                    });
                    TableColumn[] table_columns = peer_remove.getColumns();
                    for (int i = 0; i < table_columns.length; i++) {
                        if (table_columns[i].getText().equalsIgnoreCase("#")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table2_set.sortByIndex(false));
                        } else if (table_columns[i].getText().equalsIgnoreCase("IP")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table2_set.sortByIP(false));
                        } else if (table_columns[i].getText().equalsIgnoreCase("Time")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table2_set.sortTable2ByDate());
                        } else if (table_columns[i].getText().equalsIgnoreCase("Type")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table2_set.sortTable2ByType());
                        } else if (table_columns[i].getText().equalsIgnoreCase("Client")) {
                            table_columns[i].addListener(SWT.Selection, Plugin.table2_set.sortTable2ByClient());
                        }
                        table_columns[i].addControlListener(getResizeListener(2));
                    }
                    peer_remove.addMouseListener(new MouseAdapter() {

                        public void mouseDown(MouseEvent e) {
                            if (e.button == 1) {
                                if (peer_remove.getItem(new Point(e.x, e.y)) == null) {
                                    peer_remove.deselectAll();
                                }
                            }
                        }
                    });
                    peer_remove.addListener(SWT.SetData, new Listener() {

                        public void handleEvent(Event e) {
                            TableItem item = (TableItem) e.item;
                            int index = peer_remove.indexOf(item);
                            if (index % 2 == 0) {
                                item.setBackground(ColorUtilities.getBackgroundColor());
                            }
                            TableColumn[] columns = peer_remove.getColumns();
                            String[] itemString;
                            try {
                                itemString = Plugin.table2_set.getTable2ContainerArray()[index].getTableItemsAsString();
                            } catch (ArrayIndexOutOfBoundsException e2) {
                                return;
                            }
                            for (int i = 0; i < columns.length; i++) {
                                if (columns[i].getText().equalsIgnoreCase("#")) {
                                    item.setText(i, itemString[0]);
                                } else if (columns[i].getText().equalsIgnoreCase("Time")) {
                                    item.setText(i, itemString[1]);
                                } else if (columns[i].getText().equalsIgnoreCase("Type")) {
                                    item.setText(i, itemString[2]);
                                } else if (columns[i].getText().equalsIgnoreCase("Client")) {
                                    item.setText(i, itemString[3]);
                                } else if (columns[i].getText().equalsIgnoreCase("IP")) {
                                    item.setText(i, itemString[4]);
                                }
                            }
                            Color temp_color = new Color(Plugin.getDisplay(), Utils.getRGBfromHex(itemString[5]));
                            item.setForeground(temp_color);
                            temp_color.dispose();
                        }
                    });
                    Menu popupmenu_table = new Menu(peerRemoveGroup);
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
                                                FileUtilities.writeToLog(Plugin.table2_set, fileToSave, false);
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
                                                FileUtilities.writeToLog(Plugin.table2_set, fileToSave, true);
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
                                        FileUtilities.writeToLog(Plugin.table2_set, fileToSave, true);
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
                                TableItem[] item = peer_remove.getSelection();
                                int[] indices = peer_remove.getSelectionIndices();
                                if (item.length == 0) {
                                    return;
                                } else if (item.length > 0) {
                                    for (int i = 0; i < item.length; i++) {
                                        String[] itemString = Plugin.table2_set.getTable2ContainerArray()[indices[i]].getTableItemsAsString();
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
                                tab1cust.ipFilterRemovalInformationCustomizationOpen();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    peer_remove.setMenu(popupmenu_table);
                    popupmenu_table.addMenuListener(new MenuListener() {

                        public void menuHidden(MenuEvent arg0) {
                        }

                        public void menuShown(MenuEvent arg0) {
                            dump.setEnabled(false);
                            copyClip.setEnabled(false);
                            TableItem[] item = peer_remove.getSelection();
                            if (item.length > 0) {
                                copyClip.setEnabled(true);
                            }
                            if (peer_remove.getItemCount() > 0) {
                                dump.setEnabled(true);
                            }
                        }
                    });
                    peerRemoveGroup.layout();
                }
