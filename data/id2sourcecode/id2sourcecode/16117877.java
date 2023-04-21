    public String[] getChannels() throws IOException {
        String list_command = "list-channels";
        String delimiter = ",";
        String raw_list = "";
        String processed_list = "";
        String[] result = new String[0];
        Vector channels = new Vector();
        if (!connected) {
            throw new IOException("NWP Not connected.");
        }
        try {
            log.debug("Requesting NWP channel list");
            raw_list = writeReadControl(list_command + '\n');
            processed_list = raw_list.split(":")[2].trim();
        } catch (IOException ioe) {
            log.error("Communicating with daq control channel" + ioe);
            ioe.printStackTrace();
        }
        StringTokenizer tokens = new StringTokenizer(processed_list, delimiter);
        String tok;
        while (tokens.hasMoreTokens()) {
            tok = tokens.nextToken();
            channels.addElement(tok);
        }
        result = new String[channels.size()];
        for (int i = 0; i < channels.size(); i++) result[i] = (String) channels.elementAt(i);
        return (result);
    }
