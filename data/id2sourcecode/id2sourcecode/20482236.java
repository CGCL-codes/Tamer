            public void mouseClicked(MouseEvent e) {
                if (connect_btn.getLabel().equals("connect")) {
                    listener.connect();
                    listener.login();
                    if (listener.getConnected()) {
                        connect_btn.setLabel("disconnect");
                        disconnected = false;
                    }
                } else {
                    textout.append("\nDisconnecting..");
                    listener.disconnect();
                    if (!listener.getConnected()) {
                        connect_btn.setLabel("connect");
                        disconnected = true;
                        memberslist.removeAll();
                    }
                }
            }
