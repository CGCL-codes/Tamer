    public void testGetCounter() {
        try {
            RhnSwChannels channels = new RhnSwChannels("http://satellite1.example.com/rpc/api", "admin", "redhat");
            if (channels.getCounter() != channels.getChannels().size()) {
                fail("Counter is " + channels.getCounter() + " but there are " + channels.getChannels().size() + " channels.");
            }
        } catch (Exception ex) {
            fail("Exception arised: " + ex.getMessage());
        }
    }
