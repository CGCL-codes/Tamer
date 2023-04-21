    private void downloadAndInstallUpdate(@NotNull final String url) {
        final File download = new File(updateFileName + ".tmp");
        final File backup = new File(updateFileName + ".bak");
        final File orig = new File(updateFileName);
        try {
            final InputStream in = openStream(url);
            try {
                final OutputStream out = new FileOutputStream(download);
                try {
                    final byte[] buf = new byte[BUF_SIZE];
                    while (true) {
                        final int bytesRead = in.read(buf);
                        if (bytesRead == -1) {
                            break;
                        }
                        out.write(buf, 0, bytesRead);
                    }
                    out.close();
                    if (!orig.renameTo(backup)) {
                        ACTION_BUILDER.showMessageDialog(parentComponent, "updateFailedNoBackup", updateFileName);
                    } else if (!download.renameTo(orig)) {
                        backup.renameTo(orig);
                        ACTION_BUILDER.showMessageDialog(parentComponent, "updateFailedNoDownload");
                    } else {
                        ACTION_BUILDER.showMessageDialog(parentComponent, "updateRestart", updateFileName);
                        exiter.doExit(0);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } catch (final InterruptedIOException e) {
            ACTION_BUILDER.showMessageDialog(parentComponent, "updateAborted");
        } catch (final Exception e) {
            log.warn("updateError", e);
            ACTION_BUILDER.showMessageDialog(parentComponent, "updateError", e);
        }
    }
