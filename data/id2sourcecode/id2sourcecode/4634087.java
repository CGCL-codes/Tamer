            @Override
            public void onClick(View v) {
                XMPPConnection connection = new XMPPConnection("192.168.11.168");
                try {
                    connection.connect();
                    connection.login(username.getText().toString(), password.getText().toString());
                    Presence presence = new Presence(Presence.Type.available);
                    connection.sendPacket(presence);
                    setConnection(connection);
                    multiUserChat = new MultiUserChat(connection, "r157@conference.trial");
                    multiUserChat.join(username.getText().toString());
                    chatmanager = connection.getChatManager();
                    chatPage();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
