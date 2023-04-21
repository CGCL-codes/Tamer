    public static String downImage(URL imageurl) {
        String filename = null;
        try {
            String tmpname = imageurl.getFile();
            String pattern = "[/]";
            Pattern splitter = Pattern.compile(pattern);
            String[] result = splitter.split(tmpname);
            filename = CrawlerConfig.TMPDIR + File.separatorChar + "images" + result[result.length - 1];
            InputStream in = imageurl.openStream();
            byte[] buffer = new byte[8192];
            FileOutputStream out = new FileOutputStream(new File(filename));
            int _tmp = 0;
            while ((_tmp = in.read(buffer)) > 0) {
                out.write(buffer, 0, _tmp);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            Log.imagelogger.warn("Error downloading image", e);
        }
        return filename;
    }
