    public void testCopy() throws Exception {
        EntryList list = serializer.read(EntryList.class, new StringReader(LIST));
        assertEquals(list.name, "example");
        assertTrue(list.list instanceof Vector);
        Entry entry = new Entry();
        entry.id = 1234;
        entry.text = "replacement";
        list.list = new ArrayList();
        list.name = "change";
        list.list.add(entry);
        StringWriter writer = new StringWriter();
        serializer.write(list, writer);
        serializer.write(list, System.out);
        assertTrue(writer.toString().indexOf("java.util.ArrayList") > 0);
        assertTrue(writer.toString().indexOf("change") > 0);
        list = serializer.read(EntryList.class, new StringReader(writer.toString()));
        assertEquals(list.name, "change");
        assertTrue(list.list instanceof ArrayList);
        entry = list.getEntry(0);
        assertEquals(entry.id, 1234);
        assertEquals(entry.text, "replacement");
    }
