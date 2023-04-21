    public void Zip(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "TOPIC.list";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            ZipSummary(writer, tree.GetChilds());
        } finally {
            out.closeEntry();
        }
        Zip(ctxt, out, tree.GetChilds());
    }
