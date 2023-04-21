    public void read(InputStream in) throws IOException {
        this.y = WMFConstants.readLittleEndianShort(in);
        this.x = WMFConstants.readLittleEndianShort(in);
        int strLen = WMFConstants.readLittleEndianShort(in);
        this.options = WMFConstants.readLittleEndianShort(in);
        if ((this.options & (OPAQUE | CLIPPED)) != 0) {
            this.rectangle = new Rect(in);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < strLen; i++) {
            baos.write(in.read());
        }
        this.text = new String(baos.toByteArray());
        ArrayList<Short> rdDx = new ArrayList<Short>();
        try {
            short dxVal = WMFConstants.readLittleEndianShort(in);
            rdDx.add(new Short(dxVal));
        } catch (IOException ioe) {
        }
        int size = rdDx.size();
        if (size > 0) {
            this.dx = new short[size];
            for (int i = 0; i < size; i++) this.dx[i] = rdDx.get(i).shortValue();
        } else this.dx = null;
    }
