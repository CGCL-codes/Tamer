    boolean quickRebuidMatrix() {
        if (matrix == null) {
            return false;
        }
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage screen = r.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        int totalRow = getTotalRows();
        int totalCol = getTotalColumns();
        for (int row = 0; row < totalRow; row++) {
            for (int col = 0; col < totalCol; col++) {
                Block b = getBlock(row, col);
                Icon icon = whichIcon(screen, b.x, b.y, b.getIcon());
                if (icon == null) {
                    return false;
                }
                if (icon != b.getIcon()) {
                    b.setIcon(icon);
                    b.setState(getInitState(icon));
                    if (b.getState() == State.S_UNSOLVED) {
                        addToBeCheckedBlocks(b);
                    }
                }
            }
        }
        return true;
    }
