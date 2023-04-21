    @Test
    public void initialWriteReadTest() {
        System.out.println(" Test writing");
        container.write(new String[] { "1", "A", "3", "4", "5" });
        container.write(new String[] { "2", "B", "3", "4", "5" });
        container.write(new String[] { "3", "C", "3", "4", "5" });
        container.write(new String[] { "4", "D", "3", "4", "5" });
        container.write(new String[] { "5", "E", "3", "4", "5" });
        System.out.println(" ..Done!");
        System.out.println(" Test counting");
        long count = container.count();
        if (count != 5) fail("We have " + count + " instead of the expected record " + "count");
        System.out.println(" ..Done!");
        System.out.println("  Test reading after write");
        String[] record = container.read("4");
        String out = record[1];
        assertEquals(out, "D");
        System.out.println("  ..Done!");
        System.out.println(" Test overwriting");
        container.write(new String[] { "1", "A", "AA", "4", "5" });
        container.write(new String[] { "2", "B", "BB", "4", "5" });
        container.write(new String[] { "3", "C", "CC", "4", "5" });
        container.write(new String[] { "4", "D", "XX", "4", "5" });
        container.write(new String[] { "5", "E", "ZZ", "4", "5" });
        System.out.println(" ..Done!");
        System.out.println("  Test reading after overwrite");
        record = container.read("2");
        out = record[2];
        assertEquals(out, "BB");
        System.out.println("  ..Done!");
    }
