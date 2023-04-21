    private static boolean dumpFile(String inJarName, String destination) {
        File f = new File(destination);
        try {
            f.createNewFile();
        } catch (Exception e) {
            ErrorManagement.handleException("Can't create output file to copy from JAR.", e);
            return false;
        }
        InputStream source = JBidWatch.class.getClass().getResourceAsStream(inJarName);
        if (source == null) {
            ErrorManagement.logDebug("Failed to open internal resource!");
        }
        BufferedInputStream in = new BufferedInputStream(source);
        try {
            FileOutputStream out = new FileOutputStream(f);
            int ch;
            while ((ch = in.read()) != -1) out.write(ch);
            try {
                in.close();
                out.close();
            } catch (Exception e) {
            }
        } catch (IOException e) {
            ErrorManagement.handleException("Couldn't extract file (" + inJarName + " from jar to " + destination + ".", e);
            return false;
        }
        return true;
    }
