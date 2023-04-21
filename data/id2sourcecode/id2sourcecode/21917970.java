    private SocketConnection login(String userId, String pwd) throws IMException {
        logger.debug("Server.login(String..");
        SocketConnection notification = new SocketConnection();
        String response = null;
        String usrResponse = null;
        SocketConnection msnServer = new SocketConnection();
        String xfr = null;
        try {
            msnServer.connect(MSN_SERVER, MSN_SERVER_PORT);
        } catch (IOException e) {
            throw new IMException(e);
        }
        try {
            xfr = initializeServer(msnServer, userId);
        } catch (IOException e) {
            throw new IMException(e);
        } finally {
            msnServer.close();
        }
        String notificationServer = null;
        int notificationServerPort = -1;
        try {
            String tag = " NS ";
            int start = xfr.indexOf(tag);
            start += tag.length();
            int end = xfr.indexOf(":", start);
            notificationServer = xfr.substring(start, end);
            start = end + 1;
            end = xfr.indexOf(" ", start);
            String portString = xfr.substring(start, end);
            notificationServerPort = Integer.parseInt(portString);
        } catch (StringIndexOutOfBoundsException e) {
            logger.error(xfr);
            throw new IMException(e);
        }
        try {
            notification.connect(notificationServer, notificationServerPort);
        } catch (IOException e) {
            throw new IMException(e);
        }
        try {
            response = initializeServer(notification, userId);
            usrResponse = response.substring(12);
        } catch (IOException e) {
            throw new IMException(e);
        }
        String ticket = getTicket(usrResponse, userId, pwd);
        if (ticket == null) {
            notification.close();
            logger.error("ticket == null");
            return null;
        }
        try {
            String command = "USR " + notification.getTrId() + " TWN S " + ticket;
            notification.send(command);
            response = notification.readLine();
        } catch (IOException e) {
            notification.close();
            throw new IMException(e);
        }
        if ((response != null) && (response.indexOf("Unauthorized") >= 0)) {
            logger.error("unauthorized:" + response);
            return null;
        } else {
            return notification;
        }
    }
