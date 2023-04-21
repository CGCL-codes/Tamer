    public void write() {
        try {
            if (!this.useCompression) {
                this.out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outFilename), "UTF8"));
            } else {
                this.outFilename = this.outFilename.substring(0, this.outFilename.length() - 4);
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(this.outFilename + ".kmz"));
                this.out = new BufferedWriter(new OutputStreamWriter(zipOut, "UTF8"));
                ZipEntry ze = new ZipEntry(this.outFilename + ".kml");
                ze.setMethod(ZipEntry.DEFLATED);
                zipOut.putNextEntry(ze);
            }
            this.writeXMLDeclaration(this.out);
            int offset = 0;
            String offsetString = "  ";
            this.kml.writeKML(this.out, this.xmlNS, offset, offsetString, this.xmlNS);
            this.out.flush();
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
