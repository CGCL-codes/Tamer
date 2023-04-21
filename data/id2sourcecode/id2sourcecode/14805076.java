    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(new File(args[0]));
        PrintStructure ps = new PrintStructure();
        ps.print(fis.getChannel(), 0, 0);
    }
