    public ItemsTable(final Composite comp) {
        unread = new Image(comp.getDisplay(), "data" + File.separator + "icons" + File.separator + "unread.png");
        read = new Image(comp.getDisplay(), "data" + File.separator + "icons" + File.separator + "read.png");
        itemsTable = new Table(comp, SWT.SINGLE | SWT.FULL_SELECTION);
        itemsTable.setLinesVisible(true);
        itemsTable.setHeaderVisible(true);
        final TableColumn column1 = new TableColumn(itemsTable, SWT.NONE);
        column1.setText(titles[0]);
        final TableColumn column2 = new TableColumn(itemsTable, SWT.NONE);
        column2.setText(titles[1]);
        Menu popupMenu = new Menu(itemsTable);
        MenuItem openNewTab = new MenuItem(popupMenu, SWT.NONE);
        openNewTab.setText("Open item in a new tab");
        MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
        deleteItem.setText("Delete item");
        itemsTable.setMenu(popupMenu);
        refresh();
        comp.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                Rectangle area = comp.getClientArea();
                Point preferredSize = itemsTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                int width = area.width - 2 * itemsTable.getBorderWidth();
                if (preferredSize.y > area.height + itemsTable.getHeaderHeight()) {
                    Point vBarSize = itemsTable.getVerticalBar().getSize();
                    width -= vBarSize.x;
                }
                Point oldSize = itemsTable.getSize();
                if (oldSize.x > area.width) {
                    column1.setWidth((width / 3) * 2);
                    column2.setWidth(width - column1.getWidth());
                    itemsTable.setSize(area.width, area.height);
                } else {
                    itemsTable.setSize(area.width, area.height);
                    column1.setWidth((width / 3) * 2);
                    column2.setWidth(width - column1.getWidth());
                }
            }
        });
        itemsTable.addListener(SWT.Selection, new Listener() {

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
        });
        itemsTable.addListener(SWT.EraseItem, new Listener() {

            public void handleEvent(Event event) {
                if ((event.detail & SWT.SELECTED) != 0) {
                    GC gc = event.gc;
                    Rectangle area = itemsTable.getClientArea();
                    int columnCount = itemsTable.getColumnCount();
                    if (event.index == columnCount - 1 || columnCount == 0) {
                        int width = area.x + area.width - event.x;
                        if (width > 0) {
                            Region region = new Region();
                            gc.getClipping(region);
                            region.add(event.x, event.y, width, event.height);
                            gc.setClipping(region);
                            region.dispose();
                        }
                    }
                    gc.setAdvanced(true);
                    if (gc.getAdvanced()) gc.setAlpha(127);
                    Rectangle rect = event.getBounds();
                    Color foreground = gc.getForeground();
                    Color background = gc.getBackground();
                    gc.setForeground(comp.getDisplay().getSystemColor(SWT.COLOR_BLUE));
                    gc.setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
                    gc.fillGradientRectangle(0, rect.y, itemsTable.getClientArea().width, rect.height, false);
                    gc.setForeground(foreground);
                    gc.setBackground(background);
                    event.detail &= ~SWT.SELECTED;
                }
            }
        });
        MouseListener openListener = new MouseListener() {

            public void mouseDoubleClick(MouseEvent me) {
                openTab();
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        };
        itemsTable.addMouseListener(openListener);
        openNewTab.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                openTab();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        deleteItem.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                JReader.removeItem(JReader.getItems().get(itemsTable.getSelectionIndex()));
                Filters.refresh();
                SubsList.refresh();
                refresh();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        itemsTable.addFocusListener(Focus.setFocus((Items.folderItem)));
    }
