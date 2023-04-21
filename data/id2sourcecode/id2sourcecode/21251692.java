    private void generateOutput(ArticleForm articleForm, int outputProcessorType, boolean overwriteFile) throws ValidationException, FileAlreadyExistsException {
        opf.newOutputProcessor(outputProcessorType).process(articleForm, overwriteFile);
        int confirmation = JOptionPane.showOptionDialog(parentPanel, I18NResources.getInstance().get(I18NResources.SAVE_SUCCESSFUL_MESSAGE), I18NResources.getInstance().get(I18NResources.QUESTION_MESSAGE_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, I18NResources.getInstance().getDialogYesNoOption(), I18NResources.getInstance().getDialogYesNoOption()[0]);
        if (JOptionPane.OK_OPTION == confirmation) {
            parentPanel.cleanFields();
        }
    }
