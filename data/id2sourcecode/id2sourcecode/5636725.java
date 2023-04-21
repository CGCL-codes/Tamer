    private void xmlSchreibenStart(String datei) throws Exception {
        File file = new File(datei);
        System.out.println("Datei schreiben: " + file.getAbsolutePath());
        outFactory = XMLOutputFactory.newInstance();
        if (datei.endsWith(Konstanten.FORMAT_BZ2)) {
            bZip2CompressorOutputStream = new BZip2CompressorOutputStream(new FileOutputStream(file), 2);
            out = new OutputStreamWriter(bZip2CompressorOutputStream, Konstanten.KODIERUNG_UTF);
        } else if (datei.endsWith(Konstanten.FORMAT_ZIP)) {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            ZipEntry entry = new ZipEntry(Konstanten.XML_DATEI_FILME);
            zipOutputStream.putNextEntry(entry);
            out = new OutputStreamWriter(zipOutputStream, Konstanten.KODIERUNG_UTF);
        } else {
            out = new OutputStreamWriter(new FileOutputStream(file), Konstanten.KODIERUNG_UTF);
        }
        writer = outFactory.createXMLStreamWriter(out);
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement(Konstanten.XML_START);
        writer.writeCharacters("\n");
    }
