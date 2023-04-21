    public static SAMDataSource getSAM(URL url) throws IOException {
        String md5 = MD5Tools.md5(url.toString()) + ".bam";
        if (!contains(md5)) {
            GVProgressBar gv = new GVProgressBar("Downloading data", "Downloading short read data", null);
            StaticUtils.upperRight(gv);
            File tmpBAM = new File(cacheDir, md5 + ".tmp");
            File tmpBAI = new File(cacheDir, md5 + ".bai" + ".tmp");
            URLConnection conn = url.openConnection();
            System.out.println("Downloading: " + conn.getContentLength() + " bytes");
            gv.setMax(conn.getContentLength() / 1024);
            gv.setVisible(true);
            copy(conn.getInputStream(), tmpBAM, gv);
            copy(new URL(url.toString() + ".bai").openStream(), tmpBAI, null);
            tmpBAM.renameTo(new File(cacheDir, md5));
            tmpBAI.renameTo(new File(cacheDir, md5 + ".bai"));
            gv.done();
        }
        return new SAMDataSource(new File(cacheDir, md5));
    }
