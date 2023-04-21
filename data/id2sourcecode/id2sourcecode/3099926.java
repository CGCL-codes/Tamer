    @Override
    public boolean performFinish() {
        IPath path = UIActivator.getDefault().getStateLocation().append(UIActivator.SERVICE_XML);
        try {
            FileUtils.copyFile(path.toFile(), servicesExportPage.getFile());
        } catch (IOException e) {
            StatusHandler.fail(e, "Error is occured when exporting service.xml.", true);
            return false;
        }
        return true;
    }
