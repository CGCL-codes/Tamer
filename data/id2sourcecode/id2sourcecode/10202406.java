    public void write(String filename, TileSet tileSet) throws IOException {
        File file = new File(filename);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        ZipEntry catalog = new ZipEntry(CATALOG_FILE);
        out.putNextEntry(catalog);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        tileSet.write(writer);
        writer.flush();
        out.closeEntry();
        ZipEntry xmlCatalog = new ZipEntry(CATALOG_XML);
        out.putNextEntry(xmlCatalog);
        OutputStreamWriter xmlWriter = new OutputStreamWriter(out, "UTF-8");
        tileSet.writeXML(xmlWriter);
        xmlWriter.flush();
        out.closeEntry();
        final String format = "PNG";
        for (int i = 0; i < tileSet.size(); i++) {
            final TileDescriptor tld = tileSet.get(i);
            final String name = tld.getString(0) != null ? tld.getString(0) : "";
            final String pngfile = "" + i + "," + tld.tileId + "," + name + ".png";
            ZipEntry pngzip = new ZipEntry(pngfile);
            out.putNextEntry(pngzip);
            ImageIO.write(tld.img, format, out);
            out.closeEntry();
        }
        out.close();
    }
