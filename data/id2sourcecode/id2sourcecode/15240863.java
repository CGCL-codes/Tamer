    @Override
    protected void writeContents() throws IOException {
        FileUtils.copyFile(DashboardIconFactory.getApplicationIconData(), outStream);
    }
