    private String GetHtmlSource() {
        BufferedInputStream reader = null;
        String source = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            reader = new BufferedInputStream(connection.getInputStream());
            connection.setReadTimeout(10000);
            int c = 0;
            source = "";
            while ((c = reader.read()) != -1) source = source + (char) c;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return source;
    }
