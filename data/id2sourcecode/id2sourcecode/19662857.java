    public void parsePackageActivity(final MapActivity activity, final String number) {
        super.setMapActivity(activity);
        try {
            final HttpsURLConnection conn = (HttpsURLConnection) new URL(activity.getString(R.string.ups_url)).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setHostnameVerifier(new AllowAllHostnameVerifier());
            conn.setRequestMethod("POST");
            final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(this.getValidationData(number));
            writer.flush();
            writer.close();
            SAXParserFactory.newInstance().newSAXParser().parse(new InputSource(conn.getInputStream()), this);
        } catch (Exception e) {
        }
    }
