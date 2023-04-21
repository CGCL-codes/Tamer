    public void xtestURL() {
        try {
            Iterator urlIter = getUrls().iterator();
            while (urlIter.hasNext()) {
                URL url = (URL) urlIter.next();
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                int error = ((HttpURLConnection) connection).getResponseCode();
                assertTrue((error == 200) || (error == 401) || (error == 302));
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
