    protected void createFile(File f, String template) throws IOException, TemplateException {
        File fTmp = new File(fTmpDir, f.getName());
        logger.debug("Using tmp-Dir: " + fTmpDir);
        logger.debug("f.getName()=" + f.getName());
        logger.debug("ftmp=" + fTmp.getAbsolutePath());
        Template ftl = freemarkerConfiguration.getTemplate(template, "UTF-8");
        ftl.setEncoding("UTF-8");
        StringWriter sw = new StringWriter();
        ftl.process(freemarkerNodeModel, sw);
        sw.flush();
        StringIO.writeTxt(fTmp, sw.toString());
        boolean doCopy = false;
        if (!f.exists()) {
            logger.debug(f.getAbsolutePath() + " does not exist, so COPY");
            doCopy = true;
        } else {
            String hashExisting = FileIO.getHash(f);
            String hashNew = FileIO.getHash(fTmp);
            logger.debug("hashExisting " + hashExisting);
            logger.debug("hashNew      " + hashNew);
            if (!hashExisting.equals(hashNew)) {
                doCopy = true;
            }
            logger.debug("Hash evaluated: COPY:" + doCopy);
        }
        if (doCopy) {
            FileUtils.copyFile(fTmp, f);
        } else {
            logger.debug("Dont copy");
        }
    }
