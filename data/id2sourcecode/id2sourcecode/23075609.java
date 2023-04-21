    public void testStations(String resultsFile) throws IOException {
        logger.fine("Testing stations");
        FileOutputStream outputStream = new FileOutputStream(resultsFile);
        PrintWriter writer = new PrintWriter(outputStream);
        ShoutcastSnooper shoutcastSnooper = new ShoutcastSnooper();
        for (int row = 0; row < allStationsFolder.getChildCount(); row++) {
            CDSContainer stationObj = (CDSContainer) allStationsFolder.getChild(row);
            if (!(stationObj instanceof CDSPlaylistContainer)) {
                logger.warning("CDSContainer not instance of CDSPlaylistContainer!\n" + "Are there some non-XML and/or subdirectories in\n" + "the AllStations directories");
                continue;
            }
            writer.println("-------------------------------------------------------------------------");
            writer.println("Processing radio station '" + stationObj.getTitle() + "'");
            System.out.println("Processing station " + stationObj.getTitle());
            for (int n = 0; n < stationObj.getChildCount(); n++) {
                CDSContainer stationFmt = (CDSContainer) stationObj.getChild(n);
                writer.println("Testing fmt/bitrate" + stationFmt.getTitle());
                System.out.println("Testing fmt/bitrate" + stationFmt.getTitle());
                int resourceCount = stationFmt.getResourceCount();
                CDSResource resource = stationFmt.getResource(0);
                writer.println("  Protocol: " + resource.getProtocolInfo() + "\n  Resource: " + resource.getName());
                shoutcastSnooper.readPlaylist(resource, writer);
            }
        }
        writer.flush();
        outputStream.close();
    }
