    public static void write(String fileName, byte fileData[]) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(new File(fileName));
        ByteBuffer bb = ByteBuffer.wrap(fileData);
        out.getChannel().write(bb);
        out.close();
    }
