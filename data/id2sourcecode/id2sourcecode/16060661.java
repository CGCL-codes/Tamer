    protected String _doPost(String url) throws IOException {
        if (userId == -1) throw new IOException("User not logged in");
        url = SwingUtils.combineUrl(url, "portlet_id=" + namespace);
        URL urlPost = new URL(url);
        URLConnection connection = urlPost.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Cookie", "JSESSIONID=" + swsessionId);
        connection.connect();
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            outputStream.write(getParametersString().getBytes());
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
        InputStream inputStream = null;
        StringBuffer stringBuffer = new StringBuffer(1000);
        try {
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return stringBuffer.toString();
    }
