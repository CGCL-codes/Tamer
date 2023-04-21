    public void moveLeft(ServerFigure figure) {
        if (!gameOver && figure.canMoveLeft()) {
            getChannel().send(null, Protocol.moveLeft(getFigureInfo(figure)));
            figure.moveLeft();
        }
    }
