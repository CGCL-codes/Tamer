    private JSONObject getChannel() {
        JSONObject channel = (JSONObject) jsonRep.get("channel");
        if (channel == null) jsonRep.put("channel", channel = new JSONObject());
        return channel;
    }
