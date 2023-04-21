    void rebuildMatrix() {
        long beginTime = System.currentTimeMillis();
        debugPrintln("Begin contruct mine matrix for minesweeper window... [0]milliSeconds");
        if (matrix != null) {
            debugPrintln("  Trying quick rebuild matrix on old data..." + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds");
            if (quickRebuidMatrix()) {
                debugPrintln("  Trying quick rebuild matrix on old data...OK!" + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds");
                return;
            }
            debugPrintln("  Trying quick rebuild matrix on old data...Failed! Retrying normal rebuild method..." + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds");
        }
        initDataBuffers();
        int currentY = 0;
        int currentX = 0;
        List<Block> lineBlocks = null;
        int width = cover.getWidth();
        int height = cover.getHeight();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage screen = r.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        int maxX = screen.getWidth() - width;
        int maxY = screen.getHeight() - height;
        Block firstBlock = null;
        debugPrintln("  Trying to find first block..." + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds");
        findFirst: for (currentY = 0; currentY <= maxY; currentY++) {
            for (currentX = 0; currentX <= maxX; currentX++) {
                Icon icon = whichIcon(screen, currentX, currentY);
                if (icon != null) {
                    firstBlock = new Block(icon, getInitState(icon), currentX, currentY, 0, 0);
                    matrix = new ArrayList<List<Block>>();
                    lineBlocks = new ArrayList<Block>();
                    lineBlocks.add(firstBlock);
                    if (firstBlock.getState() == State.S_UNSOLVED) {
                        addToBeCheckedBlocks(firstBlock);
                    }
                    break findFirst;
                }
            }
        }
        if (firstBlock == null) {
            throw new IllegalStateException(Messages.getString("MineSweepRobot2.not.found.minesweeper.window"));
        }
        debugPrintln("  Found first block: x=" + currentX + " y=" + currentY + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds (continue to find other blocks)...");
        int firstX = currentX;
        currentX += width;
        endParsing: for (; currentY <= maxY; currentY += height) {
            parsingNextLine: for (; currentX <= maxX; currentX += width) {
                Icon icon = whichIcon(screen, currentX, currentY);
                if (icon != null) {
                    Block b = new Block(icon, getInitState(icon), currentX, currentY, matrix.size(), lineBlocks.size());
                    if (lineBlocks == null) {
                        lineBlocks = new ArrayList<Block>();
                        matrix.add(lineBlocks);
                    }
                    lineBlocks.add(b);
                    if (b.getState() == State.S_UNSOLVED) {
                        addToBeCheckedBlocks(b);
                    }
                } else {
                    if (matrix.size() > 0 && lineBlocks.size() == 0) {
                        break endParsing;
                    } else if (matrix.size() > 0 && lineBlocks.size() != matrix.get(matrix.size() - 1).size()) {
                        throw new IllegalStateException("Some line size[" + lineBlocks.size() + "] greater than last line size[" + matrix.get(matrix.size() - 1).size() + "]");
                    } else {
                        break parsingNextLine;
                    }
                }
            }
            if (lineBlocks.size() > 0) {
                matrix.add(lineBlocks);
            }
            lineBlocks = new ArrayList<Block>();
            currentX = firstX;
        }
        if (getTotalRows() < 2 || getTotalColumns() < 2) {
            throw new IllegalStateException("Too small minesweeper rows=[" + getTotalRows() + "] columns=[" + getTotalColumns() + "]");
        }
        debugPrintln("  Found all blocks: totalRows=" + getTotalRows() + " totalCols=" + getTotalColumns() + " [" + (System.currentTimeMillis() - beginTime) + "]milliSeconds");
    }
