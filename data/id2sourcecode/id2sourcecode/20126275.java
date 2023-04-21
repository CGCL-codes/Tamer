    public void Zip(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "TRIGGER" + GetId() + ".trigger";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.println(id);
            writer.println(name);
            writer.println(topic.GetSiteId());
            writer.println(topic.GetCode());
            writer.println(enabled ? 1 : 0);
            writer.println(event);
            writer.println(lang);
        } finally {
            out.closeEntry();
        }
        ZipCode(out);
    }
