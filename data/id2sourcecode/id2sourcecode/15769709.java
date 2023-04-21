    public DicomReader(URL url) throws java.io.IOException {
        URLConnection u = url.openConnection();
        int size = u.getContentLength();
        byte[] array = new byte[size];
        int bytes_read = 0;
        DataInputStream in = new DataInputStream(u.getInputStream());
        while (bytes_read < size) {
            bytes_read += in.read(array, bytes_read, size - bytes_read);
        }
        in.close();
        this.dHR = new DicomHeaderReader(array);
        h = dHR.getRows();
        w = dHR.getColumns();
        highBit = dHR.getHighBit();
        bitsStored = dHR.getBitStored();
        bitsAllocated = dHR.getBitAllocated();
        n = (bitsAllocated / 8);
        signed = (dHR.getPixelRepresentation() == 1);
        this.pixData = dHR.getPixels();
        ignoreNegValues = true;
        samplesPerPixel = dHR.getSamplesPerPixel();
        numberOfFrames = dHR.getNumberOfFrames();
    }
