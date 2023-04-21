    public int addPoint(int _idxAfter) {
        CtuluCommandComposite cmd = new CtuluCommandComposite(EbliLib.getS("Ajout de sommet(s)"));
        int idxBefore = _idxAfter - 1;
        if (_idxAfter == -1) idxBefore = getRowCount() - 1;
        double x;
        double y;
        if (idxBefore == -1 && !isClosed_) {
            double x1 = (Double) getValueAt(idxBefore + 1, 1);
            double y1 = (Double) getValueAt(idxBefore + 1, 2);
            double x2 = (Double) getValueAt((idxBefore + 2) % getRowCount(), 1);
            double y2 = (Double) getValueAt((idxBefore + 2) % getRowCount(), 2);
            x = (x1 - (x2 - x1) / 2);
            y = (y1 - (y2 - y1) / 2);
        } else if (idxBefore == getRowCount() - 1 && !isClosed_) {
            double x1 = (Double) getValueAt((idxBefore - 1 + getRowCount()) % getRowCount(), 1);
            double y1 = (Double) getValueAt((idxBefore - 1 + getRowCount()) % getRowCount(), 2);
            double x2 = (Double) getValueAt(idxBefore, 1);
            double y2 = (Double) getValueAt(idxBefore, 2);
            x = (x2 + (x2 - x1) / 2);
            y = (y2 + (y2 - y1) / 2);
        } else {
            double x1 = (Double) getValueAt((idxBefore + getRowCount()) % getRowCount(), 1);
            double y1 = (Double) getValueAt((idxBefore + getRowCount()) % getRowCount(), 2);
            double x2 = (Double) getValueAt((idxBefore + 1) % getRowCount(), 1);
            double y2 = (Double) getValueAt((idxBefore + 1) % getRowCount(), 2);
            x = (x1 + x2) / 2;
            y = (y1 + y2) / 2;
        }
        int inew = stat_.addPoint(idxBefore, x, y, cmd);
        if (cmd_ != null) {
            cmd_.addCmd(cmd.getSimplify());
        }
        return inew;
    }
