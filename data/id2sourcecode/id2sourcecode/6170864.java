    private void sendNumbers(ChannelStateEvent e) {
        Channel channel = e.getChannel();
        while (channel.isWritable()) {
            if (i <= count) {
                channel.write(Integer.valueOf(i));
                i++;
            } else {
                break;
            }
        }
    }
