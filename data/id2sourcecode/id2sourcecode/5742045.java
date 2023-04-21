    private String readRequest(File f) throws IOException {
        String retval = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            StringWriter sr = new StringWriter();
            String nextLine = null;
            while ((nextLine = br.readLine()) != null) {
                sr.write(nextLine + "\n");
            }
            retval = sr.toString();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return retval;
    }
