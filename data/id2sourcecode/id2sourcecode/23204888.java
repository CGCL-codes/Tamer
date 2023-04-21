    protected static String file2string(java.io.File file) {
        java.io.StringWriter sw = new java.io.StringWriter();
        char buff[] = new char[1024];
        try {
            java.io.FileReader fr = new java.io.FileReader(file);
            int nRead;
            while ((nRead = fr.read(buff)) > 0) sw.write(buff, 0, nRead);
            fr.close();
        } catch (java.io.IOException ioe) {
            return "CONFIG FILE READ FAILED!!!";
        }
        return sw.toString();
    }
