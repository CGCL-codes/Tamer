    boolean handshake(String username, String password) {
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String auth = Utils.md5String(Utils.md5String(password) + timestamp);
        BufferedReader stringReader = null;
        try {
            String req = "http://post.audioscrobbler.com/?hs=true&p=1.2&" + "c=" + CLIENT_ID + "&v=" + URLEncoder.encode(mClientVersion, "UTF-8") + "&u=" + URLEncoder.encode(username, "UTF-8") + "&t=" + timestamp + "&a=" + auth;
            URL url = new URL(req);
            Log.d(TAG, "Shakin' hands: " + url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            stringReader = new BufferedReader(reader);
            String res = stringReader.readLine();
            if (res != null && res.equals("OK")) {
                mSessionId = stringReader.readLine();
                mNowPlayingUrl = stringReader.readLine();
                mSubmissionUrl = stringReader.readLine();
                stringReader.close();
                Log.d(TAG, "Handshake complete");
                return true;
            }
            stringReader.close();
            Log.e(TAG, "Handshake failed: " + res);
            return false;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "in scrobbler handshake", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "in scrobbler handshake", e);
            if (stringReader != null) try {
                stringReader.close();
            } catch (IOException e1) {
                Log.e(TAG, "in scrobbler handshake", e1);
            }
            return false;
        }
    }
