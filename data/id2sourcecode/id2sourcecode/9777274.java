    public Set read() throws IOException, ParserConfigurationException {
        URL url = new URL(channelsUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = in.readLine();
        Set channels = new HashSet();
        while (line != null) {
            Channel channel = new Channel();
            String[] values = line.split("\\|");
            channel.setId(values[0]);
            channel.setDisplayName(values[1]);
            channels.add(channel);
            line = in.readLine();
        }
        return channels;
    }
