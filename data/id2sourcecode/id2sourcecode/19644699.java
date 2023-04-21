    private void checkConfigOutput(DatabaseFieldConfig config, StringBuilder body, StringWriter writer, BufferedWriter buffer) throws Exception {
        DatabaseFieldConfigLoader.write(buffer, config, TABLE_NAME);
        buffer.flush();
        StringBuilder output = new StringBuilder();
        output.append(FIELD_START).append(body).append(FIELD_END);
        assertEquals(output.toString(), writer.toString());
        StringReader reader = new StringReader(writer.toString());
        DatabaseFieldConfig configCopy = DatabaseFieldConfigLoader.fromReader(new BufferedReader(reader));
        assertTrue(isConfigEquals(config, configCopy));
        writer.getBuffer().setLength(0);
    }
