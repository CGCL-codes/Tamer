    @Override
    public boolean storeSubscriber(Subscriber aSubscriber) {
        JSONObject lSubscriberObject = new JSONObject();
        try {
            lSubscriberObject.put(ID, aSubscriber.getId());
            lSubscriberObject.put(LOGGED_IN_TIME, aSubscriber.getLoggedInTime().getTime());
            JSONArray lJSONArray = new JSONArray();
            for (String lChannel : aSubscriber.getChannels()) {
                JSONObject lChannelObject = new JSONObject();
                lChannelObject.put(ID, lChannel);
                lJSONArray.put(lChannelObject);
            }
            lSubscriberObject.put(CHANNELS, lJSONArray);
            super.put(aSubscriber.getId(), lSubscriberObject.toString());
            return true;
        } catch (JSONException lEx) {
            logger.error("Error constructing JSON data for the given subscriber '" + aSubscriber.getId() + "'", lEx);
            return false;
        }
    }
