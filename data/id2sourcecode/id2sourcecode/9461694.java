    public void moveAllWayDown(ServerFigure figure, Player ply) {
        if (!gameOver) {
            if (figure.canMoveDown()) {
                FigureInfo old = getFigureInfo(figure);
                while (figure.canMoveDown()) {
                    figure.moveDown();
                }
                FigureInfo actual = getFigureInfo(figure);
                getChannel().send(null, Protocol.moveAllWayDown(old, actual));
            }
            if (figure.isAllWayDown()) {
                handleFigureBottom(figure, ply);
            } else {
            }
        }
    }
