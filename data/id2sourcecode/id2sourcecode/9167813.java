    private static Serializable extractBody(Message message) {
        try {
            if (message instanceof TextMessage) return ((TextMessage) message).getText();
            if (message instanceof ObjectMessage) return ((ObjectMessage) message).getObject();
            if (message instanceof MapMessage) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
                Enumeration<String> names = ((MapMessage) message).getMapNames();
                while (names.hasMoreElements()) {
                    String key = names.nextElement();
                    map.put(key, ((MapMessage) message).getObject(key));
                }
                return map;
            }
            if (message instanceof BytesMessage) {
                BytesMessage bm = (BytesMessage) message;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8096];
                int c;
                while ((c = bm.readBytes(buffer)) != -1) baos.write(buffer, 0, c);
                return baos.toByteArray();
            }
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
        throw new JMSClientException("Unsupported message type: " + message.getClass().getName() + " (" + message + ")");
    }
