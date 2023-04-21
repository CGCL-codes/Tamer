    private void handleFigureBottom(ServerFigure figure, Player ply) {
        figure.fix();
        boardRef.getForUpdate().removeFullLines();
        if (!figure.isAllVisible()) {
            gameOver = true;
            getChannel().send(null, Protocol.gameOver());
        } else {
            ServerFigure newFigure = newFigure(ply);
            getChannel().send(null, Protocol.newFigure(getFigureInfo(figure), getFigureInfo(newFigure)));
        }
    }
