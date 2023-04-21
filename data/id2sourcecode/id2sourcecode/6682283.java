    @Override
    public void run() {
        if (_response == null) {
            BufferedReader in = null;
            try {
                final URL url = new URL(ENDGAMES_WEB_SERVICE + URLEncoder.encode(toFEN(_board), "UTF-8"));
                final HttpURLConnection cnx = (HttpURLConnection) url.openConnection();
                cnx.setConnectTimeout(7500);
                cnx.setReadTimeout(2500);
                in = new BufferedReader(new InputStreamReader(cnx.getInputStream()));
                _response = in.readLine();
                cnx.disconnect();
            } catch (final IOException e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (final IOException e) {
                    }
                }
            }
        }
    }
