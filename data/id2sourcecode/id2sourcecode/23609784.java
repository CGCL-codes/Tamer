    public static boolean checkAndAlterModel() {
        String projectDbVersion = InternalPeer.getDbModelVersion();
        String currentDbVersion = Constants.Application.DB_MODEL_VERSION.toString();
        if (projectDbVersion.equals(currentDbVersion)) {
            return true;
        }
        try {
            stmt = PersistenceManager.getInstance().getConnection().createStatement();
        } catch (Exception e) {
            log.error(e);
            SwingTools.showException(e);
            return false;
        }
        if (projectDbVersion.equals("0") || projectDbVersion.equals("0.1") || projectDbVersion.equals("0.1") || projectDbVersion.equals("0.2") || projectDbVersion.equals("0.3") || projectDbVersion.equals("0.4") || projectDbVersion.equals("0.5") || projectDbVersion.equals("0.6") || projectDbVersion.equals("0.7") || projectDbVersion.equals("0.8") || projectDbVersion.equals("0.9") || projectDbVersion.equals("1.0")) {
            boolean ret = false;
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "This file version is too old. Update is not supported anymore.", I18N.getMsg("msg.common.error"), JOptionPane.ERROR_MESSAGE);
            return ret;
        }
        UpdateDialog dlg = new UpdateDialog();
        SwingTools.showDialog(dlg, MainFrame.getInstance());
        File file = ProjectTools.getCurrentFile();
        File backupFile = new File(file.getAbsolutePath() + ".bak");
        try {
            FileUtils.copyFile(file, backupFile);
        } catch (IOException e1) {
            dlg.append();
            dlg.append("Cannot create backup file:");
            dlg.append(backupFile.getAbsolutePath());
            dlg.append();
        }
        boolean ret = false;
        if (projectDbVersion.equals("1.1")) {
            ret = alterFrom1_1to1_2(dlg);
            if (ret) {
                ret = alterFrom1_2to1_3(dlg);
            }
            if (ret) {
                ret = alterFrom1_3to1_4(dlg);
            }
            if (ret) {
                ret = alterFrom1_4to1_5(dlg);
            }
        } else if (projectDbVersion.equals("1.2")) {
            ret = alterFrom1_2to1_3(dlg);
            if (ret) {
                ret = alterFrom1_3to1_4(dlg);
            }
            if (ret) {
                ret = alterFrom1_4to1_5(dlg);
            }
        } else if (projectDbVersion.equals("1.3")) {
            ret = alterFrom1_3to1_4(dlg);
            if (ret) {
                ret = alterFrom1_4to1_5(dlg);
            }
        } else if (projectDbVersion.equals("1.4")) {
            ret = alterFrom1_4to1_5(dlg);
        }
        if (ret) {
            dlg.append();
            dlg.append("File");
            dlg.append(PersistenceManager.getInstance().getFile().toString());
            dlg.append("was updated successfully.");
            dlg.append();
            dlg.append("Press 'Close' to continue.");
            return ret;
        }
        JOptionPane.showMessageDialog(MainFrame.getInstance(), I18N.getMsg("msg.error.wrong.version", projectDbVersion), I18N.getMsg("msg.common.error"), JOptionPane.ERROR_MESSAGE);
        return false;
    }
