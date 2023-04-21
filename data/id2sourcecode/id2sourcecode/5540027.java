    private static int buildDeadEnds(int x0, int maxLength) {
        int floor = height - 2 - globalRandom.nextInt(2);
        int length = 0;
        int preDeadEndLength = 7 + globalRandom.nextInt(10);
        int rHeight = floor - 1;
        int separatorY = 3 + globalRandom.nextInt(rHeight - 7);
        length += buildStraight(x0, preDeadEndLength, true, floor, INFINITE_FLOOR_HEIGHT);
        if (globalRandom.nextInt(3) == 0 && isLadder) {
            int ladderX = x0 + globalRandom.nextInt(length - 1) + 1;
            if (ladderX > x0 + length) ladderX = x0 + length;
            buildLadder(ladderX, floor, floor - separatorY);
        } else buildBlocks(x0, x0 + preDeadEndLength, floor, true, 0, 0, true, true);
        globalRandom.nextInt();
        int k = globalRandom.nextInt(5);
        boolean direction = globalRandom.nextInt(k + 1) != 1;
        int separatorHeight = 2 + globalRandom.nextInt(2);
        int nx = x0 + length;
        int depth = globalRandom.nextInt(levelDifficulty + 1) + 2 * (1 + levelDifficulty);
        if (depth + length > maxLength) {
            while (depth + length > maxLength - 1) {
                depth--;
            }
        }
        int tLength = 0;
        int bSpace = floor - (separatorY + separatorHeight);
        if (bSpace < 4) {
            while (bSpace < 4) {
                separatorY -= 1;
                bSpace = floor - (separatorY + separatorHeight);
            }
        }
        int wallWidth = 2 + globalRandom.nextInt(3);
        while (tLength < depth) {
            tLength += buildZone(nx + tLength, depth - tLength, separatorY - 1, separatorY, separatorHeight);
        }
        tLength = 0;
        while (tLength < depth) {
            tLength += buildZone(nx + tLength, depth - tLength, bSpace, floor, INFINITE_FLOOR_HEIGHT);
        }
        boolean wallFromBlocks = false;
        for (int x = nx; x < nx + depth; x++) {
            for (int y = 0; y < height; y++) {
                if (x - nx >= depth - wallWidth) {
                    if (direction) {
                        if (y <= separatorY) {
                            if (wallFromBlocks) level.setBlock(x, y, (byte) (0 + 1 * 16)); else level.setBlock(x, y, (byte) (1 + 9 * 16));
                        }
                    } else {
                        if (y >= separatorY) {
                            if (wallFromBlocks) level.setBlock(x, y, (byte) (0 + 1 * 16)); else level.setBlock(x, y, (byte) (1 + 9 * 16));
                        }
                    }
                }
            }
        }
        return length + tLength;
    }
