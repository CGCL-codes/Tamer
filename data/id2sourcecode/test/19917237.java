    @NotNull
    private InputStream openStream(@NotNull final String url) throws IOException {
        final Proxy proxy = NetPreferences.getProxy();
        final URLConnection con = new URL(url).openConnection(proxy);
        final ProgressMonitorInputStream stream = new ProgressMonitorInputStream(parentComponent, ActionBuilderUtils.getString(ACTION_BUILDER, "updateProgress.title"), con.getInputStream());
        final ProgressMonitor monitor = stream.getProgressMonitor();
        monitor.setMaximum(con.getContentLength());
        monitor.setNote(ActionBuilderUtils.getString(ACTION_BUILDER, "updateProgress"));
        monitor.setMillisToDecideToPopup(10);
        monitor.setMillisToPopup(10);
        return stream;
    }
