        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) config_frames.get(local_dest_name);
            HashMap ps = (HashMap) parameters.get(local_dest_name);
            JTextField local_store_name = (JTextField) ps.get("local_store_name");
            if (local_store_name.getText().length() == 0) {
                QBrowserUtil.popupErrorMessageDialog(new Exception(resources.getString("qkey.msg.msg295")), frame.getRootPane());
                return;
            }
            JTextField local_store_dir_path = (JTextField) ps.get("local_store_dir_path");
            File ifile = new File(local_store_dir_path.getText());
            if (ifile.exists() && ifile.isDirectory()) {
            } else {
                QBrowserUtil.popupErrorMessageDialog(new Exception(resources.getString("qkey.msg.msg294")), frame.getRootPane());
                return;
            }
            JCheckBox local_store_valid = (JCheckBox) ps.get("local_store_valid");
            JCheckBox auto_migration = (JCheckBox) ps.get("auto_migration");
            LocalStoreProperty lsp = lsm.getLocalStoreProperty(local_dest_name);
            File from = new File(lsp.getReal_file_directory());
            if (!ifile.getAbsolutePath().equals(from.getAbsolutePath()) && auto_migration.getSelectedObjects() != null) {
                File[] fromfiles = from.listFiles();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < fromfiles.length; i++) {
                    if (fromfiles[i].isFile() && fromfiles[i].getName().endsWith("Message.zip")) {
                        try {
                            QBrowserUtil.copy(fromfiles[i], new File(ifile.getAbsolutePath() + File.separator + fromfiles[i].getName()));
                        } catch (Throwable thex) {
                            sb.append(thex.getMessage()).append("\n");
                        }
                    }
                }
                if (sb.length() != 0) {
                    QBrowserUtil.popupErrorMessageDialog(new Exception(sb.toString()), frame.getRootPane());
                }
            }
            if (!lsp.getDestName().equals(local_store_name.getText())) {
                vqb2.removeNamedTabbedPane(lsp.getDestNameWithSuffix());
                lsm.removeLocalStoreProperty(lsp);
            }
            lsp.setDestName(local_store_name.getText());
            lsp.setReal_file_directory(ifile.getAbsolutePath());
            if (local_store_valid.getSelectedObjects() != null) {
                lsp.setValid(true);
            } else {
                lsp.setValid(false);
            }
            try {
                lsm.updateAndSaveLocalStoreProperty(lsp);
            } catch (Exception lspe) {
                QBrowserUtil.popupErrorMessageDialog(lspe, frame.getRootPane());
            }
            frame.setVisible(false);
            if (lsp.isValid()) {
                try {
                    vqb2.collectDestination();
                } catch (Exception iie) {
                    iie.printStackTrace();
                }
            }
            vqb2.setMainDestComboBox(lsp.getDestNameWithSuffix());
            vqb2.initLocalStoreManager();
            vqb2.refreshLocalStoreMsgTableWithFileReloading(lsp.getDestNameWithSuffix());
            vqb2.initTreePane();
        }
