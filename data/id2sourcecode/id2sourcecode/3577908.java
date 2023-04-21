    public static TestResponse put(String urlString, byte[] data, String contentType) throws IOException {
        HttpURLConnection httpCon = null;
        byte[] errorResult = null;
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Type", contentType);
            OutputStream output = httpCon.getOutputStream();
            output.write(data);
            output.close();
        } catch (IOException e) {
        } finally {
            InputStream errorStream = httpCon.getErrorStream();
            if (errorStream != null) {
                BufferedInputStream errorIn = new BufferedInputStream(errorStream);
                ByteArrayOutputStream errorOs = new ByteArrayOutputStream();
                int errorNext = errorIn.read();
                while (errorNext > -1) {
                    errorOs.write(errorNext);
                    errorNext = errorIn.read();
                }
                errorOs.flush();
                errorResult = errorOs.toByteArray();
                errorOs.close();
            }
            return new TestResponse(httpCon.getResponseCode(), errorResult, null);
        }
    }
