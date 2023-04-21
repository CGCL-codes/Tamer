    private void check() throws HeapException {
        final Marshallizable fileStorableForRead;
        fileForStorable.open();
        fileStorableForWrite.writeToFile(fileForStorable);
        assertFalse("must not be in state value changed since just writed", fileStorableForWrite.isValueChanged());
        fileStorableForRead = new Marshallizable();
        fileStorableForRead.readFromFile(fileForStorable);
        assertFalse("must be in state just created", fileStorableForRead.isJustCreated());
        assertFalse("must not be in state value changed", fileStorableForRead.isValueChanged());
        fileForStorable.close();
        assertEquals("writed and readed long value must be equals", fileStorableForWrite.longValue, fileStorableForRead.longValue);
        assertEquals("writed and readed int value must be equals", fileStorableForWrite.intValue, fileStorableForRead.intValue);
        assertEquals("writed and readed boolean value must be equals", fileStorableForWrite.booleanValue, fileStorableForRead.booleanValue);
    }
