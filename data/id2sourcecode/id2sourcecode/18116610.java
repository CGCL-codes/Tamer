    @Override
    public Channel getChannel(String id) {
        Channel channel = null;
        List<Subscriber> subscribers = new ArrayList<Subscriber>();
        List<Publisher> publishers = new ArrayList<Publisher>();
        String channelData = (String) super.get(id);
        try {
            JSONObject channelObject = new JSONObject(channelData);
            String channelId = channelObject.getString(ID);
            String channelName = channelObject.getString(NAME);
            int subscriberCount = channelObject.getInt(SUBSCRIBER_COUNT);
            boolean privateChannel = channelObject.getBoolean(PRIVATE);
            boolean systemChannel = channelObject.getBoolean(SYSTEM);
            String secretKey = channelObject.getString(SECRET_KEY);
            String accessKey = channelObject.getString(ACCESS_KEY);
            String owner = channelObject.getString(OWNER);
            long createdDate = channelObject.getLong(CREATED_DATE);
            int stateValue = channelObject.getInt(STATE);
            Channel.ChannelState state = null;
            for (Channel.ChannelState ch : Channel.ChannelState.values()) {
                if (ch.getValue() == stateValue) {
                    state = ch;
                    break;
                }
            }
            channel = new Channel(channelId, channelName, subscriberCount, privateChannel, systemChannel, secretKey, accessKey, owner, createdDate, state, subscribers, publishers);
        } catch (JSONException e) {
            logger.error("Error parsing json response from the channel store:", e);
        }
        return channel;
    }
