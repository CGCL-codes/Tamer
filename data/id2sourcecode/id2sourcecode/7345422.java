    public T fecthAndParse(String url, Boolean followRedirects) throws ParseException, IOException {
        if (followRedirects == null) {
            followRedirects = true;
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setDoInput(true);
            prepareConnection(connection, followRedirects);
            if (shouldDoOutput()) {
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                try {
                    writeOutput(os);
                } finally {
                    Execute.close(os);
                }
            }
            if (responseHandler == null || responseHandler.handleResponse(connection.getResponseCode())) {
                InputStream is = connection.getInputStream();
                try {
                    return parse(url, is, connection);
                } finally {
                    Execute.close(is);
                }
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }
