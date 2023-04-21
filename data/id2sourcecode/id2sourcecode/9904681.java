    public static Response send(Request req, String dest) throws IOException, OpenIdException {
        String toSend = req.toUrlString();
        StringBuffer b = new StringBuffer();
        BufferedReader in = null;
        try {
            URL url = new URL(dest + "?" + toSend);
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            int lines = 0;
            while ((str = in.readLine()) != null) {
                b.append(str);
                b.append('\n');
                lines += 1;
            }
            if (lines == 1) {
                b.deleteCharAt(b.length() - 1);
            }
        } finally {
            if (in != null) in.close();
        }
        return ResponseFactory.parse(b.toString());
    }
