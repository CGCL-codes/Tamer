    public boolean exportToOoWriter(File file, StringWriter writer, String xslts) throws IOException {
        boolean resultValue = true;
        ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(file));
        Result result = new StreamResult(zipout);
        StringTokenizer tokenizer = new StringTokenizer(xslts, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] files = token.split("->");
            if (files.length == 2) {
                ZipEntry entry = new ZipEntry(files[1]);
                zipout.putNextEntry(entry);
                if (files[0].endsWith(".xsl")) {
                    logger.info("Transforming with xslt " + files[0] + " to file " + files[1]);
                    resultValue &= applyXsltFile(files[0], writer, result);
                } else {
                    logger.info("Copying resource from " + files[0] + " to file " + files[1]);
                    resultValue &= copyFromResource(files[0], zipout);
                }
                zipout.closeEntry();
            }
        }
        zipout.close();
        return resultValue;
    }
