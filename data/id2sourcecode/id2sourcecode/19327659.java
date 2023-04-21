    public static List<String> getPlayersInMyChannel(String host, int udpport, final String myName) {
        final List<String> players = new ArrayList<String>();
        final Map<String, Integer> playersWithChannels = new HashMap<String, Integer>();
        TeamspeakInfo tsi = TeamspeakConnector.getTeamSpeakInfo(host, udpport);
        for (final PlayerInfo pi : tsi.getPi()) {
            final String name = pi.getNick().replaceAll("\\\\|\\/|:|\\*|\\?|\"|<|>|\\|| ", "");
            final Integer channel = pi.getChannelId();
            playersWithChannels.put(name, channel);
        }
        System.out.println(playersWithChannels);
        final Integer myChannel = playersWithChannels.get(myName);
        for (final Entry<String, Integer> e : playersWithChannels.entrySet()) {
            if (e.getValue().equals(myChannel)) players.add(e.getKey());
        }
        System.out.println(players);
        return players;
    }
