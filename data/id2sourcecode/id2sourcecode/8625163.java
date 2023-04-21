    private byte[] loadFileData(String name) throws FileNotFoundException, IOException {
        File file = new File(name);
        FileInputStream fis = new FileInputStream(file);
        long filesize = fis.getChannel().size();
        byte[] result = new byte[(int) filesize];
        fis.read(result);
        fis.close();
        return result;
    }
