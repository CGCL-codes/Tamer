    private void ZipPageTemplates(ZipOutputStream out) throws Exception {
        if (page_templates == null || page_templates.isEmpty()) return;
        String filename = "TOPIC" + GetId() + ".pts";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            for (Object obj : page_templates) {
                PageTemplate pt = (PageTemplate) obj;
                if (pt != null) {
                    writer.println(pt.GetId());
                }
            }
        } finally {
            out.closeEntry();
        }
    }
