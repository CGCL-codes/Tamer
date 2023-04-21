    public void moveRight(ServerFigure figure) {
        if (!gameOver && figure.canMoveRight()) {
            getChannel().send(null, Protocol.moveRight(getFigureInfo(figure)));
            figure.moveRight();
        }
    }
