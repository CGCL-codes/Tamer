    private static ICell[][] interpolateSpace(CellBean[][] bean, int newGridSpacing, int oldGridSpacing, int radius) {
        int multiple = (int) Math.round(newGridSpacing / oldGridSpacing);
        int newLatGridSpacing = (newGridSpacing > 90) ? 90 : newGridSpacing;
        newGridWidth = (((180.0 / newGridSpacing) % 1) == 0) ? (180 / newGridSpacing) * 2 : (int) (Math.ceil((180.0 / newGridSpacing)) * 2);
        newGridHeight = (((90.0 / newLatGridSpacing) % 1) == 0) ? (90 / newLatGridSpacing) * 2 : (int) (Math.ceil((90.0 / newLatGridSpacing)) * 2);
        int oldLatGridSpacing = (oldGridSpacing > 90) ? 90 : oldGridSpacing;
        int oldGridWidth = (((180.0 / oldGridSpacing) % 1) == 0) ? (180 / oldGridSpacing) * 2 : (int) (Math.ceil((180.0 / oldGridSpacing)) * 2);
        int oldGridHeight = (((90.0 / oldLatGridSpacing) % 1) == 0) ? (90 / oldLatGridSpacing) * 2 : (int) (Math.ceil((90.0 / oldLatGridSpacing)) * 2);
        Node[][] grid = new Node[newGridHeight][newGridWidth];
        for (int y = 0; y < newGridHeight; y++) {
            for (int x = 0; x < newGridWidth; x++) {
                grid[y][x] = new Node(newGridSpacing);
                grid[y][x].init();
                if (y == 0) {
                    grid[y][x].degLatTop = 90;
                    grid[y][x].degLatBottom = (((90.0 / newLatGridSpacing) % 1) == 0) ? (grid[y][x].degLatTop - newLatGridSpacing) : (grid[y][x].degLatTop - (float) (newLatGridSpacing * ((90.0 / newLatGridSpacing) % 1)));
                    grid[y][x].top = null;
                } else if (y > 0 && y < newGridHeight - 1) {
                    grid[y][x].degLatTop = grid[y - 1][x].degLatBottom;
                    grid[y][x].degLatBottom = grid[y][x].degLatTop - newLatGridSpacing;
                    grid[y][x].top = grid[y - 1][x];
                    grid[y - 1][x].bottom = grid[y][x];
                } else if (y == (newGridHeight - 1)) {
                    grid[y][x].degLatTop = grid[y - 1][x].degLatBottom;
                    grid[y][x].degLatBottom = -90;
                    grid[y][x].top = grid[y - 1][x];
                    grid[y - 1][x].bottom = grid[y][x];
                    grid[y][x].bottom = null;
                }
                if (x == 0) {
                    grid[y][x].degLonLeft = 180;
                    grid[y][x].degLonRight = (((180.0 / newGridSpacing) % 1) == 0) ? (grid[y][x].degLonLeft - newGridSpacing) : (grid[y][x].degLonLeft - (float) (newGridSpacing * ((180.0 / newGridSpacing) % 1)));
                } else if (x > 0 && x < newGridWidth - 1) {
                    grid[y][x].degLonLeft = grid[y][x - 1].degLonRight;
                    grid[y][x].degLonRight = grid[y][x].degLonLeft - newGridSpacing;
                    grid[y][x].left = grid[y][x - 1];
                    grid[y][x - 1].right = grid[y][x];
                } else if (x == (newGridWidth - 1)) {
                    grid[y][x].degLonLeft = grid[y][x - 1].degLonRight;
                    grid[y][x].degLonRight = -180;
                    grid[y][x].left = grid[y][x - 1];
                    grid[y][x - 1].right = grid[y][x];
                    grid[y][x].right = grid[y][0];
                    grid[y][0].left = grid[y][x];
                }
                grid[y][x].cGeometry(radius);
            }
        }
        if (multiple > 1) {
            for (int y = 0; y < grid.length; y++) {
                for (int multipleCountY = 0; multipleCountY < multiple; multipleCountY++) {
                    for (int x = 0; x < grid.length; x++) {
                        for (int multipleCountX = 0; multipleCountX < multiple; multipleCountX++) {
                            try {
                                grid[y * multiple + multipleCountY][x * multiple + multipleCountX].newTemp = (float) bean[y][x].getTemperature();
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                }
            }
        } else if (multiple < 1.0) {
            multiple = 1 / multiple;
            for (int y = 0; y < grid.length; y++) {
                for (int multipleCountY = 0; multipleCountY < multiple; multipleCountY++) {
                    for (int x = 0; x < grid.length; x++) {
                        for (int multipleCountX = 0; multipleCountX < multiple; multipleCountX++) {
                            try {
                                grid[y][x].newTemp = (float) bean[y * multiple + multipleCountY][y * multiple + multipleCountY].getTemperature();
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                }
            }
        } else {
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid.length; x++) {
                    try {
                        grid[y][x].newTemp = (float) bean[y][x].getTemperature();
                    } catch (NullPointerException e) {
                    }
                }
            }
        }
        ArrayList workListX = new ArrayList();
        ArrayList workListY = new ArrayList();
        double prevTemp = 0.0f;
        double nextTemp = 0.0f;
        boolean lookingForNotNull = false;
        boolean interpolate = false;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (!interpolate) {
                    if (grid[y][x] != null) {
                        if (lookingForNotNull) {
                            lookingForNotNull = false;
                            nextTemp = grid[y][x].getTemperature();
                            interpolate = true;
                        } else {
                            prevTemp = grid[y][x].getTemperature();
                        }
                    } else {
                        lookingForNotNull = true;
                        workListX.add(new Integer(y));
                        workListY.add(new Integer(x));
                    }
                } else {
                    double avgTemp = (prevTemp + nextTemp) / 2;
                    for (int i = 0; i < workListX.size(); i++) {
                        grid[((Integer) workListY.get(i)).intValue()][((Integer) workListX.get(i)).intValue()].newTemp = (float) avgTemp;
                    }
                    interpolate = false;
                }
            }
        }
        return grid;
    }
