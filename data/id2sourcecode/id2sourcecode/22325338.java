    private void readLargeFileWithMonitor(final PrintWriter writer) {
        IRunnableWithProgress runnable = new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(PDERuntimeMessages.OpenLogDialog_message, IProgressMonitor.UNKNOWN);
                try {
                    readLargeFile(writer);
                } catch (IOException e) {
                    writer.println(PDERuntimeMessages.OpenLogDialog_cannotDisplay);
                }
            }
        };
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getParentShell());
        try {
            dialog.run(true, true, runnable);
        } catch (InvocationTargetException e) {
        } catch (InterruptedException e) {
        }
    }
