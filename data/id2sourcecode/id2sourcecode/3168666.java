    boolean nowPlaying(String artist, String track, String album, int len) {
        String req;
        URL url;
        try {
            req = "s=" + mSessionId + "&a=" + URLEncoder.encode(artist, "UTF-8") + "&t=" + URLEncoder.encode(track, "UTF-8") + "&b=" + URLEncoder.encode(album, "UTF-8") + "&l=" + Integer.toString(len) + "&n=&m=";
            url = new URL(mNowPlayingUrl);
            Log.d(TAG, "nowPlaying() '" + req + "' to " + url.toString());
        } catch (UnsupportedEncodingException e1) {
            Log.e(TAG, "while scrobbling 'now playing'", e1);
            return false;
        } catch (MalformedURLException e1) {
            Log.e(TAG, "while scrobbling 'now playing'", e1);
            return false;
        }
        URLConnection conn;
        OutputStreamWriter wr = null;
        BufferedReader rd = null;
        try {
            conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(req);
            wr.flush();
            String res;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            res = rd.readLine();
            if (res != null && res.equals("OK")) return true; else {
                if (res == null) Log.e(TAG, "Now playing returned null"); else Log.e(TAG, "Now playing returned " + res);
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "while scrobbling 'now playing'", e);
        } finally {
            try {
                if (wr != null) wr.close();
                if (rd != null) rd.close();
            } catch (IOException e) {
                Log.e(TAG, "while scrobbling 'now playing'", e);
            }
        }
        return false;
    }
