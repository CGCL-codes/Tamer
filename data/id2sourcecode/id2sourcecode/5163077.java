    public void downloadRequestedArchivedSchemaInstance() {
        FacesContext cntxt = FacesContext.getCurrentInstance();
        HttpServletResponse res = (HttpServletResponse) cntxt.getExternalContext().getResponse();
        long revisionNumber = -2;
        Map<String, String> params = cntxt.getExternalContext().getRequestParameterMap();
        revisionNumber = Long.parseLong(params.get("archivedAuditSchemaInstanceFileRevisionNumber"));
        logger.info("param: archivedAuditSchemaInstanceFileRevisionNumber=" + revisionNumber);
        String filePath = configFile.getInputFilesSvnPath() + "/" + configFile.getAuditSchemaFileName();
        logger.info("archived schema instance file: svn file path=" + filePath);
        if (revisionNumber <= 0) {
            logger.warning("revision Number is not positive abort the downloading");
            return;
        }
        OutputStream out = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fileNameWithRevisionNumber = configFile.getAuditSchemaFileNameBase() + ".revision_" + revisionNumber + ".xml";
        String locallySavedFilePath = configFile.getAuditSchemaFileDir() + "/" + fileNameWithRevisionNumber;
        try {
            File locallSavedCopy = new File(locallySavedFilePath);
            if (locallSavedCopy.exists() && locallSavedCopy.length() > 0) {
                in = new FileInputStream(locallSavedCopy);
                res.setContentType("application/xml");
                res.setHeader("content-disposition", "attachment; filename=" + fileNameWithRevisionNumber);
                out = res.getOutputStream();
                byte[] buf = new byte[1024];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            } else {
                baos = saasSubversionServiceBean.checkoutByRevisionNumber(filePath, revisionNumber);
                res.setContentType("application/xml");
                res.setHeader("content-disposition", "attachment; filename=" + fileNameWithRevisionNumber);
                out = res.getOutputStream();
                out.write(baos.toByteArray());
            }
            logger.info("finish downloading the requested arhived audit schema instance file");
        } catch (IOException e) {
            logger.warning("failed to return the requested audit schema instance file");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    logger.warning("failed to close the input stream for the requested archived file");
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    logger.warning("failed to close the ouput stream for the requested archived file");
                }
            }
        }
        cntxt.responseComplete();
    }
