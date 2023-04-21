    public String[] getChannels() throws IOException {
        String list_command = "list-channels";
        String delimiter = ",";
        String raw_list = "";
        String[] result = new String[0];
        Vector channels = new Vector();
        if (!connected) {
            throw new IOException("DAQ Not connected.");
        }
        System.out.println("Requesting DAQ channel list");
        wr.write(list_command + '\n');
        wr.flush();
        raw_list = rd.readLine();
        System.out.println("For Channels, got: " + raw_list);
        StringTokenizer tokens = new StringTokenizer(raw_list, delimiter);
        String tok;
        while (tokens.hasMoreTokens()) {
            tok = tokens.nextToken();
            channels.addElement(tok);
        }
        result = new String[channels.size()];
        for (int i = 0; i < channels.size(); i++) result[i] = (String) channels.elementAt(i);
        return (result);
    }
