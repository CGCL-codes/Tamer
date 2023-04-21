    public void readPlaylist(CDSResource playlistResource, PrintWriter writer) throws MalformedURLException {
        System.out.println("  Reading playlist " + playlistResource.getName());
        CDSObjectList objList = playlistResource.getPlaylistItems();
        if (objList == null) {
            writer.println("  Status: ERROR (couldn't access playlist)");
            writer.flush();
            return;
        }
        for (int n = 0; n < objList.size(); n++) {
            CDSItem item = (CDSItem) objList.getObject(n);
            CDSResource streamResource = item.getResource(0);
            writer.println("  Trying playlist stream " + n + ": " + item.getTitle());
            try {
                if (readURL(streamResource.getName(), writer)) {
                    writer.println("  Status: OK");
                    writer.flush();
                    return;
                }
            } catch (Exception e) {
                System.out.println("Exception processing URL " + streamResource.getName() + " " + e);
            }
        }
        writer.println("  Status: ERROR");
        writer.flush();
    }
