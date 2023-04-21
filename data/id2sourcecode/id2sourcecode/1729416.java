    @Test
    public void testRangeState() {
        final String ADDRESS = "range read/write tests";
        StatusCommand readCmd = getReadCommand(ADDRESS);
        String value = readCmd.read(EnumSensorType.RANGE, new HashMap<String, String>());
        Assert.assertTrue(value.equalsIgnoreCase("0"));
        ExecutableCommand writeLevel1 = getWriteCommand(ADDRESS, "any command", 1);
        writeLevel1.send();
        value = readCmd.read(EnumSensorType.RANGE, new HashMap<String, String>());
        Assert.assertTrue(value.equals("1"));
        ExecutableCommand writeLevel10001 = getWriteCommand(ADDRESS, "any command", 10001);
        writeLevel10001.send();
        value = readCmd.read(EnumSensorType.RANGE, new HashMap<String, String>());
        Assert.assertTrue("Was expecting '10001', got '" + value + "'.", value.equals("10001"));
    }
