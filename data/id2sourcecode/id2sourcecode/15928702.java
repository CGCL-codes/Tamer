    List<String> getChannels() {
        List<String> result = new ArrayList<String>();
        for (Object channel : bayeux.getChannels()) {
            result.add(channel.toString());
        }
        return result;
    }
