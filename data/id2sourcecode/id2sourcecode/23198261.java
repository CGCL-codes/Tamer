    public void load_preview() {
        URL url = null;
        InputStream is = null;
        Socket s = new Socket();
        try {
            url = createWmsRequestURL();
            if (url != null) {
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setReadTimeout(Navigator.TIME_OUT);
                urlc.connect();
                is = urlc.getInputStream();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        icon = new ImageIcon(url);
        jLabel.setIcon(icon);
    }
