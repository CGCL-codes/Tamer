    public static void main(String[] args) throws IOException {
        SWFWriter writer = new SWFWriter(args[1]);
        if (args.length > 2) {
            if (args[2].equals("+")) writer.setCompression(true); else if (args[2].equals("-")) writer.setCompression(false);
        }
        SWFReader reader = new SWFReader(writer, args[0]);
        reader.readFile();
    }
