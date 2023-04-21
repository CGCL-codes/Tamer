    public void testWriteEmptyGedcom() throws IOException, GedcomWriterException {
        Gedcom g = new Gedcom();
        GedcomWriter gw = new GedcomWriter(g);
        gw.validationSuppressed = true;
        File tempFile = new File("tmp/gedcom4j.emptywritertest.ged");
        gw.write(tempFile);
        assertLineSequence("Empty file contents not as expected", readBack(tempFile), "0 HEAD", "1 FILE gedcom4j.emptywritertest.ged", "1 GEDC", "2 VERS 5.5", "2 FORM LINEAGE-LINKED", "1 CHAR ANSEL", "0 @SUBMISSION@ SUBN", "0 TRLR");
    }
