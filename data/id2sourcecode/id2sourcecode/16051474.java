    public boolean offer(T msg) {
        boolean ret = false;
        Channel channel = null;
        try {
            channel = getChannel();
            if (null != channel) {
                String json = toJson(msg);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes(charset));
                l.debug(" [x] Sent '" + json + "'");
                ret = true;
            } else {
                l.debug("channel can not be initialized.");
            }
        } catch (Exception ex) {
            processException(ex);
        } finally {
            try {
                if (null != channel) {
                    l.debug("close channel.");
                    channel.close();
                    l.debug("close channel successfully.");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        return ret;
    }
