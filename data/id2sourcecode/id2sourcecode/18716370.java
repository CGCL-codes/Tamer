    public void write(String level, long thread_id, String description, String details) {
        String msg = "\r\n\r\n" + level + " [" + thread_id + "] " + date_format.format(new Date()) + "\r\n" + "TCE " + date_format.format(new Date()) + "\r\n" + "Description: " + description + "\r\n" + details + "\r\n" + "-------------------------------------";
        for (int i = 0; i < sockets.size(); i++) {
            ((SocketLog) sockets.get(i)).logs.add(msg);
        }
    }
