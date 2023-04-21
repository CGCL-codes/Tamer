        private void sendData(ChannelStateEvent e) {
            Channel channel = e.getChannel();
            if (channel.isWritable()) {
                ChannelBuffer buf = buffer("8=FIX.4.2\0019=12\00135=X\001108=30\00110=049\001whoops8=FIX.4.2\0019=12\00135=X\001108=30\00110=049\001");
                channel.write(buf);
            }
        }
