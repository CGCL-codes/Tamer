    private void saveContentsToFile(int defaultFilter, String content) throws IOException {
        final XAChooser chooser = new XAChooser(Display.getCurrent().getActiveShell(), "", SWT.SAVE);
        chooser.addDefaultFilter(defaultFilter);
        chooser.addFilter(XAFileConstants.ALL_ONLY);
        final String selectedFilePath = chooser.open();
        if (selectedFilePath != null) {
            FileUtils.copyFile(new ByteArrayInputStream(content.getBytes()), ResourceUtils.getFile(selectedFilePath));
        }
    }
