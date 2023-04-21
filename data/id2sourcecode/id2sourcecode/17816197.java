    public void redirectToPostmaster() throws IOException {
        File smtpDirectory = new File(configurationManager.getAmavisSMTPDirectory());
        File messageFile = new File(smtpDirectory, getMessageLocation().getName());
        FileUtils.copyFile(getMessageLocation(), messageFile);
        deleteMessage();
    }
