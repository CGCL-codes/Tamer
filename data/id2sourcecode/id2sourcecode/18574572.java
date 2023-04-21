            public void actionPerformed(ActionEvent e) {
                if (Client.getInstance().isConnected()) {
                    Client.getInstance().login(true);
                } else {
                    Client.getInstance().connect(true);
                }
                updateStatusTS();
            }
