    public static void copyFile(File in, String out, boolean read, boolean write, boolean execute) throws FileNotFoundException, IOException {
        copyFile(in, new File(out), read, write, execute);
    }
