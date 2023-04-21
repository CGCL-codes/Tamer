    public static void saveEntry(ZipFile zf, ZipEntry target, String destinationDirectory) throws ZipException, IOException {
        File file = new File(destinationDirectory + System.getProperty("file.separator") + target.getName());
        if (target.isDirectory()) file.mkdirs(); else {
            InputStream is = zf.getInputStream(target);
            BufferedInputStream bis = new BufferedInputStream(is);
            new File(file.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            final int EOF = -1;
            for (int c; (c = bis.read()) != EOF; ) bos.write((byte) c);
            is.close();
            bis.close();
            bos.close();
            fos.close();
        }
    }
