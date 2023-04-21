            @Override
            public void valueChanged(TreeSelectionEvent ev) {
                final TreeNode sel = (TreeNode) ev.getNewLeadSelectionPath().getLastPathComponent();
                if (sel instanceof UpdatesChannelNode) {
                    UpdatesChannelNode node = (UpdatesChannelNode) sel;
                    System.err.println("Selected channel node: " + node.getChannel().getName());
                    final UpdatesDataChannel channel = node.getChannel();
                    setChannel(channel);
                }
            }
