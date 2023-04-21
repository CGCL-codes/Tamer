    private String[] saveFiles(final HashMap files) throws java.io.IOException {
        ArrayList filesExported = new ArrayList();
        try {
            setMessage("Writing Files");
            setProgressMaximum(files.size());
            int progress = 0;
            setProgressValue(progress);
            Iterator fileNames = files.keySet().iterator();
            int result = 0;
            while (fileNames.hasNext()) {
                String currentFileName = fileNames.next().toString();
                File currentFile = new File(currentFileName);
                if (currentFile.exists()) {
                    if (result != 1 && result != 3) {
                        String[] options = { "Yes", "Yes To All", "No", "No To All", "Cancel" };
                        StringBuffer message = new StringBuffer(currentFile.getAbsolutePath());
                        message.append(" already exists. Do you want to overwrite it?");
                        result = JOptionPane.showOptionDialog(null, message.toString(), "File Exists", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    }
                    if (result == 2 || result == 3) continue;
                    if (result == 4) break;
                }
                BufferedWriter oStream = new BufferedWriter(new FileWriter(currentFile));
                try {
                    oStream.write(files.get(currentFileName).toString());
                    oStream.flush();
                } finally {
                    oStream.close();
                }
                filesExported.add(currentFileName);
                setProgressValue(++progress);
            }
        } finally {
            setProgressValue(0);
            setMessage(" ");
        }
        return (String[]) filesExported.toArray(new String[filesExported.size()]);
    }
