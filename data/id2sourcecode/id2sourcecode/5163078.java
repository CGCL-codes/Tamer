    public void downloadCurrentSchemaInstance() {
        FacesContext cntxt = FacesContext.getCurrentInstance();
        HttpServletResponse res = (HttpServletResponse) cntxt.getExternalContext().getResponse();
        long revisionNumber = -2;
        String filePath = configFile.getInputFilesSvnPath() + "/" + configFile.getAuditSchemaFileName();
        logger.info("archived schema instance file: svn file path=" + filePath);
        revisionNumber = saasSubversionServiceBean.getLastestRevisionNumber(filePath);
        logger.info("Latest Revision Number=" + revisionNumber);
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
                logger.info("locally saved current audit schema instance is available");
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
                logger.info("locally saved current audit schema instance is not available");
                logger.info("get a copy of the instance from the subversion repository");
                baos = saasSubversionServiceBean.checkoutByRevisionNumber(filePath, revisionNumber);
                res.setContentType("application/xml");
                res.setHeader("content-disposition", "attachment; filename=" + fileNameWithRevisionNumber);
                out = res.getOutputStream();
                out.write(baos.toByteArray());
            }
            logger.info("finish downloading the current audit schema instance file");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Download Request: ", "Completed"));
        } catch (IOException e) {
            logger.warning("failed to return the requested audit schema instance file");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Download Request Failed: ", "Reason: IOException occurred"));
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
