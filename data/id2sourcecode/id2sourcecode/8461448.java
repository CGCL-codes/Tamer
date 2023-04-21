    private final void componentMouseEvent(final MouseEvent e) {
        if ((e.getButton() == 3) && (e.getClickCount() == 1)) {
            try {
                final Object obj = getSelectedValue();
                if (obj == null) {
                    this.jDesktopPopupMenu.removeAll();
                    this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                    this.jDesktopPopupMenu.addSeparator();
                    this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                    if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                        this.jMenuItemPaste.setEnabled(true);
                    } else {
                        this.jMenuItemPaste.setEnabled(false);
                    }
                    this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                } else {
                    final File selectedFile = ((DiskObject) getSelectedValue()).getFile();
                    if (!selectedFile.isDirectory()) {
                        this.jDesktopPopupMenu.removeAll();
                        this.jDesktopPopupMenu.add(this.jMenuItemOpen);
                        this.jDesktopPopupMenu.addSeparator();
                        if ((JIThumbnailService.getInstance().getOpenWith() != null) && (JIThumbnailService.getInstance().getOpenWith().size() > 0)) {
                            this.jDesktopPopupMenu.add(this.jMenuItemOpenWith);
                            this.jDesktopPopupMenu.addSeparator();
                        }
                        this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                        this.jMenuItemRefresh.setText("Refresh Selected");
                        this.jMenuItemRefresh.add(this.jMenuItemRefreshIcon);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveFromDB);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveKeyWords);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveCategories);
                        this.jDesktopPopupMenu.add(this.jMenuItemRefresh);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemKeyWords);
                        this.jDesktopPopupMenu.add(this.jMenuItemCatagory);
                        this.jDesktopPopupMenu.add(this.jMenuItemMetadata);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemCut);
                        this.jDesktopPopupMenu.add(this.jMenuItemCopy);
                        this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                        if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                            this.jMenuItemPaste.setEnabled(true);
                        } else {
                            this.jMenuItemPaste.setEnabled(false);
                        }
                        this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                    } else {
                        this.jDesktopPopupMenu.removeAll();
                        this.jDesktopPopupMenu.add(this.jMenuItemOpen);
                        if ((JIPreferences.getInstance().getOpenWith() != null) && (JIPreferences.getInstance().getOpenWith().size() > 0)) {
                            this.jDesktopPopupMenu.add(this.jMenuItemOpenWith);
                            this.jDesktopPopupMenu.addSeparator();
                        }
                        this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemCopy);
                        this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                        if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                            this.jMenuItemPaste.setEnabled(true);
                        } else {
                            this.jMenuItemPaste.setEnabled(false);
                        }
                        this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                    }
                }
            } catch (final Exception exp) {
                exp.printStackTrace();
            } finally {
                e.consume();
            }
        }
    }
