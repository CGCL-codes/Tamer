    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps, Map texts, String resourceFileName) throws IOException {
        zipos.putNextEntry(new ZipEntry(resourceFileName));
        for (Iterator iterator = texts.entrySet().iterator(); iterator.hasNext(); ) {
            Entry entry = (Entry) iterator.next();
            String keyEncoded = escape((String) entry.getKey(), true);
            ps.print(keyEncoded);
            ps.print("=");
            String valueEncoded = escape((String) entry.getValue(), false);
            ps.println(valueEncoded);
        }
        ps.flush();
        zipos.closeEntry();
    }
