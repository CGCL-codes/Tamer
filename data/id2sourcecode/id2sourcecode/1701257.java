    public boolean sendMessage(String subject, String body) {
        if (body == null) return false;
        try {
            MessageBuilder builder = new MessageBuilder();
            builder.reset();
            builder.setToAddress(new JID(address.getUsername(), address.getServer(), null));
            builder.setType("groupchat");
            if (subject != null) builder.setSubject(subject);
            builder.setBody(body);
            cb.send(builder.build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
