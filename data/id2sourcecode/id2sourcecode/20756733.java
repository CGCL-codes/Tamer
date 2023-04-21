    public static void compressFileZip(String src, String dst) throws IOException {
        ZipOutputStream output = new ZipOutputStream(new FileOutputStream(new File(dst)));
        output.setLevel(Deflater.BEST_SPEED);
        FileInputStream input = new FileInputStream(new File(src));
        output.putNextEntry(new ZipEntry(src));
        copy(input, output);
        input.close();
        output.close();
    }
