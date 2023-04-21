    private void initLicenseContent(Composite composite) {
        Text content = configurePanel(composite, SWT.H_SCROLL);
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("COPYING");
            if (url != null) {
                InputStream in = url.openStream();
                FileTool tool = FileTool.getInstance();
                content.setText(tool.getInputStreamContent(in, true));
            } else {
                content.setText("No license file found !");
            }
        } catch (IOException e) {
            application.handleException("Error reading license file", e);
        }
    }
