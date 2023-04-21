    public static boolean download(final String address, final String localFileName) {
        OutputStream out = null;
        URLConnection conn;
        InputStream in = null;
        try {
            final URL url = new URL(address);
            out = new BufferedOutputStream(new FileOutputStream(localFileName));
            conn = url.openConnection();
            in = conn.getInputStream();
            final byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
        } catch (final Exception exception) {
            UpdateUtil.log.info("Downloading failed!");
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ioe) {
                UpdateUtil.log.info("Downloading failed!");
                return false;
            }
            return true;
        }
    }
