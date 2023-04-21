    public static long download(String address, String localFileName) throws IOException {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        long numWritten = 0;
        try {
            System.out.println("Starting Download: " + address);
            URL url = new URL(address);
            out = new BufferedOutputStream(new FileOutputStream(localFileName));
            conn = url.openConnection();
            conn.setConnectTimeout(10000);
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
            System.out.println("Download finished: " + address);
            System.out.println(localFileName + "\t" + numWritten);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException(exception.getMessage());
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
        return numWritten;
    }
