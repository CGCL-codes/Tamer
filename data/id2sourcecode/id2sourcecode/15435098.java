    @Override
    public boolean storePublisher(Publisher lPublisher) {
        JSONObject lPublisherObject = new JSONObject();
        try {
            lPublisherObject.put(ID, lPublisher.getId());
            lPublisherObject.put(LOGIN, lPublisher.getLogin());
            lPublisherObject.put(CHANNEL, lPublisher.getChannel());
            lPublisherObject.put(AUTHORIZED_DATE, lPublisher.getAuthorizedDate().getTime());
            lPublisherObject.put(LAST_PUBLISHED_DATE, lPublisher.getLastPublishedDate().getTime());
            lPublisherObject.put(IS_AUTHORIZED, lPublisher.isAuthorized());
            super.put(lPublisher.getId(), lPublisherObject.toString());
            return true;
        } catch (JSONException e) {
            mLog.error("Error constructing JSON data for the given publisher '" + lPublisher.getId() + "'", e);
            return false;
        }
    }
