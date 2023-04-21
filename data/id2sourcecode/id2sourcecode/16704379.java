    public void testReadWrite() throws Exception {
        String s = "JTextComponent\nRead\nWrite\n";
        String sProperty = "CurrentStreamDescriptionProperty";
        StringWriter writer = new StringWriter();
        jtc.setText(s);
        try {
            jtc.write(writer);
        } catch (IOException e) {
        }
        jtc.setText("temporary");
        assertEquals("temporary", jtc.getText());
        StringReader reader = new StringReader(writer.toString());
        try {
            jtc.read(reader, sProperty);
        } catch (IOException e) {
        }
        assertEquals(s, jtc.getText());
        assertEquals(sProperty, jtc.getDocument().getProperty(Document.StreamDescriptionProperty));
        assertTrue(jtc.getDocument() instanceof PlainDocument);
    }
