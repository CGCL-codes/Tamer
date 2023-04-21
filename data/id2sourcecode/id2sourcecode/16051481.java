    public T poll(Class<T> clazz, int timeout, Function<T> worker) {
        Channel channel = null;
        QueueingConsumer.Delivery delivery = null;
        T msg = null;
        try {
            channel = getChannel();
            QueueingConsumer consumer = getQueueingConsumer(channel);
            if (null != consumer) {
                if (timeout > 0) {
                    delivery = consumer.nextDelivery(timeout);
                } else {
                    delivery = consumer.nextDelivery();
                }
                if (null != delivery) {
                    String message = new String(delivery.getBody(), charset);
                    l.debug(" [x] Received from " + queueName + ": '" + message + "'");
                    msg = fromJson(message, clazz);
                    if (null != worker) {
                        worker.invoke(msg);
                    }
                    l.debug(" [x] Done");
                } else {
                    l.debug("rabbitmq timeout " + timeout);
                }
            }
        } catch (Exception ex) {
            if (ex instanceof JsonGenerationException) {
                l.error("json error", ex);
            } else if (ex instanceof JsonMappingException) {
                l.error("json error", ex);
            } else if (ex instanceof IOException) {
                l.error("io error, trying reconnect...", ex);
                connStateOk = false;
                checkConnection();
            } else if (ex instanceof ShutdownSignalException) {
                l.error("server shutdown, trying reconnect after 30 sec...", ex);
                connStateOk = false;
                sleep(30000);
                checkConnection();
            } else {
                l.error(ex.getMessage(), ex);
            }
        } finally {
            try {
                if (null != channel) {
                    if (null != delivery) {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                    l.debug("close channel.");
                    channel.close();
                    l.debug("close channel successfully.");
                }
            } catch (Exception ex) {
                l.error(ex.getMessage(), ex);
            }
        }
        return msg;
    }
