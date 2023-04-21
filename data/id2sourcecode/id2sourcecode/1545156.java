    public void testFactoryPlace() throws Exception {
        URL url = this.getClass().getResource("DelegateTest.xml");
        InputStream input = url.openStream();
        m_data = (new GameParser()).parse(input);
        input.close();
        PlayerID british = m_data.getPlayerList().getPlayerID("British");
        ITestDelegateBridge delegateBridge = getDelegateBridge(british(m_data));
        Territory egypt = territory("Anglo Sudan Egypt", m_data);
        UnitType factoryType = m_data.getUnitTypeList().getUnitType("factory");
        PlaceDelegate placeDelegate = placeDelegate(m_data);
        delegateBridge.setStepName("Place");
        delegateBridge.setPlayerID(british);
        placeDelegate.start(delegateBridge, m_data);
        IntegerMap<UnitType> map = new IntegerMap<UnitType>();
        map.add(factoryType, 1);
        String response = placeDelegate.placeUnits(getUnits(map, british), egypt);
        assertValid(response);
        TerritoryAttachment ta = TerritoryAttachment.get(egypt);
        assertEquals(ta.getUnitProduction(), ta.getProduction());
    }
