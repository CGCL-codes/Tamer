    @Override
    public void setUp() throws Exception {
        final URL url = this.getClass().getResource("DelegateTest.xml");
        final InputStream input = url.openStream();
        m_data = (new GameParser()).parse(input, false);
        input.close();
        black = m_data.getPlayerList().getPlayerID("Black");
        white = m_data.getPlayerList().getPlayerID("White");
        territories = new Territory[m_data.getMap().getXDimension()][m_data.getMap().getYDimension()];
        for (int x = 0; x < m_data.getMap().getXDimension(); x++) for (int y = 0; y < m_data.getMap().getYDimension(); y++) territories[x][y] = m_data.getMap().getTerritoryFromCoordinates(x, y);
        pawn = m_data.getUnitTypeList().getUnitType("pawn");
        king = m_data.getUnitTypeList().getUnitType("king");
    }
