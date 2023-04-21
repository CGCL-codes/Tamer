    private File getFileAsResource(String reportPath) throws Exception {
        File reportFile;
        String name = reportPath.substring("resource:".length()).trim();
        String localName = name.replace('/', '_');
        log.info("reportPath = " + reportPath);
        log.info("getting resource from = " + getClass().getClassLoader().getResource(name));
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
        String localFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + localName;
        log.info("localFile = " + localFile);
        reportFile = new File(localFile);
        OutputStream out = null;
        out = new FileOutputStream(reportFile);
        if (out != null) {
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
        }
        return reportFile;
    }
