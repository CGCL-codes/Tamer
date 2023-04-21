    public String performOpenFile() {
        getGraphicalViewer().getControl().setEnabled(false);
        FileDialog fd = new FileDialog(getSite().getWorkbenchWindow().getShell());
        fd.setFilterExtensions(extensions);
        String res = fd.open();
        fd = null;
        getGraphicalViewer().getControl().setEnabled(true);
        return res;
    }
