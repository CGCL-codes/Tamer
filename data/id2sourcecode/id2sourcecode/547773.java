    private static void cache() {
        OutputStream out = null;
        OutputStream lock = null;
        URLConnection conn = null;
        InputStream in = null;
        String lastUpdate = DateParser.getIsoDate(new Date());
        File cacheDir = new File("cache");
        if (!cacheDir.isDirectory()) cacheDir.mkdir();
        try {
            URL url = new URL("http://data.phishtank.com/data/online-valid/");
            out = new BufferedOutputStream(new FileOutputStream("cache/phishtank.xml"));
            lock = new BufferedOutputStream(new FileOutputStream("cache/update.lock"));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
            lock.write(lastUpdate.getBytes(), 0, lastUpdate.length());
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Cannot cache data!");
            System.exit(-1);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (lock != null) {
                    lock.close();
                }
            } catch (IOException ioe) {
            }
        }
    }
