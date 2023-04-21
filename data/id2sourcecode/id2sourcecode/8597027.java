    public synchronized Connection openPrimInternal(String name, int mode, boolean timeouts) throws IOException {
        if (mode == Connector.WRITE) {
            throw new IllegalArgumentException("WRITE not supported.");
        }
        if ((mode != Connector.READ) && (mode != Connector.READ_WRITE)) {
            throw new IllegalArgumentException("Invalid I/O constraint.");
        }
        if (name.charAt(0) != '/' || name.charAt(1) != '/') {
            throw new IllegalArgumentException("Missing protocol separator.");
        }
        int colon = name.indexOf(':');
        if (colon != 2) {
            throw new IllegalArgumentException("Host not supported.");
        }
        String msgIDText = name.substring(colon + 1);
        int msgID = 0;
        try {
            msgID = Integer.parseInt(msgIDText);
            if ((msgID > 65535) || (msgID < 0)) {
                throw new IllegalArgumentException("Message ID out of range.");
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Message ID formatted badly.");
        }
        if (openPermission == false) {
            try {
                midletSuite.checkForPermission(Permissions.CBS_SERVER, "cbs:open");
                openPermission = true;
            } catch (InterruptedException ie) {
                throw new InterruptedIOException("Interrupted while trying " + "to ask the user permission.");
            }
        }
        for (int i = 0, n = openConnections.size(); i < n; i++) {
            if (((Protocol) openConnections.elementAt(i)).url.equals(name)) {
                throw new IOException("Connection already open.");
            }
        }
        url = name;
        m_imsgid = 0;
        if (msgIDText != null) {
            m_imsgid = msgID;
        }
        try {
            connHandle = open0(m_imsgid, midletSuite.getID());
        } catch (IOException ioexcep) {
            m_mode = 0;
            throw new IOException("Unable to open CBS connection.");
        } catch (OutOfMemoryError oomexcep) {
            m_mode = 0;
            throw new IOException("Unable to open CBS connection.");
        }
        openConnections.addElement(this);
        m_mode = mode;
        open = true;
        return this;
    }
