    public void changePlaylist(String newM3UFileName, boolean shuffle) throws Exception {
        URL url;
        URLConnection conn;
        InputStreamReader rd;
        terminatePlayer();
        startPlayer();
        getPlayerStatus();
        if ((playerState.random && !shuffle) || (!playerState.random && shuffle)) {
            url = new URL("http://" + httpHostVal + "/requests/status.xml?command=pl_random");
            conn = url.openConnection();
            conn.connect();
            rd = new InputStreamReader(conn.getInputStream());
            rd.close();
        }
        newM3UFileName = newM3UFileName.replace('\\', '/');
        URI uri = new URI("file", "///" + newM3UFileName, null);
        url = new URL("http://" + httpHostVal + "/requests/status.xml?command=in_play&input=" + uri);
        conn = url.openConnection();
        conn.connect();
        rd = new InputStreamReader(conn.getInputStream());
        rd.close();
        getPlayerStatus();
    }
