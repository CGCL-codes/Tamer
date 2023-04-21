    public Object getValueAt(int row, int col) {
        if (arProgrammes[row] == null) {
            return "-";
        }
        switch(col) {
            case COL_TITLE:
                return arProgrammes[row].getTitle();
            case COL_TIME:
                return VerticalViewerConfig.listTimeFormat24Hour.format(new Date(arProgrammes[row].getStart())) + " - " + VerticalViewerConfig.listTimeFormat24Hour.format(new Date(arProgrammes[row].getEnd()));
            case COL_DATE:
                return VerticalViewerConfig.listDateFormat.format(new Date(arProgrammes[row].getStart()));
            case COL_CHANNEL:
                return arProgrammes[row].getChannel().getDisplayName();
            case COL_PROGRAMME:
                return arProgrammes[row];
            default:
                System.err.println("Unknown column #" + col);
                return "?";
        }
    }
