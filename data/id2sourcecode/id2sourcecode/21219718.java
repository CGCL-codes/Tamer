    private void ZipData(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "FCK" + GetId() + ".data";
        out.putNextEntry(new ZipEntry(filename));
        ZipWriter writer = new ZipWriter(out);
        try {
            writer.print(html);
        } finally {
            out.closeEntry();
        }
    }
