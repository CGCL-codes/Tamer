    public Object getValueAt(int rowIndex, int columnIndex) {
        if (lines == null) {
            return null;
        }
        if (columnIndex == 1) {
            Line line = lines.getLine(rowIndex);
            Integer tempChannel = new Integer(line.getChannel());
            return tempChannel.toString();
        } else {
            return super.getValueAt(rowIndex, columnIndex);
        }
    }
