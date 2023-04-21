    private void doFinish(String connName, String[] protocols, String port, String userName, String pwd, String host, String defaultDir) throws CoreException {
        Connection connection = validateConnectionInfo(connName, protocols, port, userName, pwd, host, defaultDir);
        ConnectionStore.getConnectionStore().addConnection(connName, connection);
        SFtpFileHandler handle = new SFtpFileHandler();
        try {
            handle.getChannel(connection);
        } catch (JSchException e1) {
            if (!MessageDialog.openConfirm(getShell(), "Confirm !!!!!", "Unable to Connet to the host,\nDo you want to still Save?")) {
                throwCoreException("Did not Save the Connection");
            }
        }
        try {
            ConnectionStore.getConnectionStore().save();
        } catch (Exception e) {
            throwCoreException("Unable to save connection Data");
        }
    }
