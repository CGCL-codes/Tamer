    private void convert(DicomInputStream in, double[] k, String pmi) throws IOException {
        DicomObject attrs = in.getDicomObject();
        check("Unsupported Bits Allocated: ", 8, attrs.getInt(Tag.BitsAllocated));
        check("Wrong Samples per Pixel: ", 3, attrs.getInt(Tag.SamplesPerPixel));
        int valLen = in.valueLength();
        int planeLen = attrs.getInt(Tag.Columns) * attrs.getInt(Tag.Rows);
        int nFrames = attrs.getInt(Tag.NumberOfFrames, 1);
        int padded = valLen - planeLen * nFrames * 3;
        if (padded < 0) {
            throw new IOException("Too short Pixel Data: " + valLen);
        }
        boolean byPlane = attrs.getInt(Tag.PlanarConfiguration) != 0;
        attrs.putString(Tag.PhotometricInterpretation, VR.CS, pmi);
        FileOutputStream fos = new FileOutputStream(ofile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        dos = new DicomOutputStream(bos);
        dos.writeDicomFile(attrs);
        attrs.clear();
        dos.writeHeader(in.tag(), in.vr(), valLen);
        if (byPlane) {
            byte[] r = new byte[planeLen];
            byte[] g = new byte[planeLen];
            byte[] b = new byte[planeLen];
            int r1, g1, b1;
            for (int i = 0; i < nFrames; i++) {
                in.readFully(r);
                in.readFully(g);
                in.readFully(b);
                for (int j = 0; j < r.length; j++) {
                    r1 = r[j] & 0xff;
                    g1 = g[j] & 0xff;
                    b1 = b[j] & 0xff;
                    r[j] = (byte) Math.max(0, Math.min(255, k[0] * r1 + k[1] * g1 + k[2] * b1 + k[3]));
                    g[j] = (byte) Math.max(0, Math.min(255, k[4] * r1 + k[5] * g1 + k[6] * b1 + k[7]));
                    b[j] = (byte) Math.max(0, Math.min(255, k[8] * r1 + k[9] * g1 + k[10] * b1 + k[11]));
                }
                dos.write(r);
                dos.write(g);
                dos.write(b);
            }
        } else {
            int r, g, b;
            for (int i = 0; i < nFrames; i++) {
                for (int j = 0; j < planeLen; j++) {
                    r = in.read();
                    g = in.read();
                    b = in.read();
                    dos.write((int) Math.max(0, Math.min(255, k[0] * r + k[1] * g + k[2] * b + k[3])));
                    dos.write((int) Math.max(0, Math.min(255, k[4] * r + k[5] * g + k[6] * b + k[7])));
                    dos.write((int) Math.max(0, Math.min(255, k[8] * r + k[9] * g + k[10] * b + k[11])));
                }
            }
        }
        for (int i = 0; i < padded; i++) {
            dos.write(in.read());
        }
    }
