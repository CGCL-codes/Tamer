    private void saveTempMainDocument() {
        try {
            String userOption = this.getMainDocumentOption();
            String tempFileName = this.createMainDocumentTempFileName();
            if (userOption.equals(MAIN_DOCUMENT_FILE_OPTION)) {
                if (this.getMainDoc().getAbsolutePath().compareTo(this.getTempDir().concat(tempFileName)) != 0) {
                    FileUtils.copyFile(new File(this.getMainDoc().getAbsolutePath()), new File(this.getTempDir().concat(tempFileName)));
                }
            } else if (userOption.equals(MAIN_DOCUMENT_NOTES_OPTION)) {
                String fullPath = this.getTempDir().concat(tempFileName);
                FileWriter fstream = new FileWriter(fullPath);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(this.getNote());
                out.close();
                this.setMainDocFileName(tempFileName);
            } else if (userOption.equals(MAIN_DOCUMENT_MAIL_OPTION)) {
                MessageAttachment attach = getPecManagerIN().extractMainDocument(this.getMailId());
                if (attach != null) {
                    String fullPath = this.getTempDir().concat(attach.getFileName());
                    FileUtil.writeFile(attach.getInputStream(), fullPath);
                    this.setMainDocFileName(attach.getFileName());
                    tempFileName = attach.getFileName();
                } else {
                    SmallMessageIN messageIn = getPecManagerIN().getEmailIN(this.getMailId());
                    String fullPath = this.getTempDir().concat(tempFileName);
                    FileWriter fstream = new FileWriter(fullPath);
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(messageIn.getSubject());
                    out.close();
                    this.setMainDocFileName(tempFileName);
                }
            }
            this.setMainDocumentTempFilePath(tempFileName);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "saveMainDocument");
            throw new RuntimeException("Errore in salvataggio file principale del protocollo su cartella temporanea");
        }
    }
