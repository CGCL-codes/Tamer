    public void publish(Set<Object[]> dataUpdates) {
        if (!enabled) return;
        try {
            AsyncMessage message = new AsyncMessage();
            message.setClientId(clientId);
            message.setHeader(AsyncMessage.SUBTOPIC_HEADER, "tideDataTopic");
            message.setDestination(topic);
            message.setHeader("GDSSessionID", sessionId);
            message.setHeader("type", "DATA");
            if (paramsProvider != null) {
                DataPublishParams params = new DataPublishParams();
                for (Object[] dataUpdate : dataUpdates) paramsProvider.publishes(params, dataUpdate[1]);
                params.setHeaders(message);
            }
            message.setBody(dataUpdates.toArray());
            Channel channel = gravity.getChannel(clientId);
            Message resultMessage = gravity.publishMessage(channel, message);
            if (resultMessage instanceof ErrorMessage) log.error("Could not dispatch data update on topic %s, message %s", topic, resultMessage.toString()); else log.debug("Data message dispatched on topic %s", topic);
        } catch (Exception e) {
            log.error(e, "Could not dispatch data update on topic %s", topic);
        }
    }
