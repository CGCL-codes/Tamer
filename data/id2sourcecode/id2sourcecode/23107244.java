    public static void copy(File source, File target) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(target);
        fis.getChannel().transferTo(0, source.length(), fos.getChannel());
        fis.close();
        fos.flush();
        fos.close();
    }
