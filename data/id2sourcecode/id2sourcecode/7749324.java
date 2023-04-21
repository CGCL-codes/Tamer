    private JSONObject toJSON(XSection xsect) {
        JSONObject jxsect = new JSONObject();
        jxsect.put("channel_id", xsect.getChannelId());
        jxsect.put("dist", xsect.getDistance());
        jxsect.put("layers", toJSONLayer(xsect.getLayers()));
        return jxsect;
    }
