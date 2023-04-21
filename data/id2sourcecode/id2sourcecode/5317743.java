    private String buildATFHeader() {
        DataChannel channel = null;
        StringBuffer hdr = new StringBuffer();
        int chans = 0;
        try {
            for (int i = 0; i < storage.getGroupsSize(); i++) {
                for (int j = 0; j < storage.getChannelsSize(i); j++) {
                    channel = storage.getChannel(i, j);
                    if (!channel.getAttribute().isHidden()) chans++;
                }
            }
            hdr.append("ATF" + storage.getDelimiter() + "1.0");
            hdr.append('\n');
            hdr.append(1 + storage.getDelimiter() + chans);
            hdr.append('\n');
            hdr.append("\"Comment=" + comment.getText() + "\"");
            hdr.append('\n');
        } catch (Exception e) {
        }
        return hdr.toString();
    }
