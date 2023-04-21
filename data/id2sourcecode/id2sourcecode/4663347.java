    private void processParameters() throws IOException {
        type = JPEG2000;
        originalType = ORIGINAL_JPEG2000;
        inp = null;
        try {
            String errorID;
            if (rawData == null) {
                inp = url.openStream();
                errorID = url.toString();
            } else {
                inp = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            boxLength = cio_read(4);
            if (boxLength == 0x0000000c) {
                boxType = cio_read(4);
                if (JP2_JP != boxType) {
                    throw new IOException(MessageLocalization.getComposedMessage("expected.jp.marker"));
                }
                if (0x0d0a870a != cio_read(4)) {
                    throw new IOException(MessageLocalization.getComposedMessage("error.with.jp.marker"));
                }
                jp2_read_boxhdr();
                if (JP2_FTYP != boxType) {
                    throw new IOException(MessageLocalization.getComposedMessage("expected.ftyp.marker"));
                }
                Utilities.skip(inp, boxLength - 8);
                jp2_read_boxhdr();
                do {
                    if (JP2_JP2H != boxType) {
                        if (boxType == JP2_JP2C) {
                            throw new IOException(MessageLocalization.getComposedMessage("expected.jp2h.marker"));
                        }
                        Utilities.skip(inp, boxLength - 8);
                        jp2_read_boxhdr();
                    }
                } while (JP2_JP2H != boxType);
                jp2_read_boxhdr();
                if (JP2_IHDR != boxType) {
                    throw new IOException(MessageLocalization.getComposedMessage("expected.ihdr.marker"));
                }
                scaledHeight = cio_read(4);
                setTop(scaledHeight);
                scaledWidth = cio_read(4);
                setRight(scaledWidth);
                bpc = -1;
            } else if (boxLength == 0xff4fff51) {
                Utilities.skip(inp, 4);
                int x1 = cio_read(4);
                int y1 = cio_read(4);
                int x0 = cio_read(4);
                int y0 = cio_read(4);
                Utilities.skip(inp, 16);
                colorspace = cio_read(2);
                bpc = 8;
                scaledHeight = y1 - y0;
                setTop(scaledHeight);
                scaledWidth = x1 - x0;
                setRight(scaledWidth);
            } else {
                throw new IOException(MessageLocalization.getComposedMessage("not.a.valid.jpeg2000.file"));
            }
        } finally {
            if (inp != null) {
                try {
                    inp.close();
                } catch (Exception e) {
                }
                inp = null;
            }
        }
        plainWidth = getWidth();
        plainHeight = getHeight();
    }
