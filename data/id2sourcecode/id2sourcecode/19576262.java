    public void testGetCounter() {
        try {
            RhnConn connection = new RhnConn("http://satellite1.example.com/rpc/api", "admin", "redhat");
            RhnConfigChannels channels = new RhnConfigChannels(connection);
            System.out.println(channels.getCounter());
            if (channels.getCounter() != channels.getChannels().size()) {
                fail("Counter function is not working properly");
            }
        } catch (Exception ex) {
            fail("Exception arised: " + ex.toString());
        }
    }
