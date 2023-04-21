    public void saveMyself(Document doc, ZipOutputStream out) throws IOException {
        org.w3c.dom.Element cover = null;
        switch(getType()) {
            case CDFRONT:
                cover = doc.createElementNS("http://cdox.sf.net/schema/fileformat", "cdfront");
                break;
            case CDBOOKLET:
                cover = doc.createElementNS("http://cdox.sf.net/schema/fileformat", "cdbooklet");
                break;
            case CDBACK_SIDE:
                cover = doc.createElementNS("http://cdox.sf.net/schema/fileformat", "cdback");
                break;
        }
        if (backColor != null) cover.setAttribute("color", backColor.getRGB() + "");
        if (backgroundImage != null) {
            cover.setAttribute("image", this + ".back.image.png");
            out.putNextEntry(new ZipEntry(this + ".back.image.png"));
            if (origBackImage == null) {
                origBackImage = CDoxFrame.loadImage(TempFiles.get(backgroundSource));
            }
            ImageIO.write(origBackImage, "png", out);
            origBackImage = null;
            out.closeEntry();
            System.gc();
            if ((getType() == CDBACK_SIDE) && (backposition != 0)) cover.setAttribute("freesides", "true");
        }
        Iterator i = elements.iterator();
        while (i.hasNext()) ((cdox.edit.Element) i.next()).saveMyself(cover, out);
        doc.getDocumentElement().appendChild(cover);
    }
