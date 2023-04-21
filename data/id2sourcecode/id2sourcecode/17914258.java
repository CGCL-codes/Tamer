    public String getBlurb(String id) {
        URL url;
        try {
            url = new URL(String.format(blurbURLFormat, id));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }
            InputStream is = connection.getInputStream();
            try {
                Writer writer = new StringWriter();
                char[] buffer = new char[1300];
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                return writer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
