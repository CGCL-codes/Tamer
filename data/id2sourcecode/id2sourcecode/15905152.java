    private void checkConfigOutput(DatabaseTableConfig<?> config, StringBuilder body, StringWriter writer, BufferedWriter buffer, boolean hasFields) throws Exception {
        DatabaseTableConfigLoader.write(buffer, config);
        buffer.flush();
        StringBuilder output = new StringBuilder();
        output.append(TABLE_START).append(body);
        if (!hasFields) {
            output.append(TABLE_FIELDS_START);
        }
        output.append(TABLE_FIELDS_END);
        output.append(TABLE_END);
        assertEquals(output.toString(), writer.toString());
        StringReader reader = new StringReader(writer.toString());
        DatabaseTableConfig<?> configCopy = DatabaseTableConfigLoader.fromReader(new BufferedReader(reader));
        assertTrue(isConfigEquals(config, configCopy));
        writer.getBuffer().setLength(0);
    }
