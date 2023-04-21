            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof PVTreeNode) {
                    PVTreeNode tn = (PVTreeNode) source;
                    if (tn.isPVName()) {
                        Channel channel = tn.getChannel();
                        if (channel != null) {
                            pvNameJText.setText(null);
                            pvNameJText.setText(channel.channelName());
                        } else {
                            pvNameJText.setText(null);
                            pvNameJText.setText(tn.getName());
                        }
                    }
                }
            }
