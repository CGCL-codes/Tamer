    private static Tile angledTileHelp(Tile currentTile, int anchX, int anchY, SideSelector selector, Position pivot, int sizeX, int sizeY, ITileContent box, Tile cornerTile) {
        int epsilon, b, currentX, maxX, minX;
        int orgX = 0, orgY = 0, tempOrgX, tempOrgY;
        int x, y;
        boolean foundOne = false;
        if (selector.xSideCount != 0 && Math.abs(selector.xSideCount) > Math.abs(selector.ySideCount)) {
            if (cornerTile != null) {
                if (selector.xSideCount > 0) {
                    minX = anchX;
                    y = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                    if (selector.ySideCount == 0) maxX = cornerTile.x(); else if (selector.ySideCount > 0) {
                        if (y < cornerTile.y()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x();
                        }
                    } else {
                        if (y > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x();
                        }
                    }
                } else {
                    minX = anchX;
                    y = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                    if (selector.ySideCount == 0) maxX = cornerTile.x() + cornerTile.width(); else if (selector.ySideCount > 0) {
                        if (y < cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x() + cornerTile.width();
                        }
                    } else {
                        if (y > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                        } else {
                            maxX = cornerTile.x() + cornerTile.width();
                        }
                    }
                }
            } else {
                if (selector.xSideCount > 0) {
                    cornerTile = currentTile.find_LRHC();
                    minX = anchX;
                    maxX = cornerTile.right();
                } else {
                    cornerTile = currentTile.find_ULHC();
                    minX = anchX;
                    maxX = cornerTile.x();
                }
            }
            epsilon = currentX = (minX + maxX) / 2;
            b = (anchY - (int) ((float) selector.ySideCount / (float) selector.xSideCount * (float) anchX));
            tempOrgX = currentX - pivot.x();
            tempOrgY = (int) (((float) selector.ySideCount / (float) selector.xSideCount) * (float) currentX) + b - pivot.y();
            if (currentTile.locate(tempOrgX, tempOrgY) == null) {
                epsilon = currentX = currentX / 2;
                System.err.println("Bad origin point selected; trying again...");
            }
            while (Math.abs(epsilon) > 1) {
                tempOrgX = currentX - pivot.x();
                tempOrgY = (int) (((float) selector.ySideCount / (float) selector.xSideCount) * (float) currentX) + b - pivot.y();
                Tile flag = currentTile.areaSearch(tempOrgX, tempOrgY, sizeX, sizeY);
                if (flag != null) {
                    NextMoveResult res = nextMove(minX, maxX, currentX, FURTHER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                } else {
                    NextMoveResult res = nextMove(minX, maxX, currentX, CLOSER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                    orgX = tempOrgX;
                    orgY = tempOrgY;
                    foundOne = true;
                }
            }
        } else if (selector.ySideCount != 0) {
            if (cornerTile != null) {
                if (selector.ySideCount > 0) {
                    minX = anchY;
                    x = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.y() - anchY)) + anchX;
                    if (selector.xSideCount == 0) maxX = cornerTile.y(); else if (selector.xSideCount > 0) {
                        if (x > cornerTile.x()) {
                            maxX = cornerTile.y();
                        } else {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                        }
                    } else {
                        if (x > cornerTile.right()) {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                        } else {
                            maxX = cornerTile.y();
                        }
                    }
                } else {
                    minX = anchY;
                    x = (int) ((float) selector.xSideCount / (float) selector.ySideCount * (cornerTile.bottom() - anchY)) + anchX;
                    if (selector.xSideCount == 0) maxX = cornerTile.bottom(); else if (selector.xSideCount > 0) {
                        if (x > cornerTile.x()) {
                            maxX = cornerTile.bottom();
                        } else {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.x() - anchX)) + anchY;
                        }
                    } else {
                        if (x > cornerTile.bottom()) {
                            maxX = (int) ((float) selector.ySideCount / (float) selector.xSideCount * (cornerTile.right() - anchX)) + anchY;
                        } else {
                            maxX = cornerTile.bottom();
                        }
                    }
                }
            } else {
                if (selector.ySideCount > 0) {
                    cornerTile = currentTile.find_ULHC();
                    minX = anchY;
                    maxX = cornerTile.bottom();
                } else {
                    cornerTile = currentTile.find_LRHC();
                    maxX = cornerTile.y();
                    minX = anchY;
                }
            }
            epsilon = currentX = (minX + maxX) / 2;
            b = (anchX - (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) anchY));
            tempOrgY = currentX - pivot.y();
            tempOrgX = (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) currentX) + b - pivot.x();
            if (currentTile.locate(tempOrgX, tempOrgY) == null) {
                currentTile.locate(tempOrgX, tempOrgY);
                epsilon = currentX = currentX / 2;
                System.err.println("Bad origin point selected; trying again...\n");
            }
            while (Math.abs(epsilon) > 1) {
                tempOrgY = currentX - pivot.y();
                tempOrgX = (int) (((float) selector.xSideCount / (float) selector.ySideCount) * (float) currentX) + b - pivot.x();
                Tile flag = currentTile.areaSearch(tempOrgX, tempOrgY, sizeX, sizeY);
                if (flag != null) {
                    NextMoveResult res = nextMove(minX, maxX, currentX, FURTHER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                } else {
                    NextMoveResult res = nextMove(minX, maxX, currentX, CLOSER, epsilon);
                    currentX = res.result;
                    minX = res.min;
                    maxX = res.max;
                    epsilon = res.epsilon;
                    orgX = tempOrgX;
                    orgY = tempOrgY;
                    foundOne = true;
                }
            }
        } else {
            System.err.println("angled_tile_help: null <selector.ySideCount>, <selector.xSideCount> values given.\n");
        }
        if (!foundOne) return null;
        return currentTile.insertTile(orgX, orgY, sizeX, sizeY, box);
    }
