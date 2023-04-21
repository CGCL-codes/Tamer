    private void analyseLocalFileList() {
        File updateDir = new File(updateDirectory);
        updateDir.mkdir();
        File deleteFile = new File(updateDirectory + System.getProperty("file.separator") + deleteListFile);
        deleteFile.delete();
        try {
            deleteFile.createNewFile();
        } catch (IOException e1) {
            Logger.err(threadName + "Could not create version file: " + deleteFile.getAbsolutePath());
        }
        try {
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(deleteFile), "UTF8"));
            for (int i = 0; i < localFilesList.size(); i++) {
                UpdateFile currentLocalFile = localFilesList.get(i);
                if (!remoteFilesList.contains(currentLocalFile)) {
                    pw.write(currentLocalFile.getName());
                    pw.newLine();
                }
            }
            pw.flush();
            pw.close();
        } catch (UnsupportedEncodingException e1) {
            Logger.err(threadName + "Could not write file with UTF8 encoding");
        } catch (FileNotFoundException e1) {
            Logger.err(threadName + "Could not find file " + deleteFile);
        } catch (IOException e) {
            Logger.err(threadName + "Could not write to file " + deleteFile);
        }
    }
