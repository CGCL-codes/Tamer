    void handleDragStart(DragSourceEvent evt) {
        final ArrayList<Transfer> transfers = new ArrayList<Transfer>(3);
        if (this.layout.cmdEditExport().getEnabled()) {
            final FileScannerResult result = getSelectedResult();
            if (result != null) {
                final String tmpDir = System.getProperty("java.io.tmpdir");
                File file = new File(tmpDir, result.name());
                try {
                    if (!file.createNewFile()) {
                        file = null;
                    }
                } catch (final IOException e) {
                    Debug.exception(e);
                    file = null;
                }
                FileOutputStream fileOS = null;
                FileChannel fileOSChannel = null;
                try {
                    if (file == null) {
                        file = File.createTempFile(result.name(), "");
                    }
                    fileOS = new FileOutputStream(file);
                    fileOSChannel = fileOS.getChannel();
                    final RuntimeLimiter limiter = new RuntimeLimiter(1000);
                    long transferred = 0;
                    final long resultSize = result.end() - result.start();
                    while (transferred < resultSize && !limiter.limit()) {
                        final long position = result.start() + transferred;
                        transferred += result.input().transferTo(position, Math.min(4096, resultSize - position), fileOSChannel);
                    }
                    if (transferred < resultSize) {
                        file.delete();
                        file = null;
                    }
                } catch (final IOException e) {
                    LOG.error("An I/O error occured while writing result ''{0}'' to file ''{1}''.", result, file);
                    file = null;
                } finally {
                    fileOSChannel = Closeables.saveClose(fileOSChannel);
                    fileOS = Closeables.saveClose(fileOS);
                }
                this.dataDragSourceFile = file;
            }
            if (this.dataDragSourceFile != null) {
                transfers.add(FileTransfer.getInstance());
            } else {
                cmdEditExport();
            }
        }
        this.dataDragSource.setTransfer(transfers.toArray(new Transfer[transfers.size()]));
        evt.doit = transfers.size() > 0;
    }
