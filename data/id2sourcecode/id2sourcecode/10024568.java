    public static ImageTree waveletTransform(Image origImg, int level, FilterGH[] filterGHList, int method) {
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
        returnTree.setLevel(0);
        min = origImg.getWidth();
        if (origImg.getHeight() < min) {
            min = origImg.getHeight();
        }
        maxLevel = ((int) (Math.log(min) / Math.log(2))) - 2;
        if (maxLevel < level) {
            level = maxLevel;
        }
        if (level < 1) {
            returnTree.setImage(tempImg);
            return returnTree;
        }
        for (int i = 0; i < level; i++) {
            width = (width + 1) / 2;
            height = (height + 1) / 2;
            coarseImg = new Image(width, height);
            horizontalImg = new Image(width, height);
            verticalImg = new Image(width, height);
            diagonalImg = new Image(width, height);
            decomposition(tempImg, coarseImg, horizontalImg, verticalImg, diagonalImg, filterGHList[i].getG(), filterGHList[i].getH(), method);
            tempTree.setCoarse(new ImageTree());
            tempTree.setHorizontal(new ImageTree());
            tempTree.setVertical(new ImageTree());
            tempTree.setDiagonal(new ImageTree());
            tempTree.getCoarse().setLevel(i + 1);
            tempTree.getHorizontal().setLevel(i + 1);
            tempTree.getVertical().setLevel(i + 1);
            tempTree.getDiagonal().setLevel(i + 1);
            tempTree.getHorizontal().setImage(horizontalImg);
            tempTree.getVertical().setImage(verticalImg);
            tempTree.getDiagonal().setImage(diagonalImg);
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
