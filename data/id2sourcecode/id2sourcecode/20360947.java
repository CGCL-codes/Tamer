    public void write(Message message) throws IOException {
        String date = T4CMessage.dateFormat.format(message.getDate());
        String channel = message.getChannel();
        String source = message.getAvatar();
        String content = message.getContent();
        String padding = content.length() > 0 ? " " : "";
        out.print(date + "-- ");
        if (channel.equals(CC_AREA)) out.print("{" + source + "}\":\"" + padding + content); else if (channel.equals(CC_SELF)) out.print(YOU_SAID + padding + content); else if (channel.equals(CC_SYSTEM)) out.print(content); else if (channel.equals(CC_WHISP)) {
            if (source.equals(YOURSELF)) out.print(WHISP_OUT + " " + content); else out.print(source + WHISP_IN + padding + content);
        } else out.print("[\"" + channel + "\"] \"" + source + "\":" + padding + content);
        out.println();
    }
