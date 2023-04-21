    @Test
    public void testGettersAndSetters() {
        Primary primary = primaryReadOnly.newInstance();
        String code = "testCode";
        DateWithoutMillis date = new DateWithoutMillis();
        String description = "testDescription";
        Double doubleField = 123.45;
        TestEnum enumField = TestEnum.SECOND;
        Long id = 100L;
        Long longField = 234L;
        String stringField = "testString";
        String nonReadWrite = "IShouldNotBeHere";
        primary.setCode(code);
        primary.setDateField(date);
        primary.setDescriptionValue(description);
        primary.setDoubleField(doubleField);
        primary.setEnumField(enumField);
        primary.setId(id);
        primary.setLongField(longField);
        primary.setStringField(stringField);
        primary.setNonReadWriteProperty(nonReadWrite);
        assertEquals("Code should match", code, primary.getCode());
        assertEquals("Date field should match", date, primary.getDateField());
        assertEquals("Description should match", description, primary.getDescriptionValue());
        assertEquals("Double field should match", doubleField, primary.getDoubleField());
        assertEquals("Enum field should match", enumField, primary.getEnumField());
        assertEquals("Id should match", id, primary.getId());
        assertEquals("Long field should match", longField, primary.getLongField());
        assertEquals("String field should match", stringField, primary.getStringField());
        assertEquals("Non read/write field should match", nonReadWrite, primary.getNonReadWriteProperty());
        primary.setAnotherIgnoredString("testy");
        assertNotNull("Should have been able to call the getter", primary.getIgnoredString());
    }
