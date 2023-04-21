    private static void writeFile(ByteBuffer byteBuffer, File outputDir, String fileName) throws IOException {
        File outputFile = new File(outputDir, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            FileChannel channel = out.getChannel();
            channel.write(byteBuffer);
            out.close();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
