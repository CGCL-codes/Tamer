    public static void copyFile(String in, File out, boolean read, boolean write, boolean execute) throws FileNotFoundException, IOException {
        copyFile(new File(in), out, read, write, execute);
    }
