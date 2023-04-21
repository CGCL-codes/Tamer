    private String getLoadStream(URL url) throws IOException {
        String rr = null;
        try {
            java.net.URLConnection urlConnection = url.openConnection();
            int size = (int) urlConnection.getContentLength();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buf = new byte[size];
            int bufSize = inputStream.read(buf, 0, buf.length);
            inputStream.close();
            if (bufSize != size) throw new NullPointerException("Error size in read " + bufSize + " != " + size);
            rr = new String(buf, 0, bufSize);
            rr = new String(" " + rr + " ");
            rr = rr.trim();
        } catch (NullPointerException npe) {
            throw new IOException("Ilegal Argument in Filer " + npe);
        } catch (IndexOutOfBoundsException ioe) {
            throw new IOException("Ilegal Argument in Filer " + ioe);
        }
        return (rr);
    }
