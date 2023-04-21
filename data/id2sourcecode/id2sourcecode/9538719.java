    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps, Set keys, String resourceFileName) throws IOException {
        zipos.putNextEntry(new ZipEntry(resourceFileName));
        for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
            String value = (String) iterator.next();
            String keyEncoded = escape(value, true);
            ps.print(keyEncoded);
            ps.print("=");
            ps.println();
        }
        ps.flush();
        zipos.closeEntry();
    }
