    public SubsList(final Composite comp) {
        subsList = new Table(comp, SWT.SINGLE);
        Font initialFont = subsList.getFont();
        FontData[] fontData = initialFont.getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setHeight(10);
            fontData[i].setStyle(SWT.BOLD);
        }
        fontBold = new Font(comp.getDisplay(), fontData);
        refresh();
        Menu popupMenu = new Menu(subsList);
        MenuItem markRead = new MenuItem(popupMenu, SWT.NONE);
        markRead.setText("Mark as read");
        MenuItem synchronize = new MenuItem(popupMenu, SWT.NONE);
        synchronize.setText("Synchronize");
        MenuItem changeTag = new MenuItem(popupMenu, SWT.NONE);
        changeTag.setText("Edit tags");
        MenuItem delete = new MenuItem(popupMenu, SWT.NONE);
        delete.setText("Delete");
        subsList.setMenu(popupMenu);
        subsList.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int indeks = subsList.getSelectionIndex();
                ItemsTable.refresh();
                if (Preview.folderPreview.getItemCount() != 0) {
                    JReader.selectChannel(indeks, Preview.folderPreview.getSelectionIndex());
                    Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
                } else {
                    JReader.addNewPreviewTab();
                    JReader.selectChannel(indeks, 0);
                    GUI.openTab(JReader.getChannel(indeks).getTitle()).refresh();
                }
            }
        });
        markRead.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                JReader.markChannelAsRead(JReader.getChannel(indeks));
                SubsList.refresh();
                ItemsTable.refresh();
                Filters.refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        synchronize.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                try {
                    JReader.updateChannel(JReader.getChannel(indeks));
                    JReader.getChannel(indeks).setFail(false);
                    GUI.statusLine.setText("Channel has been updated.");
                } catch (SAXParseException spe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "Source is not a valid XML.\n" + "Error in line " + spe.getLineNumber() + ". " + "Details:\n" + spe.getLocalizedMessage());
                    JReader.getChannel(indeks).setFail(true);
                } catch (SAXException saxe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "XML parser error has occured.");
                    JReader.getChannel(indeks).setFail(true);
                } catch (SocketException se) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + se.getLocalizedMessage());
                    JReader.getChannel(indeks).setFail(true);
                } catch (IOException ioe) {
                    GUI.statusLine.setText("Failed to update channel.");
                    errorDialog("Failed to update channel.\n" + "Unable to connect to the site.");
                    JReader.getChannel(indeks).setFail(true);
                }
                SubsList.refresh();
                ItemsTable.refresh();
                Filters.refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        changeTag.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                final int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                final Shell changeShell = new Shell(comp.getDisplay(), SWT.DIALOG_TRIM);
                changeShell.setText("Edit tags: " + subsList.getItem(indeks).getText());
                RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
                rowLayout.pack = true;
                rowLayout.justify = true;
                rowLayout.marginWidth = 40;
                rowLayout.center = true;
                rowLayout.spacing = 10;
                changeShell.setLayout(rowLayout);
                changeShell.setLocation(300, 300);
                new Label(changeShell, SWT.NONE).setText("Enter tags: ");
                final Text tags = new Text(changeShell, SWT.BORDER);
                tags.setText(JReader.getChannel(indeks).getTagsAsString());
                Button okBut = new Button(changeShell, SWT.PUSH);
                okBut.setText("OK");
                okBut.addListener(SWT.Selection, new Listener() {

                    public void handleEvent(Event event) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
                });
                tags.addListener(SWT.DefaultSelection, new Listener() {

                    public void handleEvent(Event e) {
                        JReader.editTags(JReader.getChannel(indeks), tags.getText());
                        TagList.refresh();
                        changeShell.close();
                    }
                });
                changeShell.pack();
                changeShell.open();
                TagList.refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        delete.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                int indeks = subsList.getSelectionIndex();
                if (indeks == -1) return;
                JReader.removeChannel(indeks);
                SubsList.refresh();
                ItemsTable.refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        subsList.addFocusListener(Focus.setFocus((Subscriptions.folderSubs)));
    }
