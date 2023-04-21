    @Test
    public void TestNickCountAfterPartJoinsEtc() {
        int size = session.getChannel("#ubuntu").getNicks().size();
        assertTrue(size == 1224);
    }
