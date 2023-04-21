    @Override
    public void run() {
        if (getGraphicalViewer() == null) return;
        try {
            String fname = openFileDialog();
            if (fname != null && (!new File(fname).exists() || MessageDialog.openConfirm(window.getShell(), "文件覆盖确认", "文件已经存在，是否覆盖?"))) {
                exportImage(fname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
