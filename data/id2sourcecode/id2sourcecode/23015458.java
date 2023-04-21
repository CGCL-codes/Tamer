    public void messageReceived(IoSession session, Object msg) throws Exception {
        String str = msg.toString();
        if (str.trim().equalsIgnoreCase("quit")) {
            session.close();
            return;
        }
        if (session.getAttribute("FIRST_MESSAGE") == null) {
            session.setAttribute("FIRST_MESSAGE", str);
        }
        Date date = new Date();
        session.write(date.toString() + ":" + Thread.currentThread().getId() + str + ":" + session.getAttribute("FIRST_MESSAGE"));
        System.out.println("Message written...");
    }
