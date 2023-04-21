    public Object getValueAt(int row, int col) {
        PVTableCell cell = pvPanel.getCell(row);
        if (cell.isDummy()) return null;
        if (col == 0) {
            return cell.getChannelWrapper().getId();
        }
        if (col == 1) {
            return cell;
        }
        return null;
    }
