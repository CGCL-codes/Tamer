    public void sendCommand(Map<String, String> parameters, boolean encodeValues) throws IOException {
        reply_values = new ArrayList<String>();
        URL url = new URL(targetPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStreamWriter textout = new OutputStreamWriter(bout, "ASCII");
        boolean first = false;
        for (String key : parameters.keySet()) {
            if (StringUtils.isBlank(key)) continue;
            String val = parameters.get(key);
            if (StringUtils.isBlank(val)) val = "";
            if (encodeValues) val = URLEncoder.encode(val, "UTF-8");
            if (first) {
                textout.append('?');
                first = false;
            } else {
                textout.append('&');
            }
            textout.append(key).append('=').append(val);
        }
        textout.flush();
        byte[] encodedData = bout.toByteArray();
        con.setRequestProperty("Content-Length", Integer.toString(encodedData.length));
        con.setFixedLengthStreamingMode(encodedData.length);
        con.getOutputStream().write(encodedData);
        InputStream in = con.getInputStream();
        readReplyParams(in);
        in.close();
    }
