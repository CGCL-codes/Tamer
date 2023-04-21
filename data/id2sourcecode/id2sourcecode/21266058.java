    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println(montyLingua.tag_text("hello"));
            System.out.println(montyLingua.tag_text("world"));
            log.info("Usage:\t1. java POSTagger [input_file] [output_file]\n\t2. java POSTagger [input_dir]  [output_dir]\n\t3. java POSTagger [input_file] [output_dir]");
            return;
        }
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        if (!inFile.exists()) {
            log.fatal("Error: File " + inFile + " could not be found!");
            return;
        }
        if (inFile.isFile()) {
            if (outFile.isDirectory()) outFile = new File(outFile.getPath() + File.separator + inFile.getName());
            writeFile(new File(outFile.getName() + ".l"), POSTag(IOUtil.readFile(inFile)));
        } else if (inFile.isDirectory()) {
            if (!outFile.exists()) outFile.mkdir();
            File[] fileList = inFile.listFiles();
            for (int i = 0; i < fileList.length; i++) if (fileList[i].isFile()) {
                File outTo = new File(outFile.getPath() + File.separator + fileList[i].getName());
                log.debug("tagging " + fileList[i]);
                writeFile(outTo, POSTag(IOUtil.readFile(fileList[i])));
            }
        }
    }
