    @SuppressWarnings("unchecked")
    public Channel resolveChannel(String channelId) throws VDRParserException {
        if (this.channelMap.containsKey(channelId)) {
            return this.channelMap.get(channelId);
        }
        List<String> lines = Collections.emptyList();
        try {
            lines = FileUtils.readLines(this.channels);
        } catch (IOException e) {
            throw new VDRParserException("Problem parsing channels.conf", e);
        }
        String matches = null;
        String[] chanIds = channelId.split("-");
        Channel channel = new Channel();
        if (chanIds.length == 1) {
            matches = this.getChannelLine(lines, Integer.parseInt(chanIds[0]));
        } else {
            String source = chanIds[0];
            String nid = chanIds[1];
            String tid = chanIds[2];
            String sid = chanIds[3];
            String rid = null;
            if (chanIds.length > 4) {
                rid = chanIds[4];
                channel.setRid(rid);
            }
            matches = this.getChannelLine(lines, source, nid, tid, sid, rid);
            channel.setNid(nid);
            channel.setSid(sid);
            channel.setTid(tid);
            channel.setSource(source);
        }
        if (matches == null) {
            throw new VDRParserException("Cannot find channel entry: " + channelId);
        }
        String channelName = matches.split(":")[0];
        if (channelName.contains(";")) {
            channelName = channelName.split(";")[0];
        }
        if (channelName == null) {
            throw new VDRParserException("Cannot find channel entry: " + channelId);
        }
        channel.setName(channelName);
        this.channelMap.put(channelId, channel);
        return channel;
    }
