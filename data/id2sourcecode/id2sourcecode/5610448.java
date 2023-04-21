    public List<ChannelInfo> getChannels() throws IOException {
        Pattern pattern = Pattern.compile("\"(.*)\" \"(.*)\" ([0-9]+) ([0-9]+) ([0-9]+|N/A) ([0-9]+)");
        send("listchan");
        List<ChannelInfo> channels = new ArrayList<ChannelInfo>();
        String line = null;
        QueryProtocol protocol = new QueryProtocol();
        while (!QueryProtocol.OK.equals(line = protocol.readLine(in, encoding))) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int i = 1;
                ChannelInfo channel = new ChannelInfo();
                channel.setName(matcher.group(i++));
                channel.setDescription(matcher.group(i++));
                channel.setPlayernum(Integer.parseInt(matcher.group(i++)));
                channel.setPlayermax(Integer.parseInt(matcher.group(i++)));
                String priority = matcher.group(i++);
                if (!"N/A".equals(priority)) {
                    channel.setPriority(Integer.parseInt(priority));
                }
                channel.setStatus(Integer.parseInt(matcher.group(i++)));
                channels.add(channel);
            } else {
                log.warning("Invalid response for the listchan message (" + hostname + ") : " + line);
            }
        }
        return channels;
    }
