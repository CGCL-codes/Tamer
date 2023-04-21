    public void refresh(TableCell cell) {
        DiskManagerFileInfo fileInfo = (DiskManagerFileInfo) cell.getDataSource();
        long value = (fileInfo == null) ? 0 : fileInfo.getAccessMode();
        if (!cell.setSortValue(value) && cell.isValid()) {
            return;
        }
        String sText = MessageText.getString("FileItem." + ((value == DiskManagerFileInfo.WRITE) ? "write" : "read"));
        cell.setText(sText);
    }
