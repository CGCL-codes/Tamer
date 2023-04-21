    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 3) {
            System.out.println("Usage: FileUtils <first file> <second file> <type (text|bcp|properties|copy)> <terminator>");
            System.exit(1);
        }
        boolean result = false;
        if (args[2].equalsIgnoreCase("text")) {
            result = FileUtils.compareTextFiles(args[0], args[1]);
            System.out.println("The two " + args[2] + " files " + args[0] + " and " + args[1] + " are identical: " + result);
        } else if (args[2].equalsIgnoreCase("properties")) {
            result = FileUtils.comparePropertiesFiles(args[0], args[1]);
            System.out.println("The two " + args[2] + " files " + args[0] + " and " + args[1] + " are identical: " + result);
        } else if (args[2].equalsIgnoreCase("bcp")) {
            if (args.length == 4) {
                result = FileUtils.compareBCPFiles(args[1], args[0], args[3]);
            } else {
                result = FileUtils.compareBCPFiles(args[1], args[0]);
            }
            System.out.println("The two " + args[2] + " files " + args[0] + " and " + args[1] + " are identical: " + result);
        } else if (args[2].equalsIgnoreCase("copy")) {
            FileUtils.copyFile(args[0], args[1]);
        }
        System.exit(0);
    }
