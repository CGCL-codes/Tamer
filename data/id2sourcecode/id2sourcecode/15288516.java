    private void save() {
        final String baseName = StringUtils.isBlank(fileName) ? getFirstWord() : fileName.substring(0, fileName.indexOf("."));
        final String text = textArea.getText();
        (new SwingWorker<String, Object>() {

            /** @{inheritDoc} */
            public String doInBackground() {
                try {
                    File file = new File(baseName + ".txt");
                    File revisions = new File(baseName + "-revisions");
                    revisions.mkdir();
                    File latestRevision = new File(revisions, baseName + "-rev" + System.currentTimeMillis() + ".txt");
                    if (file.exists()) {
                        FileUtils.copyFile(file, latestRevision);
                    }
                    FileUtils.writeStringToFile(file, text);
                    return "Saved";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Not saved";
                }
            }
        }).execute();
    }
