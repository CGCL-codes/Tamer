    private void setChannelModel() {
        videoChannelModel = new DefaultListModel();
        for (int i = 0; i < channels.size(); i++) {
            String channelName = (String) channels.get(i);
            Channel channel = rbnb.getChannel(channelName);
            String mime = channel.getMetadata("mime");
            if (mime.equals("image/jpeg") || mime.equals("video/jpeg")) {
                videoChannelModel.addElement(new ExportChannel(channelName));
            }
        }
    }
