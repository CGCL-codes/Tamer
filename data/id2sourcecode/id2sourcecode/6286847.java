    public void extractFirstRevision() throws IOException {
        PdfReader reader = new PdfReader(SIGNED2);
        AcroFields af = reader.getAcroFields();
        FileOutputStream os = new FileOutputStream(REVISION);
        byte bb[] = new byte[1028];
        InputStream ip = af.extractRevision("first");
        int n = 0;
        while ((n = ip.read(bb)) > 0) os.write(bb, 0, n);
        os.close();
        ip.close();
    }
