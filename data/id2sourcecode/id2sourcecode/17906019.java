    public void updateUsersList() {
        usersListModel.removeAllElements();
        Channel channel = getDocument().getChannel();
        List<String> muteableNicks = new ArrayList<String>(channel.getNicks());
        Session session = getDocument().getSession();
        Map<String, String> nickPrefixMap = session.getServerInformation().getNickPrefixMap();
        Map<String, String> modeNicksMap = new HashMap<String, String>();
        for (String prefix : nickPrefixMap.keySet()) {
            List<String> modeNicks = channel.getNicksForMode(nickPrefixMap.get(prefix));
            Collections.sort(modeNicks, new NickComparator());
            for (String nick : modeNicks) {
                if (!modeNicksMap.containsKey(nick)) {
                    modeNicksMap.put(nick, prefix);
                    muteableNicks.remove(nick);
                    usersListModel.addElement(prefix + nick);
                }
            }
        }
        Collections.sort(muteableNicks, new NickComparator());
        for (String aNick : muteableNicks) {
            usersListModel.addElement(aNick);
        }
    }
