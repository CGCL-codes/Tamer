    public void rotate(ServerFigure figure) {
        if (!gameOver && figure.canRotate()) {
            getChannel().send(null, Protocol.rotate(getFigureInfo(figure)));
            figure.rotate();
        }
    }
