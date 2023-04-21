    public static boolean mapsourcesOnlineUpdate() throws MapSourcesUpdateException {
        URL url;
        try {
            File mapFile = new File(DirectoryManager.currentDir, MapSourcesUpdater.MAPSOURCES_PROPERTIES);
            String mapUpdateUrl = System.getProperty("mobac.updateurl");
            if (mapUpdateUrl == null) throw new MapSourcesUpdateException("No update url configured!");
            url = new URL(mapUpdateUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Settings settings = Settings.getInstance();
            if (mapFile.isFile() && settings.mapSourcesUpdate.etag != null && settings.mapSourcesUpdate.etag != "") conn.addRequestProperty("If-None-Match", settings.mapSourcesUpdate.etag);
            int code = conn.getResponseCode();
            MapSourcesUpdater.log.trace("Mapsources online update: \n\tUpdate url: " + mapUpdateUrl + "\n\tResponse  : " + code + " " + conn.getResponseMessage() + "\n\tSize      : " + conn.getContentLength() + " bytes \n\tETag      : " + conn.getHeaderField("ETag"));
            if (code == 304) return false;
            if (code != 200) throw new MapSourcesUpdateException("Invalid HTTP server response: " + code + " " + conn.getResponseMessage());
            DataInputStream in = new DataInputStream(conn.getInputStream());
            if (conn.getContentLength() == 0) throw new MapSourcesUpdateException("This version of Mobile Atlas Creator is no longer supported. \n" + "Please update to the current version.");
            byte[] data = new byte[conn.getContentLength()];
            in.readFully(data);
            in.close();
            conn.disconnect();
            Properties onlineProps = new Properties();
            onlineProps.load(new ByteArrayInputStream(data));
            int onlineRev = getMapSourcesRev(onlineProps);
            int currentRev = parseMapSourcesRev(System.getProperty(MapSourcesUpdater.MAPSOURCES_REV_KEY));
            settings.mapSourcesUpdate.lastUpdate = new Date();
            settings.mapSourcesUpdate.etag = conn.getHeaderField("ETag");
            if (onlineRev > currentRev || !mapSourcesExternalFileUsed) {
                System.getProperties().putAll(onlineProps);
                FileOutputStream mapFs = null;
                try {
                    mapFs = new FileOutputStream(mapFile);
                    mapFs.write(data);
                } finally {
                    Utilities.closeStream(mapFs);
                }
                for (MapSource ms : MapSourcesManager.getAllMapSources()) {
                    if (ms instanceof UpdatableMapSource) {
                        ((UpdatableMapSource) ms).update();
                    }
                }
                mapSourcesExternalFileUsed = true;
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MapSourcesUpdateException(e);
        }
    }
