    private void pasteFile(final DFSFile from, final DFSFile to) throws DFSException {
        boolean skip = false;
        if (to == null || to.getName().equals("")) {
            JOptionPane.showMessageDialog(explorer, "Invalid File Name", "Error Saving File", JOptionPane.ERROR_MESSAGE);
        } else {
            if (to.exists()) {
                if (noToAll) {
                    skip = true;
                } else if (!yesToAll) {
                    String[] options = new String[] { "Yes", "No", "Yes to all", "No to all" };
                    Object selected = JOptionPane.showInputDialog(explorer, to.getPath() + " already exists. Overwrite?", "File exists", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
                progressMonitor.setNote(from.getName());
                boolean suceeded = false;
                int failed = 0;
                while (!suceeded && failed < 4 && !progressMonitor.isCanceled()) {
                    suceeded = copy(from, to);
                    if (!suceeded) {
                        failed++;
                    }
                }
                if (!suceeded) {
                    progressMonitor.setProgress(progressMonitor.getMaximum() + 1);
                    throw new DFSException();
                }
            } else if (skip) {
                progressMonitor.setNote("Skipping " + from.getName());
                progressCounter += from.length();
                progressMonitor.setProgress(progressCounter);
            }
        }
    }
