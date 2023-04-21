    protected String getMessage() {
        displayInfo("The message to be displayed is " + juploadContext.getParameter(MESSAGE_URL, ""));
        String urlContent = juploadContext.getParameter(MESSAGE_URL, "");
        String content = urlContent;
        HttpURLConnection connection = null;
        BufferedReader in = null;
        try {
            URL url = new URL(urlContent);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            displayInfo("Getting data ...");
            CharArrayWriter text = new CharArrayWriter();
            int size = 0;
            char[] buffer = new char[8];
            while ((size = in.read(buffer)) >= 0) {
                text.write(buffer, 0, size);
            }
            content = text.toString();
        } catch (IOException ioex) {
            displayErr(ioex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                displayErr(ex);
            }
            connection.disconnect();
        }
        return content;
    }
