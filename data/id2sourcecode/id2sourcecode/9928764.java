    public void valueChanged(GraphSelectionEvent e) {
        String s = "";
        Object[] cells;
        cells = (Object[]) e.getCells();
        for (int x = 0; x < cells.length; x++) if (x == 0) s = Integer.toString(((GraphCell) cells[x]).getChannelNumber() + 1); else s += "+" + (((GraphCell) cells[x]).getChannelNumber() + 1);
        _actInt.interprete(s);
    }
