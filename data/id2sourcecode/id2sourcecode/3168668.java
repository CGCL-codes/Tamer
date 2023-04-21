    boolean submit(String artist, String track, String album, int len, long startTime, String auth, String rating) {
        String req;
        URL url;
        try {
            req = "s=" + mSessionId + "&a[0]=" + URLEncoder.encode(artist, "UTF-8") + "&t[0]=" + URLEncoder.encode(track, "UTF-8") + "&i[0]=" + Long.toString(startTime) + "&o[0]=L" + auth + "&r[0]=" + rating + "&l[0]=" + Integer.toString(len) + "&b[0]=" + URLEncoder.encode(album, "UTF-8") + "&n[0]=" + "&m[0]=";
            url = new URL(mSubmissionUrl);
            Log.d(TAG, "submit() '" + req + "' to " + url.toString());
        } catch (UnsupportedEncodingException e1) {
            Log.e(TAG, "while scrobbling", e1);
            return false;
        } catch (MalformedURLException e1) {
            Log.e(TAG, "while scrobbling", e1);
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
            if (res != null && res.equals("OK")) {
                Log.d(TAG, "Submit returned " + res);
                return true;
            } else {
                Log.e(TAG, "Submit returned " + res);
                return false;
            }
        } catch (IOException e) {
            Log.e(TAG, "while scrobbling", e);
        } finally {
            try {
                if (wr != null) wr.close();
                if (rd != null) rd.close();
            } catch (IOException e) {
                Log.e(TAG, "while scrobbling", e);
            }
        }
        return false;
    }
