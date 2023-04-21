    private void processParameters() throws BadElementException, IOException {
        type = JPEG;
        originalType = ORIGINAL_JPEG;
        InputStream is = null;
        try {
            String errorID;
            if (rawData == null) {
                is = url.openStream();
                errorID = url.toString();
            } else {
                is = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            if (is.read() != 0xFF || is.read() != 0xD8) {
                throw new BadElementException(errorID + " is not a valid JPEG-file.");
            }
            boolean firstPass = true;
            int len;
            while (true) {
                int v = is.read();
                if (v < 0) throw new IOException("Premature EOF while reading JPG.");
                if (v == 0xFF) {
                    int marker = is.read();
                    if (firstPass && marker == M_APP0) {
                        firstPass = false;
                        len = getShort(is);
                        if (len < 16) {
                            skip(is, len - 2);
                            continue;
                        }
                        byte bcomp[] = new byte[JFIF_ID.length];
                        int r = is.read(bcomp);
                        if (r != bcomp.length) throw new BadElementException(errorID + " corrupted JFIF marker.");
                        boolean found = true;
                        for (int k = 0; k < bcomp.length; ++k) {
                            if (bcomp[k] != JFIF_ID[k]) {
                                found = false;
                                break;
                            }
                        }
                        if (!found) {
                            skip(is, len - 2 - bcomp.length);
                            continue;
                        }
                        skip(is, 2);
                        int units = is.read();
                        int dx = getShort(is);
                        int dy = getShort(is);
                        if (units == 1) {
                            dpiX = dx;
                            dpiY = dy;
                        } else if (units == 2) {
                            dpiX = (int) ((float) dx * 2.54f + 0.5f);
                            dpiY = (int) ((float) dy * 2.54f + 0.5f);
                        }
                        skip(is, len - 2 - bcomp.length - 7);
                        continue;
                    }
                    if (marker == M_APPE) {
                        len = getShort(is);
                        byte[] byteappe = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteappe[k] = (byte) is.read();
                        }
                        if (byteappe.length > 12) {
                            String appe = new String(byteappe, 0, 5, "ISO-8859-1");
                            if (appe.equals("Adobe")) {
                                invert = true;
                            }
                        }
                    }
                    firstPass = false;
                    int markertype = marker(marker);
                    if (markertype == VALID_MARKER) {
                        skip(is, 2);
                        if (is.read() != 0x08) {
                            throw new BadElementException(errorID + " must have 8 bits per component.");
                        }
                        scaledHeight = getShort(is);
                        setTop(scaledHeight);
                        scaledWidth = getShort(is);
                        setRight(scaledWidth);
                        colorspace = is.read();
                        bpc = 8;
                        break;
                    } else if (markertype == UNSUPPORTED_MARKER) {
                        throw new BadElementException(errorID + ": unsupported JPEG marker: " + marker);
                    } else if (markertype != NOPARAM_MARKER) {
                        skip(is, getShort(is) - 2);
                    }
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }
