    public void testGetIdYearCount() {
        Date dataD = null;
        SimpleDateFormat dataF = new SimpleDateFormat("dd/MM/yyyy");
        String dataS = "28/05/2007";
        try {
            dataD = dataF.parse(dataS);
            dataS = dataF.format(dataD);
            dataS = "01/01/" + dataS.substring(6, 10);
            dataD = dataF.parse(dataS);
            assertEquals(java.sql.Date.valueOf("2007-01-01"), dataD);
            dataS = "31/12/" + dataS.substring(6, 10);
            dataD = dataF.parse(dataS);
            assertEquals(java.sql.Date.valueOf("2007-12-31"), dataD);
        } catch (Throwable t) {
            System.out.println("ERRORE");
        }
        dataS = "28/05/2008";
        try {
            dataD = dataF.parse(dataS);
        } catch (Throwable t) {
            System.out.println("ERRORE");
        }
        assertEquals(2, dao.getIdYearCount(dataD));
    }
