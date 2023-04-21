    String get(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);
        conn.setRequestProperty("User-agent", USER_AGENT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (PRINT_TO_CONSOLE) System.out.println(urlString + " - " + conn.getResponseMessage());
        InputStream is;
        if (responseCode == 200) {
            is = conn.getInputStream();
        } else if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_SEE_OTHER || responseCode == 307) {
            redirectCounter++;
            if (redirectCounter > MAX_REDIRECTS) {
                redirectCounter = 0;
                throw new IOException(urlString + " : " + conn.getResponseMessage() + "(" + responseCode + ") : Too manny redirects. exiting.");
            }
            String location = conn.getHeaderField("Location");
            if (location != null && !location.equals("")) {
                System.out.println(urlString + " - " + responseCode + " - new URL: " + location);
                return get(location);
            } else {
                throw new IOException(urlString + " : " + conn.getResponseMessage() + "(" + responseCode + ") : Received moved answer but no Location. exiting.");
            }
        } else {
            is = conn.getErrorStream();
        }
        redirectCounter = 0;
        if (is == null) {
            throw new IOException(urlString + " : " + conn.getResponseMessage() + "(" + responseCode + ") : InputStream was null : exiting.");
        }
        String result = "";
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n\r");
            }
            rd.close();
            result = sb.toString();
        } catch (Exception e) {
            throw new IOException(urlString + " - " + conn.getResponseMessage() + "(" + responseCode + ") - " + e.getLocalizedMessage());
        }
        return result;
    }
