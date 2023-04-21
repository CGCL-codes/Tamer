    public Dicer() {
        for (int level = 0; level < MAX_SUBDIV; level++) {
            final int dim = ((1 << level)) + 3;
            if (level == 0) {
                rim0[0] = new int[4][2];
                rim0[0][0] = new int[] { 5, 6 };
                rim0[0][1] = new int[] { 6, 10 };
                rim0[0][2] = new int[] { 10, 9 };
                rim0[0][3] = new int[] { 9, 5 };
                rim1[0] = new int[0][];
                rimTriangles[0][0] = new int[4][0];
                rimTriangleNormals[0][0] = new int[4][0];
            } else {
                rim0[level] = new int[4][dim - 2];
                for (int i = 0; i < (dim - 2); i++) {
                    rim0[level][0][i] = dim + i + 1;
                    rim0[level][1][i] = dim + dim - 2 + (dim * i);
                    rim0[level][2][i] = dim * dim - dim - 2 - i;
                    rim0[level][3][i] = dim * dim - 2 * dim - (dim * i) + 1;
                }
                rim1[level] = new int[4][dim - 4];
                for (int i = 0; i < (dim - 4); i++) {
                    rim1[level][0][i] = 2 * dim + i + 2;
                    rim1[level][1][i] = 2 * dim + dim - 3 + (dim * i);
                    rim1[level][2][i] = dim * dim - 2 * dim - 3 - i;
                    rim1[level][3][i] = dim * dim - 3 * dim - (dim * i) + 2;
                }
                for (int pairLevel = level; pairLevel >= 0; pairLevel--) {
                    rimTriangles[level][pairLevel] = new int[4][];
                    rimTriangleNormals[level][pairLevel] = new int[4][];
                    int[] tmpTriangles = new int[dim * 6];
                    int[] tmpNormals = new int[dim * 6];
                    for (int side = 0; side < 4; side++) {
                        int j = 0;
                        int levelDelta = level - pairLevel;
                        if (levelDelta < 0) {
                            continue;
                        }
                        int step = 1 << levelDelta;
                        int correction = step == 1 ? 0 : -1;
                        for (int i = 0; i < (dim - 2 - 1 * step); i += step) {
                            int ii = i + step / 2 + correction;
                            int innerNormal = ((side + 2) % 4) * 3;
                            if (ii >= rim1[level][side].length) {
                                ii = rim1[level][side].length - 1;
                                innerNormal = ((side + 3) % 4) * 3;
                            }
                            tmpNormals[j] = side * 3;
                            tmpTriangles[j++] = rim0[level][side][i] + GRID_START;
                            tmpNormals[j] = ((side + 1) % 4) * 3;
                            tmpTriangles[j++] = rim0[level][side][i + step] + GRID_START;
                            tmpNormals[j] = innerNormal;
                            tmpTriangles[j++] = rim1[level][side][ii] + GRID_START;
                        }
                        for (int i = 0; i < (dim - 5); i++) {
                            int ii = ((i + (step / 2) + 1) / step) * step;
                            int outerNormal = ((i + 1) % step < step / 2) ? side * 3 : ((side + 1) % 4) * 3;
                            tmpNormals[j] = outerNormal;
                            tmpTriangles[j++] = rim0[level][side][ii] + GRID_START;
                            tmpNormals[j] = ((side + 2) % 4) * 3;
                            tmpTriangles[j++] = rim1[level][side][i + 1] + GRID_START;
                            tmpNormals[j] = ((side + 3) % 4) * 3;
                            tmpTriangles[j++] = rim1[level][side][i] + GRID_START;
                        }
                        rimTriangles[level][pairLevel][side] = new int[j];
                        rimTriangleNormals[level][pairLevel][side] = new int[j];
                        System.arraycopy(tmpTriangles, 0, rimTriangles[level][pairLevel][side], 0, j);
                        System.arraycopy(tmpNormals, 0, rimTriangleNormals[level][pairLevel][side], 0, j);
                    }
                }
            }
            subdivPoints[level] = new float[dim * dim + GRID_START][3];
            limitPoints[level] = new float[dim * dim + GRID_START][3];
            limitNormals[level] = new float[dim * dim + GRID_START][12];
            for (int valence = 3; valence <= MAX_VALENCE; valence++) {
                for (int corner = 0; corner < 2; corner++) {
                    cornerStencil[level][valence - 3][corner] = new int[valence * 2 + 6];
                    cornerStencil[level][valence - 3][corner][0] = 0;
                    cornerStencil[level][valence - 3][corner][1] = 0;
                    cornerStencil[level][valence - 3][corner][2] = 0;
                    cornerStencil[level][valence - 3][corner][3] = 0;
                    cornerStencil[level][valence - 3][corner][4] = patchCornerIndex(corner, level + 1, 1, 1);
                    cornerStencil[level][valence - 3][corner][5] = patchCornerIndex(corner, level, 1, 1);
                    cornerStencil[level][valence - 3][corner][6] = patchCornerIndex(corner, level, 0, 2);
                    cornerStencil[level][valence - 3][corner][7] = patchCornerIndex(corner, level, 1, 2);
                    cornerStencil[level][valence - 3][corner][8] = patchCornerIndex(corner, level, 2, 2);
                    cornerStencil[level][valence - 3][corner][9] = patchCornerIndex(corner, level, 2, 1);
                    cornerStencil[level][valence - 3][corner][10] = patchCornerIndex(corner, level, 2, 0);
                    for (int i = 0, n = valence * 2 - 5; i < n; i++) {
                        int index = i + 1;
                        if (index == n) {
                            index = 0;
                        }
                        cornerStencil[level][valence - 3][corner][i + 11] = cornerIndex(corner, valence, index);
                    }
                    cornerLimitStencil[level][valence - 3][corner] = new int[valence * 2 + 6];
                    cornerLimitStencil[level][valence - 3][corner][0] = 0;
                    cornerLimitStencil[level][valence - 3][corner][1] = 0;
                    cornerLimitStencil[level][valence - 3][corner][2] = 0;
                    cornerLimitStencil[level][valence - 3][corner][3] = 0;
                    cornerLimitStencil[level][valence - 3][corner][4] = patchCornerIndex(corner, level + 1, 1, 1);
                    cornerLimitStencil[level][valence - 3][corner][5] = patchCornerIndex(corner, level + 1, 1, 1);
                    cornerLimitStencil[level][valence - 3][corner][6] = patchCornerIndex(corner, level + 1, 0, 2);
                    cornerLimitStencil[level][valence - 3][corner][7] = patchCornerIndex(corner, level + 1, 1, 2);
                    cornerLimitStencil[level][valence - 3][corner][8] = patchCornerIndex(corner, level + 1, 2, 2);
                    cornerLimitStencil[level][valence - 3][corner][9] = patchCornerIndex(corner, level + 1, 2, 1);
                    cornerLimitStencil[level][valence - 3][corner][10] = patchCornerIndex(corner, level + 1, 2, 0);
                    for (int i = 0, n = valence * 2 - 5; i < n; i++) {
                        int index = i + 1;
                        if (index == n) {
                            index = 0;
                        }
                        cornerLimitStencil[level][valence - 3][corner][i + 11] = cornerIndex(corner, valence, index);
                    }
                    int[][] array = new int[cornerStencilLength(valence)][];
                    fanStencil[level][valence - 3][corner] = array;
                    if (valence == 3) {
                        array[0] = new int[] { EDGE, 0, 0, 0, cornerIndex2(corner, level, valence, 0), patchCornerIndex(corner, level, 1, 1), cornerIndex2(corner, level, valence, 2), cornerIndex2(corner, level, valence, 3), cornerIndex2(corner, level, valence, -1), cornerIndex2(corner, level, valence, -2) };
                        array[1] = array[0].clone();
                    } else {
                        for (int i = 0, n = valence * 2 - 5; i < n; i++) {
                            int index = i + 1;
                            if (index == n) {
                                index = 0;
                            }
                            if ((i & 1) == 0) {
                                array[index] = new int[] { EDGE, 0, 0, 0, cornerIndex2(corner, level, valence, i), patchCornerIndex(corner, level, 1, 1), cornerIndex2(corner, level, valence, i + 1), cornerIndex2(corner, level, valence, i + 2), cornerIndex2(corner, level, valence, i - 1), cornerIndex2(corner, level, valence, i - 2) };
                            } else {
                                array[index] = new int[] { FACE, cornerIndex2(corner, level, valence, i), cornerIndex2(corner, level, valence, i + 1), patchCornerIndex(corner, level, 1, 1), cornerIndex2(corner, level, valence, i - 1) };
                            }
                        }
                    }
                }
            }
            patchStencil[level] = new int[dim * dim][];
            for (int row = 0; row < dim; row++) {
                int rowStart = row * dim;
                for (int column = 0; column < dim; column++) {
                    int index = rowStart + column;
                    if ((row < 2 && column < 2) || (row > dim - 3 && column > dim - 3)) {
                        patchStencil[level][index] = new int[] { UNUSED, 0 };
                        continue;
                    }
                    int nextRow = row * 2 - 1;
                    int nextColumn = column * 2 - 1;
                    int nextLevelIndex_0 = getStencilIndex(level + 1, nextRow, nextColumn);
                    int nextLevelIndex_1 = getStencilIndex(level + 1, nextRow - 1, nextColumn);
                    int nextLevelIndex_2 = getStencilIndex(level + 1, nextRow, nextColumn + 1);
                    int nextLevelIndex_3 = getStencilIndex(level + 1, nextRow + 1, nextColumn);
                    int nextLevelIndex_4 = getStencilIndex(level + 1, nextRow, nextColumn - 1);
                    if ((column & 1) == 0) {
                        if ((row & 1) == 0) {
                            int c = column / 2;
                            int r = row / 2;
                            patchStencil[level][index] = new int[] { FACE, patchIndex(level, r, c), patchIndex(level, r, c + 1), patchIndex(level, r + 1, c + 1), patchIndex(level, r + 1, c) };
                        } else {
                            int c = column / 2;
                            int r = (row + 1) / 2;
                            patchStencil[level][index] = new int[] { EDGE_H, 0, 0, 0, nextLevelIndex_0, nextLevelIndex_1, nextLevelIndex_2, nextLevelIndex_3, nextLevelIndex_4, patchIndex(level, r, c), patchIndex(level, r, c + 1), patchIndex(level, r - 1, c), patchIndex(level, r - 1, c + 1), patchIndex(level, r + 1, c), patchIndex(level, r + 1, c + 1) };
                        }
                    } else {
                        if ((row & 1) == 0) {
                            int c = (column + 1) / 2;
                            int r = row / 2;
                            patchStencil[level][index] = new int[] { EDGE_V, 0, 0, 0, nextLevelIndex_0, nextLevelIndex_1, nextLevelIndex_2, nextLevelIndex_3, nextLevelIndex_4, patchIndex(level, r, c), patchIndex(level, r + 1, c), patchIndex(level, r, c + 1), patchIndex(level, r + 1, c + 1), patchIndex(level, r, c - 1), patchIndex(level, r + 1, c - 1) };
                        } else {
                            int c = (column + 1) / 2;
                            int r = (row + 1) / 2;
                            patchStencil[level][index] = new int[] { POINT, 0, nextLevelIndex_0, patchIndex(level, r, c), patchIndex(level, r - 1, c), patchIndex(level, r, c + 1), patchIndex(level, r + 1, c), patchIndex(level, r, c - 1), patchIndex(level, r - 1, c - 1), patchIndex(level, r - 1, c + 1), patchIndex(level, r + 1, c + 1), patchIndex(level, r + 1, c - 1) };
                        }
                    }
                }
            }
            patchLimitStencil[level] = new int[dim * dim][];
            for (int row = 1; row < dim - 1; row++) {
                int rowStart = row * dim;
                for (int column = 1; column < dim - 1; column++) {
                    if ((row == 1 && column == 1) || (row == dim - 2 && column == dim - 2)) {
                        continue;
                    }
                    int index = rowStart + column;
                    patchLimitStencil[level][index] = new int[] { patchIndex(level + 1, row, column), patchIndex(level + 1, row - 1, column), patchIndex(level + 1, row, column + 1), patchIndex(level + 1, row + 1, column), patchIndex(level + 1, row, column - 1), patchIndex(level + 1, row - 1, column - 1), patchIndex(level + 1, row - 1, column + 1), patchIndex(level + 1, row + 1, column + 1), patchIndex(level + 1, row + 1, column - 1) };
                }
            }
        }
    }
