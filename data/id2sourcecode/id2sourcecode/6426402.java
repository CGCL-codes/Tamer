    private String getIniFile(File iniFile) throws IOException {
        BufferedReader in = null;
        String inputtext = "";
        FileInputStream fis = new FileInputStream(iniFile);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb;
        bb = ByteBuffer.allocate((int) fc.size());
        fc.read(bb);
        fc.close();
        inputtext = new String(bb.array());
        String tagregex = "#.*\n";
        Pattern p2 = Pattern.compile(tagregex);
        Matcher m2 = p2.matcher(inputtext);
        inputtext = m2.replaceAll("");
        return inputtext;
    }
