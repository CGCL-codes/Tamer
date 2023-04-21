        public void valueChanged(TreeSelectionEvent e) {
            TreePath treePath = e.getNewLeadSelectionPath();
            System.out.println("selected " + treePath);
            Object o = treePath.getLastPathComponent();
            if (o instanceof ChannelTreeNode) {
                ChannelIF channel = ((ChannelTreeNode) o).getChannel();
                itemTable.getItemModel().setChannel(channel);
                itemPane.setItem((ItemIF) channel.getItems().iterator().next());
            }
        }
