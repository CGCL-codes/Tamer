    public int getNeighbourCount(int x, int y) {
        if (this.boundaryCondition == BOUNDARY_CONDITION_WRAPAROUND) {
            int neighbourx[] = new int[8];
            int neighboury[] = new int[8];
            int xprev = x - 1;
            int xnext = x + 1;
            int yprev = y - 1;
            int ynext = y + 1;
            if (x == 0) {
                xprev = gridMaxWidth - 1;
                xnext = x + 1;
            }
            if (x == gridMaxWidth - 1) {
                xprev = x - 1;
                xnext = 0;
            }
            if (y == 0) {
                yprev = gridMaxHeight - 1;
                ynext = y + 1;
            }
            if (y == gridMaxHeight - 1) {
                yprev = y - 1;
                ynext = 0;
            }
            neighbourx[0] = xprev;
            neighboury[0] = yprev;
            neighbourx[1] = x;
            neighboury[1] = yprev;
            neighbourx[2] = xnext;
            neighboury[2] = yprev;
            neighbourx[3] = xprev;
            neighboury[3] = y;
            neighbourx[4] = xnext;
            neighboury[4] = y;
            neighbourx[5] = xprev;
            neighboury[5] = ynext;
            neighbourx[6] = x;
            neighboury[6] = ynext;
            neighbourx[7] = xnext;
            neighboury[7] = ynext;
            int count = 0;
            for (int i = 0; i < 8; i++) {
                if (grid[neighbourx[i]][neighboury[i]] != -1) {
                    count++;
                }
            }
            return count;
        }
        if (this.boundaryCondition == BOUNDARY_CONDITION_ZEROES) {
            int neighbour[] = new int[8];
            if (x == 0) {
                neighbour[0] = -1;
                neighbour[3] = -1;
                neighbour[5] = -1;
            }
            if (y == 0) {
                neighbour[0] = -1;
                neighbour[1] = -1;
                neighbour[2] = -1;
            }
            if (x == gridMaxWidth - 1) {
                neighbour[2] = -1;
                neighbour[4] = -1;
                neighbour[7] = -1;
            }
            if (y == gridMaxHeight - 1) {
                neighbour[5] = -1;
                neighbour[6] = -1;
                neighbour[7] = -1;
            }
            if (neighbour[0] != -1) {
                neighbour[0] = grid[x - 1][y - 1];
            }
            if (neighbour[1] != -1) {
                neighbour[1] = grid[x][y - 1];
            }
            if (neighbour[2] != -1) {
                neighbour[2] = grid[x + 1][y - 1];
            }
            if (neighbour[3] != -1) {
                neighbour[3] = grid[x - 1][y];
            }
            if (neighbour[4] != -1) {
                neighbour[4] = grid[x + 1][y];
            }
            if (neighbour[5] != -1) {
                neighbour[5] = grid[x - 1][y + 1];
            }
            if (neighbour[6] != -1) {
                neighbour[6] = grid[x][y + 1];
            }
            if (neighbour[7] != -1) {
                neighbour[7] = grid[x + 1][y + 1];
            }
            int count = 0;
            for (int i = 0; i < neighbour.length; i++) {
                if (neighbour[i] != -1) {
                    count++;
                }
            }
            return count;
        }
        return 0;
    }
