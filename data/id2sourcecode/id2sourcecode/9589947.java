    public static ProjectInstaller getProjectInstaller(String fileName) throws Exception {
        InputStream is = null;
        boolean isUrl = FileSystem.isStrUrl(fileName);
        URL url = null;
        File f = null;
        if (isUrl) {
            url = new URL(fileName);
            is = url.openStream();
        } else {
            f = new File(fileName);
            is = new FileInputStream(f);
        }
        org.jdom.Document doc = JDomUtility.getSAXBuilder().build(is);
        edu.xtec.util.JDomUtility.clearNewLineElements(doc.getRootElement());
        is.close();
        ProjectInstaller result = getProjectInstaller(doc.getRootElement());
        String from = null;
        String fName = null;
        if (isUrl) {
            String s0 = url.toExternalForm();
            int k = s0.lastIndexOf('/');
            if (k < 0) throw new Exception("Unable to get install store path from " + s0);
            from = s0.substring(0, k);
            fName = s0.substring(k + 1);
        } else {
            from = f.getParent();
            fName = f.getName();
        }
        if (from != null) result.from = from;
        if (fName != null) result.fName = fName;
        return result;
    }
