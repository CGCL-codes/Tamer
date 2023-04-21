    public void gameStart() {
        AppContext.getDataManager().markForUpdate(this);
        started = true;
        int numPlayers = players.size();
        SquareBoard board = new SquareBoard(8 + (numPlayers - 1) * 4, 20);
        boardRef = AppContext.getDataManager().createReference(board);
        List<FigureInfo> figures = new ArrayList<FigureInfo>(numPlayers);
        int x = 3;
        for (Player ply : getPlayers()) {
            ply.setX(x);
            x += 4;
            ServerFigure fig = newFigure(ply);
            figures.add(new FigureInfo(fig.getType(), fig.getX(), fig.getY(), fig.getRotation(), fig.getColor()));
        }
        getChannel().send(null, Protocol.gameStart(board.getBoardWidth(), figures));
        AppContext.getChannelManager().getChannel(DarkstrisServer.LOBBY_CHANNEL).send(null, Protocol.gameStarted(id));
    }
