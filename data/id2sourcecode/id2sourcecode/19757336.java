    private String jsonImportRemote(String archiveQuery) {
        InputStream in = null;
        String queryResult = "";
        try {
            URL url = new URL(archiveQuery);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.connect();
            in = httpConn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int read = 0;
            int bufSize = 512;
            byte[] buffer = new byte[bufSize];
            while (true) {
                read = bis.read(buffer);
                if (read == -1) {
                    break;
                }
                baf.append(buffer, 0, read);
            }
            queryResult = new String(baf.toByteArray());
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return queryResult;
    }
