    public void moveDown(ServerFigure figure, Player ply) {
        if (!gameOver) {
            if (figure.canMoveDown()) {
                getChannel().send(null, Protocol.moveDown(getFigureInfo(figure)));
                figure.moveDown();
            } else if (figure.isAllWayDown()) {
                handleFigureBottom(figure, ply);
            } else {
            }
        }
    }
