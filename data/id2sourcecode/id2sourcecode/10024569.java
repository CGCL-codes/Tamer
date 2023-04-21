    public static ImageTree waveletTransformWp(Image origImg, int currLevel, int level, FilterGH[] filterGHList, int method) {
        int width = 0;
        int height = 0;
        int min = 0;
        int maxLevel = 0;
        Image coarseImg = null;
        Image horizontalImg = null;
        Image verticalImg = null;
        Image diagonalImg = null;
        Image tempImg = null;
        ImageTree returnTree = null;
        ImageTree tempTree = null;
        width = origImg.getWidth();
        height = origImg.getHeight();
        tempImg = new Image(width, height);
        copyIntoImage(tempImg, origImg, 0, 0);
        returnTree = new ImageTree();
        tempTree = returnTree;
        tempTree.setLevel(currLevel);
        min = origImg.getWidth();
        if (origImg.getHeight() < min) {
            min = origImg.getHeight();
        }
        maxLevel = (int) (Math.log(min) / Math.log(2)) - 2;
        if (maxLevel < level) {
            level = maxLevel;
        }
        if (currLevel >= level) {
            returnTree.setImage(tempImg);
            return returnTree;
        }
        for (int i = currLevel; i < level; i++) {
            width = (width + 1) / 2;
            height = (height + 1) / 2;
            coarseImg = new Image(width, height);
            horizontalImg = new Image(width, height);
            verticalImg = new Image(width, height);
            diagonalImg = new Image(width, height);
            decomposition(tempImg, coarseImg, horizontalImg, verticalImg, diagonalImg, filterGHList[i].getG(), filterGHList[i].getH(), method);
            tempTree.setCoarse(new ImageTree());
            tempTree.getCoarse().setLevel(i + 1);
            tempTree.setHorizontal(waveletTransformWp(horizontalImg, i + 1, level, filterGHList, method));
            tempTree.setVertical(waveletTransformWp(verticalImg, i + 1, level, filterGHList, method));
            tempTree.setDiagonal(waveletTransformWp(diagonalImg, i + 1, level, filterGHList, method));
            horizontalImg = null;
            verticalImg = null;
            diagonalImg = null;
            tempImg = null;
            if (i != (level - 1)) {
                tempImg = new Image(width, height);
                copyIntoImage(tempImg, coarseImg, 0, 0);
                coarseImg = null;
            }
            tempTree = tempTree.getCoarse();
        }
        tempTree.setImage(coarseImg);
        return returnTree;
    }
