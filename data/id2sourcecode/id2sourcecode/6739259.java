    public static boolean mapsourcesOnlineUpdate() throws MapSourcesUpdateException {
        URL url;
        try {
            File mapFile = new File(Settings.getUserDir(), "mapsources.properties");
            url = new URL(MAPSOURCES_UPDATE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Settings s = Settings.getInstance();
            if (mapFile.isFile() && s.mapSourcesUpdate.etag != null && s.mapSourcesUpdate.etag != "") conn.addRequestProperty("If-None-Match", s.mapSourcesUpdate.etag);
            int code = conn.getResponseCode();
            log.trace("Mapsources online update: \n\tUpdate url: " + MAPSOURCES_UPDATE_URL + "\n\tResponse  : " + code + " " + conn.getResponseMessage() + "\n\tSize      : " + conn.getContentLength() + " bytes \n\tETag      : " + conn.getHeaderField("ETag"));
            if (code == 304) return false;
            if (code != 200) throw new MapSourcesUpdateException("Invalid HTTP server response: " + code + " " + conn.getResponseMessage());
            DataInputStream in = new DataInputStream(conn.getInputStream());
            if (conn.getContentLength() == 0) throw new MapSourcesUpdateException("This version of TrekBuddy Atlas Creator is no longer supported. \n" + "Please update to the current version.");
            byte[] data = new byte[conn.getContentLength()];
            in.readFully(data);
            in.close();
            conn.disconnect();
            Properties onlineProps = new Properties();
            onlineProps.load(new ByteArrayInputStream(data));
            int onlineRev = getMapSourcesRev(onlineProps);
            int currentRev = parseMapSourcesRev(System.getProperty(MAPSOURCES_REV_KEY));
            s.mapSourcesUpdate.lastUpdate = new Date();
            s.mapSourcesUpdate.etag = conn.getHeaderField("ETag");
            if (onlineRev > currentRev || !mapSourcesExternalFileUsed) {
                System.getProperties().putAll(onlineProps);
                FileOutputStream mapFs = null;
                try {
                    mapFs = new FileOutputStream(mapFile);
                    mapFs.write(data);
                } finally {
                    Utilities.closeStream(mapFs);
                }
                for (MapSource ms : getAllMapSources()) {
                    if (ms instanceof UpdatableMapSource) {
                        ((UpdatableMapSource) ms).update();
                    }
                }
                mapSourcesExternalFileUsed = true;
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new MapSourcesUpdateException(e);
        }
    }
