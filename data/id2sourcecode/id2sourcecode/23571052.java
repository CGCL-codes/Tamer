    private void saveZipChildren(File file) throws IOException {
        if (isReadOnly()) {
            throw new IllegalStateException("This bundle was loaded from a read-only zip file." + " You must use saveAs to save it in a non read-only location");
        } else {
            FileOutputStream fout = new FileOutputStream(file);
            ZipOutputStream zout = new ZipOutputStream(fout);
            for (int x = 0; x < bundleDocs.size(); x++) {
                BundledDocumentHandle docHandle = bundleDocs.get(x);
                SCAPDocument sdoc = docHandle.getDocument();
                Document dom = sdoc.getDoc();
                ZipEntry entry = new ZipEntry(sdoc.getFilename());
                zout.putNextEntry(entry);
                XMLOutputter xmlo = new XMLOutputter(Format.getPrettyFormat());
                xmlo.output(dom, zout);
            }
            zout.close();
        }
    }
