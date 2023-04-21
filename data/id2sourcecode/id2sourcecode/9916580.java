    private ReceiveMessage receive(final Socket conn) throws IOException {
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[512];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
        byte[] tmp = out.toByteArray();
        int index = 0;
        while (index < tmp.length) if (tmp[index++] == Sender.SEPERATOR) break;
        Type type;
        try {
            type = Type.valueOf(new String(tmp, 0, index - 1));
        } catch (IllegalArgumentException e) {
            type = Type.UNKNOWN;
        }
        if (logger.isDebugEnabled()) logger.debug("we recevie a message: " + type);
        if (Type.UNKNOWN == type || (Type.QUERY != type && index == tmp.length)) {
            logger.warn("[beep] receive unknown type message or [" + type + "]message without content.");
            return ReceiveMessage.INVALID_MESSAGE;
        }
        ReceiveMessage receive = new ReceiveMessage(type);
        if (Type.QUERY == type) {
            for (ServerEventListener l : set) l.getBuddyQuery(conn.getInetAddress().getHostAddress());
        } else if (Type.TEXT == type) {
            String msg = new String(tmp, index, tmp.length - index, "utf-8");
            String title = String.format(getMessage("msg_text_title"), conn.getInetAddress().getHostAddress());
            receive.setTitle(title);
            receive.setContent(msg);
        } else if (Type.LINK == type) {
            String link = new String(tmp, index, tmp.length - index, "utf-8");
            URL url;
            try {
                url = new URL(link);
            } catch (MalformedURLException e) {
                url = new URL("http", link, "");
            }
            String title = String.format(getMessage("msg_link_title"), conn.getInetAddress().getHostAddress());
            receive.setTitle(title);
            receive.setContent(url.toString());
            receive.setUrl(url);
        } else if (Type.IMAGE == type) {
            String title = String.format(getMessage("msg_image_title"), conn.getInetAddress().getHostAddress());
            File file = new File(Config.getLogPath() + System.currentTimeMillis() + IMAGE_FILE_EXT);
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(tmp, index, tmp.length - index);
            fileOut.flush();
            fileOut.close();
            String msg = String.format(getMessage("msg_image_content"), file.toURL().getPath());
            receive.setTitle(title);
            receive.setContent(msg);
            receive.setUrl(file.toURL());
        }
        receive.setSource(conn.getInetAddress().getHostAddress());
        if (receive.getType() != Type.QUERY) Recorder.addMessage(LogMessage.getLog(receive));
        OutputStream response = conn.getOutputStream();
        response.write(Sender.RESP_OK_BYTES);
        response.flush();
        response.close();
        return receive;
    }
