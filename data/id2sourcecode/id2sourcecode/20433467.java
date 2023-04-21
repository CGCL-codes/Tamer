    private Object concreteSending(Map<String, Object> obj) throws IOException, ClassNotFoundException, ServerException {
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = null;
            try {
                httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod(HTTP_METHOD_POST);
                httpConnection.setDoOutput(true);
                OutputStream outStream = httpConnection.getOutputStream();
                ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
                objOutStream.writeObject(obj);
                httpConnection.connect();
                int response = httpConnection.getResponseCode();
                if (HttpURLConnection.HTTP_OK != response) {
                    throw new ServerException("Bad response from server, response was: " + response);
                }
                InputStream is = httpConnection.getInputStream();
                ObjectInputStream objInStream = new ObjectInputStream(is);
                Object result = objInStream.readObject();
                return result;
            } finally {
                if (httpConnection != null) httpConnection.disconnect();
            }
        } else {
            throw new IllegalStateException("An unvalid url was given and a non http connection was opened");
        }
    }
