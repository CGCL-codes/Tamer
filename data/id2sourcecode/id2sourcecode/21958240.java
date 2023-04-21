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
