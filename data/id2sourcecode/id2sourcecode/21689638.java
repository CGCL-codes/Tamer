    @Test
    public void testLevelState() {
        final String ADDRESS = "level read/write tests";
        StatusCommand readCmd = getReadCommand(ADDRESS);
        String value = readCmd.read(EnumSensorType.LEVEL, new HashMap<String, String>());
        Assert.assertTrue(value.equalsIgnoreCase("0"));
        ExecutableCommand writeLevel1 = getWriteCommand(ADDRESS, "any command", 1);
        writeLevel1.send();
        value = readCmd.read(EnumSensorType.LEVEL, new HashMap<String, String>());
        Assert.assertTrue(value.equals("1"));
        ExecutableCommand writeLevel100 = getWriteCommand(ADDRESS, "any command", 100);
        writeLevel100.send();
        value = readCmd.read(EnumSensorType.LEVEL, new HashMap<String, String>());
        Assert.assertTrue(value.equals("100"));
    }
