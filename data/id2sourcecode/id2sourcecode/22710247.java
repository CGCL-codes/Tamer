    public void testToString() {
        String programString = DateFormatter.formatDate2TimeWithDay(programTest.getStartDate()) + "-" + DateFormatter.formatDate2Time(programTest.getStopDate()) + "   " + programTest.getTitle() + "   (" + programTest.getChannel() + ")";
        assertEquals(programString, programTest.toString());
    }
