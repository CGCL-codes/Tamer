    public String invokeAction(String url, String token) {
        StringBuffer response = null;
        try {
            if (!url.startsWith("https")) {
                throw new MalformedURLException("ITunesU.invokeAction(): URL \"" + url + "\" does not use HTTPS.");
            }
            System.out.println("url in iTunesUUtilities " + url);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.connect();
            OutputStream output = connection.getOutputStream();
            output.write(token.getBytes("UTF-8"));
            output.flush();
            output.close();
            response = new StringBuffer();
            InputStream input = connection.getInputStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            reader = new BufferedReader(reader);
            char[] buffer = new char[16 * 1024];
            for (int n = 0; n >= 0; ) {
                n = reader.read(buffer, 0, buffer.length);
                if (n > 0) response.append(buffer, 0, n);
            }
            input.close();
            connection.disconnect();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new java.lang.AssertionError("ITunesU.invokeAction(): UTF-8 encoding not supported!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new java.lang.AssertionError("ITunesU.invokeAction(): I/O Exception " + e);
        }
        return response.toString();
    }
