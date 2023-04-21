    Utils.OptionsParser handshake(String Username, String Pass) throws IOException {
        String passMD5 = Utils.md5String(Pass);
        URL url = new URL(HOST + "/radio/handshake.php?version=1.0.0.0&platform=windows&username=" + URLEncoder.encode(Username, "UTF_8") + "&passwordmd5=" + passMD5);
        Log.d(TAG, "Shakin' hands: " + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader stringReader = new BufferedReader(reader);
        Utils.OptionsParser options = new Utils.OptionsParser(stringReader);
        if (!options.parse()) options = null;
        stringReader.close();
        Log.d(TAG, "Handshake complete");
        return options;
    }
