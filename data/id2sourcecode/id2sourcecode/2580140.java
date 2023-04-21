    public NickResponse doInBackground() {
        updateStatus("1");
        BufferedReader reader = null;
        URL url = null;
        StringBuffer buff;
        try {
            url = new URL(strurl);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            buff = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buff.append(line);
                buff.append("\n");
            }
            NickResponse resp = NickResponseParser.parse(buff.toString());
            return resp;
        } catch (IOException e) {
            updateStatus("2");
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception e) {
            }
        }
    }
