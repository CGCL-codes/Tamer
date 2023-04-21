            public void handleEvent(Event e) {
                TableItem[] item = itemsTable.getSelection();
                Font initialFont = item[0].getFont();
                FontData[] fontData = initialFont.getFontData();
                if (fontData[0].getStyle() != SWT.NORMAL) {
                    for (int i = 0; i < fontData.length; i++) {
                        fontData[i].setStyle(SWT.NORMAL);
                    }
                    Font newFont = new Font(comp.getDisplay(), fontData);
                    item[0].setFont(newFont);
                    Item it = JReader.getItems().get(itemsTable.getSelectionIndex());
                    if (JReader.getChannel(it.getChannelId()).getIconPath() == null) item[0].setImage(read);
                }
                if (Preview.folderPreview.getItemCount() != 0) {
                    JReader.selectItem(JReader.getItems().get(itemsTable.getSelectionIndex()), Preview.folderPreview.getSelectionIndex());
                    Preview.previewItemList.get(Preview.folderPreview.getSelectionIndex()).refresh();
                } else {
                    JReader.addNewPreviewTab();
                    JReader.selectItem(JReader.getItems().get(itemsTable.getSelectionIndex()), 0);
                    GUI.openTab(item[0].getText()).refresh();
                }
                SubsList.refresh();
                Filters.refresh();
            }
