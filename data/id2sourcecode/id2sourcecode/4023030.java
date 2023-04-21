    public InputStream getInputStreamOfZipRulesFile(String fileName) throws IOException {
        InputStream is = null;
        try {
            URL url = Class.forName("rj.tools.jcsc.rules.RuleConstants").getResource(fileName);
            is = url.openStream();
        } catch (ClassNotFoundException e) {
            throw new IOException("rj.tools.jcsc.rules.RuleConstants class could not be found");
        }
        return is;
    }
