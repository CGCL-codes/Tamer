    private InputStream openConnection(URL url) throws RTDException {
        int response = -1;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            connection.setAllowUserInteraction(false);
            connection.setRequestMethod("GET");
            connection.connect();
            response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                return (InputStream) connection.getInputStream();
            } else {
                Log.e(Program.LOG, "HTTP Error: " + response);
                throw new RTDException(Program.Error.HTTP_EXCEPTION, R.string.error_default, true, false);
            }
        } catch (MalformedURLException e) {
            Log.e(Program.LOG, "Problem forming URL: " + request + "\n" + e.getMessage());
            throw new RTDException(Program.Error.MALFORMED_URL, R.string.error_default, true, false, e);
        } catch (IOException e) {
            Log.e(Program.LOG, "Problem opening the connection: " + e.getMessage());
            throw new RTDException(Program.Error.IO_EXCEPTION, R.string.error_default, true, false, e);
        }
    }
