    public static boolean UpdateRebaseDBFile() {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(urlAddress);
            File folder = new File(relativeXmlFileLocation);
            if (!folder.exists()) folder.mkdir();
            out = new BufferedOutputStream(new FileOutputStream(relativeDataFileLocation));
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
            int totalSize = Integer.parseInt(conn.getHeaderField("content-length"));
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        return true;
    }
