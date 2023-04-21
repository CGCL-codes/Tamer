        public void actionPerformed(ActionEvent e) {
            try {
                Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    String s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    RssChannelDialog dialog = RssChannelDialog.showDialog(parent, s);
                    if (dialog.getDialogResult() == RssChannelDialog.YES_OPTION) {
                        NodePath path = new NodePath(selectionModel.getSelectionPath());
                        if (path.getLastElement() instanceof CategoryNode) {
                            System.out.println("LastPathElement");
                            channelModel.add((CategoryNode) path.getLastElement(), dialog.getChannel());
                        } else {
                            System.out.println("LastCategory");
                            channelModel.add(path.getLastCategory(), dialog.getChannel());
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }
