    private final void onExportCSV() {
        FileDialog fileDialog = new FileDialog(getSite().getShell(), SWT.SAVE);
        fileDialog.setText(LABEL_CSV_EXPORT_FILE_DIALOG);
        fileDialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
        String location = fileDialog.open();
        if (location != null) {
            File exportFile = new File(location);
            String outputString = this.matrixControl.createCSVExportString();
            exportFile.delete();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(exportFile, true);
                FileChannel outChannel = fileOutputStream.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(outputString.length());
                byte[] bytes = outputString.getBytes();
                buffer.put(bytes);
                buffer.flip();
                outChannel.write(buffer);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                CorrelationView.logger.error("A FileNotFoundException " + "occurred during csv export", e);
            } catch (IOException e) {
                CorrelationView.logger.error("A IOException occurred during csv export", e);
            }
        }
    }
