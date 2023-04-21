    public static String getHttpContent(String url, String charSet) {
        HttpURLConnection connection = null;
        String content = "";
        try {
            URL address_url = new URL(url);
            connection = (HttpURLConnection) address_url.openConnection();
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            int response_code = connection.getResponseCode();
            if (response_code == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, charSet));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    content += line;
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }
