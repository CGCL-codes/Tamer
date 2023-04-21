    public boolean checkForUpdate() throws IOException {
        try {
            URL url = new URL("http://www.broad.mit.edu/mpg/haploview/uc/version.txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-agent", Constants.USER_AGENT);
            con.connect();
            int response = con.getResponseCode();
            if ((response != HttpURLConnection.HTTP_ACCEPTED) && (response != HttpURLConnection.HTTP_OK)) {
                throw new IOException("Could not connect to update server.");
            } else {
                InputStream inputStream = con.getInputStream();
                byte[] buf = new byte[200];
                int size = con.getContentLength();
                int read;
                if (size > 200) {
                    read = inputStream.read(buf, 0, 200);
                } else {
                    read = inputStream.read(buf, 0, size);
                }
                String data = "";
                if (read != 0) {
                    data = new String(buf);
                    double newestVersion = Double.parseDouble(data);
                    if (newestVersion > Constants.VERSION) {
                        this.newVersion = newestVersion;
                        this.newVersionAvailable = true;
                    } else {
                        this.newVersionAvailable = false;
                        this.newVersion = Constants.VERSION;
                    }
                }
            }
            con.disconnect();
        } catch (MalformedURLException mue) {
        }
        return this.newVersionAvailable;
    }
