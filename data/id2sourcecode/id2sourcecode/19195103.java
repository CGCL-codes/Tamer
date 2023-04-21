            public void run() {
                bgProgress.setIndeterminate(true);
                try {
                    final File file = exportDialog.getSelectedFile();
                    final String baseName = Utils.baseName(file.getName());
                    MifWriter.writeMIF(file.getParentFile(), baseName, MifWriter.mifFromMap(T3DIO.readMap(file)));
                } catch (IOException e) {
                    Utils.showWarning(mainPanel, "Error while writing MIF file.");
                } catch (T3DException e) {
                    Utils.showWarning(mainPanel, "Error while reading map:<br>" + e.getMessage());
                } finally {
                    progressComplete();
                }
            }
