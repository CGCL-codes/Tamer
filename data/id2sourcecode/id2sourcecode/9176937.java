    public static void copyFile(String in, String out, boolean read, boolean write, boolean execute) throws FileNotFoundException, IOException {
        copyFile(new File(in), new File(out), read, write, execute);
    }
