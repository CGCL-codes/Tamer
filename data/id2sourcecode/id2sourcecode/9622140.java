    static PartEvent part(String data, Connection con) {
        Matcher m = partPattern.matcher(data);
        if (m.matches()) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("HERE? " + m.group(4));
                log.fine(data);
            }
            PartEvent partEvent = new PartEventImpl(data, myManager.getSessionFor(con), m.group(1), m.group(2), m.group(3), con.getChannel(m.group(4).toLowerCase()).getName(), con.getChannel(m.group(4).toLowerCase()), m.group(5));
            return partEvent;
        } else {
            log.severe("NO MATCH");
        }
        debug("PART", data);
        return null;
    }
