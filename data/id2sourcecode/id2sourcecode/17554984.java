    public void openTemplate(String templateFilename) throws UnsupportedEncodingException, IOException {
        writeLog(0, "Template Filename : " + templateFilename);
        try {
            templateFile = new File(templateFilename);
            templateFis = new FileInputStream(templateFile);
            templateIsr = new InputStreamReader(templateFis, "UTF-8");
        } catch (FileNotFoundException ex) {
            URL url = ClassLoader.getSystemResource(templateFilename);
            templateIs = url.openStream();
            templateIsr = new InputStreamReader(templateIs, "UTF-8");
        }
        templateReader = new BufferedReader(templateIsr, readHeadSize);
        log.info("Generation of: '" + targetFilename + "'");
        targetFile = new File(targetFilename);
        targetFos = new FileOutputStream(targetFile);
        targetWriter = new OutputStreamWriter(targetFos, "UTF-8");
    }
