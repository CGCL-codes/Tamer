    public void actionPerformed(ActionEvent evt) {
        JMenuItem item = (JMenuItem) evt.getSource();
        String command = item.getActionCommand();
        BajjerClient client = Bajjer.clientWindow;
        if (command.equals("favorite")) {
            Bajjer.signonWindow.setVisible(false);
            client.setVisible(true);
            JabFavorite favorite = (JabFavorite) favoritesMap.get(item.getText());
            String room = (favorite.getChannel() + "@" + favorite.getServer()).toLowerCase();
            HashMap roomList = client.getRoomList();
            if (roomList.containsKey(room)) {
                client.setActive(room);
            } else {
                BajjerConference conferenceWindow = new BajjerConference(favorite.getChannel(), favorite.getServer(), favorite.getNickname());
                client.addRoom(room, conferenceWindow.getConferenceRoom());
                client.addWindow("Group Chat - " + favorite.getChannel(), conferenceWindow.getConferenceRoom());
                presence = new JabPresence("", favorite.getChannel() + "@" + favorite.getServer() + "/" + favorite.getNickname(), "", "Online", "0", "", null);
                presence.send(Bajjer.jabberServer);
            }
        } else if (command.equals("remove")) {
            String favoriteName = item.getText();
            JabFavorite favorite = (JabFavorite) favoritesMap.get(favoriteName);
            favoritesMap.remove(favoriteName);
            favorites.remove(favorite);
            Private preferences = new Private();
            preferences.setFavorites(favorites);
            JabIq iq = new JabIq();
            iq.setId("BajjerMenu:removefavorite");
            iq.setType("set");
            iq.setQuery("jabber:iq:private", "Private", preferences);
            iq.send(Bajjer.jabberServer);
            iq = new JabIq();
            iq.setId("BajjerMenu:favorites");
            iq.setType("get");
            iq.setQuery("jabber:iq:private", "Private", new Private());
            iq.send(Bajjer.jabberServer);
        } else if (command.equals("debug")) {
            JCheckBoxMenuItem checkbox = (JCheckBoxMenuItem) evt.getSource();
            try {
                Class clazz = Class.forName(checkbox.getText());
                Field field = clazz.getField("bDebug");
                field.setBoolean(clazz, checkbox.getState());
            } catch (Exception e) {
                System.out.println("Unable to set debugging for class: " + checkbox.getText());
                System.out.println("Exception = " + e.toString());
            }
        } else {
            String value = item.getText();
            presence = new JabPresence(value, "0", command);
            Vector rooms = JabUtil.vectorFromHashMap(client.getRoomList());
            presence.broadcast(Bajjer.jabberServer, rooms);
        }
    }
