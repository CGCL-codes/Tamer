    @Override
    public String newDocument_2() {
        try {
            if (null == this.getDoc()) {
                this.addActionError(this.getText("Error.documentoPersonale.nullFile"));
                return INPUT;
            } else {
                String pathOutFile = this.getTempDir().concat(this.getDoc().getName());
                FileUtils.copyFile(new File(this.getDoc().getAbsolutePath()), new File(pathOutFile));
                this.setDocTempFilePath(pathOutFile);
                this.getDocumentoPersonale().setStartDate(new Date());
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "newDocument_2");
            return FAILURE;
        }
        return SUCCESS;
    }
