    protected String processScriptReferences(String body) throws IOException {
        Matcher m = SCRIPT_REFERENCE.matcher(body);
        StringBuffer out = new StringBuffer();
        while (m.find()) {
            String scriptBody = null;
            String src = m.group(1);
            if (src.startsWith(CLASSPTAH_PREFIX)) {
                src = src.substring(CLASSPTAH_PREFIX.length());
                InputStream is = getClass().getClassLoader().getResourceAsStream(src);
                if (is == null) {
                    getLog().warn("Unable to find referenced script on classpath: " + src);
                } else {
                    scriptBody = IOUtils.toString(is);
                }
            }
            if (src.startsWith(INLINE_PREFIX)) {
                src = src.substring(INLINE_PREFIX.length());
                URL url = new URL(src);
                InputStream is = null;
                try {
                    is = url.openStream();
                    scriptBody = IOUtils.toString(is);
                } catch (IOException ioe) {
                    getLog().error("Error loading script from: " + src + "[" + ioe.getClass().getName() + ":" + ioe.getMessage() + "]");
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } else {
                File f = new File(scontrolsDirectory + File.separator + src);
                if (f.exists()) {
                    scriptBody = FileUtils.readFileToString(f);
                } else {
                    if (src.startsWith("..")) {
                        getLog().warn("SCRIPT NOT FOUND: " + f.getCanonicalPath());
                    }
                }
            }
            if (scriptBody != null) {
                m.appendReplacement(out, "<script type=\"text/javascript\">\n" + Utils.escapeReplacement(scriptBody) + "\n</script>");
            }
        }
        m.appendTail(out);
        return out.toString();
    }
