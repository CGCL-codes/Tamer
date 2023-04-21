    public static TestResponse delete(String urlString) throws IOException {
        HttpURLConnection httpCon = null;
        byte[] errorResult = null;
        try {
            URL url = new URL(urlString);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("DELETE");
        } catch (IOException e) {
            e.printStackTrace();
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
