    public void testGetChannels() {
        try {
            RhnSwChannels channels = new RhnSwChannels("http://satellite1.example.com/rpc/api", "admin", "redhat");
            List<RhnSwChannel> list = new ArrayList<RhnSwChannel>();
            list = channels.getChannels();
            for (RhnSwChannel channel : list) {
                System.out.println(channel.getName() + " - Channel label: " + channel.getLabel() + " - Channel ID: " + channel.getId().toString() + " - Parent channel: " + channel.getParentchannel());
            }
        } catch (Exception ex) {
            fail("Exception arised: " + ex.getMessage());
        }
    }
