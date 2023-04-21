    public void execute(IWorkerStatusController worker) throws Exception {
        IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        Object[] uids = r.getUids();
        IMailbox srcFolder = (IMailbox) r.getSourceFolder();
        ((StatusObservableImpl) srcFolder.getObservable()).setWorker(worker);
        JFileChooser fileChooser = new JFileChooser();
        for (int j = 0; j < uids.length; j++) {
            Object uid = uids[j];
            LOG.info("Saving UID=" + uid);
            String subject = (String) srcFolder.getHeaderFields(uid, new String[] { "Subject" }).get("Subject");
            String defaultName = getValidFilename(subject, false);
            if (defaultName.length() == 0) {
                defaultName = srcFolder.getHeaderList().get(uid).get("columba.from").toString();
            }
            fileChooser.setSelectedFile(new File(defaultName));
            fileChooser.setDialogTitle(MailResourceLoader.getString("dialog", "saveas", "save_msg_source"));
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File f = fileChooser.getSelectedFile();
                if (f.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null, MailResourceLoader.getString("dialog", "saveas", "overwrite_existing_file"), MailResourceLoader.getString("dialog", "saveas", "file_exists"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.NO_OPTION) {
                        j--;
                        continue;
                    }
                }
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = new BufferedInputStream(srcFolder.getMessageSourceStream(uid));
                    out = new BufferedOutputStream(new FileOutputStream(f));
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer, 0, buffer.length)) > 0) {
                        out.write(buffer, 0, read);
                    }
                } catch (IOException ioe) {
                    LOG.severe("Error saving msg source to file: " + ioe.getMessage());
                    JOptionPane.showMessageDialog(null, MailResourceLoader.getString("dialog", "saveas", "err_save_msg"), MailResourceLoader.getString("dialog", "saveas", "err_save_title"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                    }
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException ioe) {
                    }
                }
            }
        }
    }
