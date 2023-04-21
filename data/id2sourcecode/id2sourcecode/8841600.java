            public void internalFrameClosed(InternalFrameEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        ((ChannelPanel) getChannelPanel()).part();
                    }
                });
            }
