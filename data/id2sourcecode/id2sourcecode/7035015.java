    public Map<Integer, Cell> createChildrenCells() {
        Map<Integer, Cell> newCells = new HashMap<Integer, Cell>();
        if (nodes.size() > maxNodesPerZone) {
            this.hasChildren = true;
            childrenCells = new Cell[4];
            childrenCells[0] = new Cell();
            childrenCells[1] = new Cell();
            childrenCells[2] = new Cell();
            childrenCells[3] = new Cell();
            childrenCells[0].xMin = xMin;
            childrenCells[0].xMax = xMin + (xMax - xMin) / 2;
            childrenCells[0].yMin = yMin;
            childrenCells[0].yMax = yMin + (yMax - yMin) / 2;
            childrenCells[1].xMin = xMin + (xMax - xMin) / 2;
            childrenCells[1].xMax = xMax;
            childrenCells[1].yMin = yMin;
            childrenCells[1].yMax = yMin + (yMax - yMin) / 2;
            childrenCells[2].xMin = xMin;
            childrenCells[2].xMax = xMin + (xMax - xMin) / 2;
            childrenCells[2].yMin = yMin + (yMax - yMin) / 2;
            childrenCells[2].yMax = yMax;
            childrenCells[3].xMin = xMin + (xMax - xMin) / 2;
            childrenCells[3].xMax = xMax;
            childrenCells[3].yMin = yMin + (yMax - yMin) / 2;
            childrenCells[3].yMax = yMax;
            for (Cell cell : childrenCells) {
                cell.maxNodesPerZone = maxNodesPerZone;
                cell.setParentCell(this);
            }
            for (Node node : nodes) {
                Coord coord = node.getCoord();
                double xCoord = coord.getX();
                double yCoord = coord.getY();
                if (xCoord < xMin + (xMax - xMin) / 2) {
                    if (yCoord < yMin + (yMax - yMin) / 2) {
                        childrenCells[0].addNode(node);
                    } else {
                        childrenCells[2].addNode(node);
                    }
                } else {
                    if (yCoord < yMin + (yMax - yMin) / 2) {
                        childrenCells[1].addNode(node);
                    } else {
                        childrenCells[3].addNode(node);
                    }
                }
            }
            newCells.put(childrenCells[0].getCellId(), childrenCells[0]);
            newCells.put(childrenCells[1].getCellId(), childrenCells[1]);
            newCells.put(childrenCells[2].getCellId(), childrenCells[2]);
            newCells.put(childrenCells[3].getCellId(), childrenCells[3]);
            newCells.putAll(childrenCells[0].createChildrenCells());
            newCells.putAll(childrenCells[1].createChildrenCells());
            newCells.putAll(childrenCells[2].createChildrenCells());
            newCells.putAll(childrenCells[3].createChildrenCells());
            nodes = null;
        }
        return newCells;
    }
