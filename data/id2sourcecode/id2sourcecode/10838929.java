    private void ZipInfo(ZipOutputStream out) throws Exception {
        String filename = "SITE" + GetId() + ".site";
        out.putNextEntry(new ZipEntry(filename));
        ZipWriter writer = new ZipWriter(out);
        try {
            writer.println(id);
            writer.println(name);
            writer.println(rooturl);
            writer.println(Utils.FixPath(art_publish_dir.getAbsolutePath()));
            writer.println(art_suffix);
            writer.println(Utils.FixPath(img_publish_dir.getAbsolutePath()));
            writer.println(img_rooturl);
            writer.println(unit.GetId());
            writer.println(state);
            writer.println(domain);
            writer.println(fulltext_index ? "1" : "0");
            writer.println(Utils.Null2Empty(solr_core));
        } finally {
            out.closeEntry();
        }
    }
