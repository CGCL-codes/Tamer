    private String[] json2list(String url) throws DownloaderException {
        try {
            final HttpClient client = new DefaultHttpClient();
            final HttpResponse response = client.execute(new HttpGet(url));
            final InputStream is = response.getEntity().getContent();
            final int size = (int) response.getEntity().getContentLength();
            final byte[] buffer = new byte[size];
            final BufferedInputStream bis = new BufferedInputStream(is);
            int position = 0;
            while (position < size) {
                final int read = bis.read(buffer, position, buffer.length - position);
                if (read <= 0) break;
                position += read;
            }
            final String strResponse = new String(buffer);
            final JSONArray arr = new JSONArray(strResponse);
            final String[] returnValue = new String[arr.length()];
            for (int i = 0; i < arr.length(); ++i) returnValue[i] = arr.getString(i);
            return returnValue;
        } catch (ClientProtocolException e) {
            throw new DownloaderException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new DownloaderException(e.getMessage(), e);
        } catch (IOException e) {
            throw new DownloaderException(e.getMessage(), e);
        } catch (JSONException e) {
            throw new DownloaderException(e.getMessage(), e);
        }
    }
