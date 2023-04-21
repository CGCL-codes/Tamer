    @Deprecated
    protected static String readFile(File file) throws IOException {
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        StringWriter outputString = null;
        try {
            inputStream = new BufferedReader(new FileReader(file));
            outputString = new StringWriter();
            outputStream = new PrintWriter(outputString);
            final char[] buffer = new char[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, len);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return outputString.toString();
    }
