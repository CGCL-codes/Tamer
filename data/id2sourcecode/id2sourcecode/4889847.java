    @Test
    public void equals() {
        PresetValue value1 = newPresetValue("name1", 1);
        PresetValue value2 = newPresetValue("name1", 1);
        PresetValue value3 = newPresetValue("name2", 1);
        assertTrue(value1.equals(value2));
        assertFalse(value1.equals(value3));
        value1.getChannel().setNumber(2);
        assertFalse(value1.equals(value2));
        value2.getChannel().setNumber(2);
        assertTrue(value1.equals(value2));
    }
