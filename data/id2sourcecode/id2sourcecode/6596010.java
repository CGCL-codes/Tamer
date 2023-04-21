    public static void main(String[] args) throws Exception {
        Served xml = new Served();
        List<Server> networks = xml.parse("divx_ita.xml");
        for (Server s : networks) {
            System.out.println("network: " + s.getNetwork());
            System.out.println("server: " + s.getServer());
            System.out.println("channels: " + s.getChannels());
        }
    }
