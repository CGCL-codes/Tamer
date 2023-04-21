    public void testGetChannelNames() {
        try {
            System.out.println("Channels registered in the server:");
            RhnServer server = new RhnServer("http://satellite1.example.com/rpc/api", "admin", "redhat");
            for (String channel : server.getChannelNames()) {
                System.out.println(channel);
            }
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }
