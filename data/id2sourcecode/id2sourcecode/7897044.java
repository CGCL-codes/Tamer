    public void testGetChannelLabels() {
        try {
            System.out.println("Channels registered in the server:");
            RhnServer server = new RhnServer("http://satellite1.example.com/rpc/api", "admin", "redhat");
            for (String label : server.getChannelLabels()) {
                System.out.println(label);
            }
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }
