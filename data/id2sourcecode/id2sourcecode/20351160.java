            public void run() {
                ItemsTable.itemsTable.removeAll();
                Font initialFont = itemsTable.getFont();
                FontData[] fontData = initialFont.getFontData();
                for (int i = 0; i < fontData.length; i++) {
                    fontData[i].setStyle(SWT.BOLD);
                }
                Font fontBold = new Font(Items.tableComposite.getDisplay(), fontData);
                int index = 0;
                for (Item it : JReader.getItems()) {
                    TableItem item = new TableItem(ItemsTable.itemsTable, SWT.NONE);
                    if (JReader.getChannel(it.getChannelId()).getIconPath() == null) if (it.isRead()) item.setImage(read); else item.setImage(unread); else {
                        try {
                            ImageData imData = new ImageData(JReader.getChannel(it.getChannelId()).getIconPath());
                            imData = imData.scaledTo(16, 16);
                            item.setImage(new Image(Items.tableComposite.getDisplay(), imData));
                        } catch (SWTException swte) {
                            if (it.isRead()) item.setImage(read); else item.setImage(unread);
                        }
                    }
                    item.setText(0, (it.getTitle() != null ? it.getTitle() : "brak tytułu"));
                    item.setText(1, GUI.shortDateFormat.format(it.getDate()));
                    if (!it.isRead()) {
                        item.setFont(fontBold);
                    }
                    if (!System.getProperty("os.name").equalsIgnoreCase("Linux")) {
                        if (index % 2 == 0) {
                            item.setBackground(GUI.gray);
                        } else item.setBackground(GUI.white);
                    }
                    index++;
                }
            }
