    private void ZipInfo(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "FCK" + GetId() + ".fck";
        out.putNextEntry(new ZipEntry(filename));
        ZipWriter writer = new ZipWriter(out);
        try {
            writer.println(id);
            writer.println(title);
            writer.println(scope);
            writer.println(unitid);
            writer.println(creator);
            writer.println(createdate);
            writer.print(desc);
        } finally {
            out.closeEntry();
        }
    }
