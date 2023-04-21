    private void ZipInfo(ZipOutputStream out) throws Exception {
        String filename = "TOPIC" + GetId() + ".topic";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            writer.println(id);
            writer.println(parentid);
            writer.println(site.GetId());
            writer.println(name);
            writer.println(alias);
            writer.println(code);
            writer.println(order);
            writer.println(default_article_state);
            writer.println(default_article_score);
            writer.println(table);
            if (article_template == null) writer.println(); else writer.println(article_template.GetId());
            writer.println(visible ? 1 : 0);
        } finally {
            out.closeEntry();
        }
    }
