    public void Zip(NpsContext ctxt, ZipOutputStream out) throws Exception {
        String filename = "UNIT" + GetId() + ".unit";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.println(id);
            writer.println(name);
            writer.println(code);
            writer.println(address);
            writer.println(email);
            writer.println(attachman);
            writer.println(zipcode);
            writer.println(phonenum);
            writer.println(mobile);
        } finally {
            out.closeEntry();
        }
    }
