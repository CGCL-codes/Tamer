    private void broadcastToClients(String title, String link, String description) {
        JSONObject jsonMessage = null;
        String query = "select from " + ChannelClient.class.getName();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<ChannelClient> ids = (List<ChannelClient>) pm.newQuery(query).execute();
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        for (ChannelClient m : ids) {
            String client = m.getClientId();
            try {
                jsonMessage = new JSONObject();
                jsonMessage.put("title", title);
                jsonMessage.put("link", link);
                jsonMessage.put("description", description);
                System.out.println("sending json stream: " + jsonMessage.toString());
                System.out.println("to client: " + client);
                channelService.sendMessage(new ChannelMessage(client, jsonMessage.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ChannelFailureException e) {
                log.warning("msg: " + e.getMessage());
            }
        }
        pm.close();
    }
