    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if (action != null && action.equals("open")) {
            log.info("Open connection request received.");
            open(request, response);
        } else {
            long id = Long.parseLong(request.getParameter("id"));
            if (action != null && action.equals("close")) {
                log.info("Close connection request recieved.");
                connectionManager.removeConnection(id);
            } else if (action != null && action.equals("read")) {
                log.debug("Read request recieved.");
                read(id, response, true);
            } else if (action != null && action.equals("write")) {
                log.debug("Write request recieved.");
                write(id, request);
            } else if (action != null && action.equals("readwrite")) {
                log.debug("Read/Write request recieved.");
                readWrite(id, request, response);
            }
        }
    }
