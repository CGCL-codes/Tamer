    private void uploadFile(final File localFile, final DFSFile upFile) throws DFSException {
        boolean skip = false;
        if (upFile.exists()) {
            if (noToAll) {
                skip = true;
            } else if (!yesToAll) {
                String[] options = new String[] { "Yes", "No", "Yes to all", "No to all" };
                Object selected = JOptionPane.showInputDialog(explorer, upFile.getPath() + " already exists. Overwrite?", "File exists", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (selected == null) {
                    skip = true;
                    cancelled = true;
                    progressCounter = progressMonitor.getMaximum() + 1;
                    progressMonitor.setProgress(progressCounter);
                } else if (selected == options[1]) {
                    skip = true;
                } else if (selected == options[2]) {
                    yesToAll = true;
                } else if (selected == options[3]) {
                    noToAll = true;
                    skip = true;
                }
            }
        }
        if (!skip && !cancelled) {
            progressMonitor.setNote(localFile.getName());
            boolean suceeded = false;
            int failed = 0;
            while (!suceeded && failed < 4 && !progressMonitor.isCanceled()) {
                suceeded = transferUp(localFile, upFile);
                if (!suceeded) {
                    failed++;
                }
            }
            if (!suceeded) {
                progressMonitor.setProgress(progressMonitor.getMaximum() + 1);
                throw new DFSException();
            }
        } else if (skip) {
            progressMonitor.setNote("Skipping " + localFile.getName());
            progressCounter += localFile.length();
            progressMonitor.setProgress(progressCounter);
        }
    }
