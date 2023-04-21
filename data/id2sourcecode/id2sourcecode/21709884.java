    public void showExportVideoDialog() {
        List<String> channels = channelListPanel.getSelectedChannels();
        for (int i = channels.size() - 1; i >= 0; i--) {
            Channel channel = RBNBController.getInstance().getChannel(channels.get(i));
            String mime = channel.getMetadata("mime");
            if (!mime.equals("image/jpeg")) {
                channels.remove(i);
            }
        }
        if (channels.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no video channels selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new ExportVideoDialog(frame, rbnb, channels);
    }
