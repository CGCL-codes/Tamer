    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please make sure you have input the options: \n java -jar pride-inspector.jar [input file or folder] [1 for PRIDE XML, 2 for mzML] [output file]");
            System.exit(1);
        }
        File inputFile = new File(args[0]);
        int option = Integer.parseInt(args[1]);
        File outputFile = new File(args[2]);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(outputFile));
            if (inputFile.isDirectory()) {
                visitAllFiles(inputFile, option, writer);
            } else {
                readFile(inputFile, option, writer);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
