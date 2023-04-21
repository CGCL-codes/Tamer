    @Override
    public void prepareWriteSequence(IIOMetadata metadata) throws IOException {
        if (dos != null) throw new IOException("Already written the DICOM object header - can't write it again.");
        DicomStreamMetaData dmeta = (DicomStreamMetaData) metadata;
        DicomObject dobj = dmeta.getDicomObject();
        Object output = getOutput();
        dos = new DicomOutputStream((ImageOutputStream) output);
        dos.setAutoFinish(false);
        dos.writeDicomFile(dobj);
        setupWriter(metadata);
        if (encapsulated) {
            dos.writeHeader(Tag.PixelData, VR.OB, -1);
            dos.writeHeader(Tag.Item, null, 0);
        } else {
            int frames = dobj.getInt(Tag.NumberOfFrames, 1);
            int width = dobj.getInt(Tag.Columns);
            int height = dobj.getInt(Tag.Rows);
            int bytes = dobj.getInt(Tag.BitsStored, 8) / 8;
            int samples = dobj.getInt(Tag.SamplesPerPixel);
            int size = frames * width * height * bytes * samples;
            dos.writeHeader(Tag.PixelData, VR.OB, size);
        }
        dos.flush();
        ((ImageOutputStream) output).flush();
    }
