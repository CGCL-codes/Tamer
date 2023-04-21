    public static void main(String args[]) {
        if (args.length < 2) {
            System.out.println("Usage: snooper <playlistURL> <infoFile>");
            System.exit(-1);
        }
        try {
            String playlist = args[0];
            ShoutcastSnooper snooper = new ShoutcastSnooper();
            CDSResource resource = new CDSResource();
            String protocolInfo = null;
            if (playlist.toLowerCase().endsWith("m3u")) {
                protocolInfo = "http-get:*:audio/mpegurl:*";
            } else if (playlist.toLowerCase().endsWith("pls")) {
                protocolInfo = "http-get:*:audio/x-scpls:*";
            } else {
                System.out.println("Unknown playlist type, assuming .pls");
                protocolInfo = "http-get:*:audio/x-scpls:*";
            }
            resource.setProtocolInfo(protocolInfo);
            resource.setName(playlist);
            PrintWriter writer = new PrintWriter(new FileOutputStream(args[1]));
            snooper.readPlaylist(resource, writer);
        } catch (Exception e) {
            System.out.println("Session: readURL: Exception" + e);
        }
    }
