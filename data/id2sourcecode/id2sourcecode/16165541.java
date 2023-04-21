    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (canEncode()) {
            DeflaterOutputStream encoderOutputStream = null;
            if (this.encoding.equals(Encoding.GZIP)) {
                encoderOutputStream = new GZIPOutputStream(outputStream);
            } else if (this.encoding.equals(Encoding.DEFLATE)) {
                encoderOutputStream = new DeflaterOutputStream(outputStream);
            } else if (this.encoding.equals(Encoding.ZIP)) {
                final ZipOutputStream stream = new ZipOutputStream(outputStream);
                String name = "entry";
                if (getWrappedRepresentation().getDisposition() != null) {
                    name = getWrappedRepresentation().getDisposition().getParameters().getFirstValue(Disposition.NAME_FILENAME, true, name);
                }
                stream.putNextEntry(new ZipEntry(name));
                encoderOutputStream = stream;
            } else if (this.encoding.equals(Encoding.IDENTITY)) {
            }
            if (encoderOutputStream != null) {
                getWrappedRepresentation().write(encoderOutputStream);
                encoderOutputStream.finish();
            } else {
                getWrappedRepresentation().write(outputStream);
            }
        } else {
            getWrappedRepresentation().write(outputStream);
        }
    }
