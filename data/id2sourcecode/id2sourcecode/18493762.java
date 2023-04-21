        public void actionPerformed(ActionEvent e) {
            RssChannelDialog dialog = RssChannelDialog.showDialog(parent);
            if (dialog.getDialogResult() == RssChannelDialog.YES_OPTION) {
                System.out.println("Adding Channel to channel model!!!");
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
