    public InputStream connect() {
        try {
            URL url = new URL(this.url);
            RSSTitle = url.getHost();
            tv = (TextView) findViewById(R.id.label);
            tv.setText(RSSTitle);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                this.wena = true;
                return is;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
