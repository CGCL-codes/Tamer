    public void createFile(String className, String fileName, String fileURL) throws Exception {
        System.out.println("Creating " + fileName);
        System.out.println("    from " + fileURL);
        File outFile = new File(fileName);
        if (!outFile.exists()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            bw.write(Sysutil.readUrl(fileURL));
            bw.close();
            bluej.getCurrentPackage().newClass(className);
        } else {
            throw new Exception("File already exists");
        }
    }
