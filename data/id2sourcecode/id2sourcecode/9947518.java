    private static File downloadToTemp(String fileName) throws IOException, XLWrapException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (!tmpDir.endsWith(File.pathSeparator)) tmpDir = "/" + tmpDir;
        URL url = new URL(fileName);
        InputStream in = url.openStream();
        String file = null;
        Matcher m = Pattern.compile("^.*\\/(.*)$").matcher(fileName);
        if (m.find()) file = m.group(1);
        File tmp = new File(tmpDir + file);
        BufferedOutputStream out;
        try {
            out = new BufferedOutputStream(new FileOutputStream(tmp));
        } catch (IOException e) {
            throw new XLWrapException("Failed to download " + fileName + ", cannot write into temp directory (" + tmpDir + file + ".");
        }
        if (!tmp.canWrite()) throw new XLWrapException("Failed to download " + fileName + ", cannot write into temp directory " + tmpDir + ".");
        int b;
        while ((b = in.read()) >= 0) out.write(b);
        in.close();
        out.close();
        return tmp;
    }
