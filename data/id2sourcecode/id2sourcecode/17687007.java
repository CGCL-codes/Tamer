    public String readUmpleFile(String[] args, PrintStream writer) {
        if (args.length > 0) {
            return args[0];
        } else {
            writer.println("Please specify the file to compile:");
            return readLine();
        }
    }
