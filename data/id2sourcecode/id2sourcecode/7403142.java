    @Override
    public InputStream getByteStream() {
        try {
            URL url = new URL(getSystemId());
            URLConnection conn = url.openConnection();
            InputStream rtn = null;
            InputStream is = null;
            try {
                is = conn.getInputStream();
            } catch (IOException x) {
                System.gc();
                is = conn.getInputStream();
            }
            rtn = new BufferedInputStream(is);
            openedFiles.add(rtn);
            return rtn;
        } catch (MalformedURLException x) {
            throw new RuntimeException(x);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
