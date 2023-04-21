    private static String getMessage(Message message) {
        String data = message.getData();
        if (!data.matches("\\{.*")) {
            data = "\"" + data + "\"";
        }
        String pattern = "{\"id\":\"" + message.getuID() + "\", \"ch\":\"" + message.getChannelName() + "\",\"data\":" + data + "},";
        return pattern;
    }
