    private static void saveEntry(final ZipFile zf, final File targetDir, final ZipEntry target) throws ZipException, IOException {
        final File file = new File(targetDir.getAbsolutePath() + "/" + target.getName());
        if (target.isDirectory()) file.mkdirs(); else {
            final InputStream is = zf.getInputStream(target);
            final BufferedInputStream bis = new BufferedInputStream(is);
            new File(file.getParent()).mkdirs();
            final FileOutputStream fos = new FileOutputStream(file);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            final int EOF = -1;
            for (int c; (c = bis.read()) != EOF; ) bos.write((byte) c);
            bos.close();
            fos.close();
        }
    }
