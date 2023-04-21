    public static void renderFile(String dataResourceTypeId, String objectInfo, String rootDir, Writer out) throws GeneralException, IOException {
        if (dataResourceTypeId.equals("LOCAL_FILE")) {
            File file = new File(objectInfo);
            if (!file.isAbsolute()) {
                throw new GeneralException("File (" + objectInfo + ") is not absolute");
            }
            int c;
            FileReader in = new FileReader(file);
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } else if (dataResourceTypeId.equals("OFBIZ_FILE")) {
            String prefix = System.getProperty("ofbiz.home");
            String sep = "";
            if (objectInfo.indexOf("/") != 0 && prefix.lastIndexOf("/") != (prefix.length() - 1)) {
                sep = "/";
            }
            File file = new File(prefix + sep + objectInfo);
            int c;
            FileReader in = new FileReader(file);
            while ((c = in.read()) != -1) out.write(c);
        } else if (dataResourceTypeId.equals("CONTEXT_FILE")) {
            String prefix = rootDir;
            String sep = "";
            if (objectInfo.indexOf("/") != 0 && prefix.lastIndexOf("/") != (prefix.length() - 1)) {
                sep = "/";
            }
            File file = new File(prefix + sep + objectInfo);
            int c;
            FileReader in = null;
            try {
                in = new FileReader(file);
                String enc = in.getEncoding();
                if (LOG.isInfoEnabled()) {
                    LOG.info("in serveImage, encoding:" + enc);
                }
            } catch (FileNotFoundException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(" in renderDataResourceAsHtml(CONTEXT_FILE), in FNFexception:", e);
                }
                throw new GeneralException("Could not find context file to render", e);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(" in renderDataResourceAsHtml(CONTEXT_FILE), got exception:", e);
                }
            }
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }
    }
