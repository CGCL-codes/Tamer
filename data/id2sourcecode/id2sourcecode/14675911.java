    public void save(File file, ParticleSystem ps) throws IOException, DataFormatException {
        prepareParticleSystemForSaving(ps);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);
        ZipOutputStream out = new ZipOutputStream(bos);
        String filename = file.getName().substring(0, file.getName().indexOf('.'));
        ZipEntry entry = new ZipEntry(filename + ".xml");
        out.putNextEntry(entry);
        out.setMethod(ZipOutputStream.DEFLATED);
        IMarshallingContext mctx;
        try {
            mctx = bfact.createMarshallingContext();
            mctx.setIndent(4);
            mctx.marshalDocument(ps, "UTF-8", null, out);
            cleanParticleSystemAfterSaving(ps);
        } catch (JiBXException e) {
            e.printStackTrace();
            throw new DataFormatException(e);
        }
    }
