    protected void initializeGraphicalViewer() {
        EditPartViewer viewer = getGraphicalViewer();
        ProgressMonitorDialog progress = new ProgressMonitorDialog(null);
        progress.setCancelable(false);
        try {
            if (MaseConnection.useMaseServer() == false) {
                if (FileSystemUtility.getFileSystemUtility().getnewFile()) {
                    this.setPartName(CardConstants.APPLICATIONNAME);
                    tooltip = CardConstants.APPLICATIONNAME;
                } else {
                    tableModel = FileSystemUtility.getFileSystemUtility().getTableModel();
                    this.setPartName(FileSystemUtility.getFileSystemUtility().getFileName());
                    tooltip = FileSystemUtility.getFileSystemUtility().getAbsoluteFileName();
                }
            } else if (MaseConnection.useMaseServer()) {
                try {
                    MaseConnection connection;
                    connection = MaseConnection.getMaseConnection();
                    connection.setTableModel(tableModel);
                    tableModel = connection.getInitialState();
                    this.setPartName(MaseConnection.getMaseConnection().getProjectName());
                } catch (Exception e) {
                    System.err.println("There was an error connecting to mase!");
                    e.printStackTrace();
                }
            }
            tableModel.setEditor(editor);
        } catch (Exception e) {
        }
        viewer.setContents(tableModel);
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }
