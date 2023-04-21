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
                throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.jpeg.file", errorID));
            }
            boolean firstPass = true;
            int len;
            while (true) {
                int v = is.read();
                if (v < 0) throw new IOException(MessageLocalization.getComposedMessage("premature.eof.while.reading.jpg"));
                if (v == 0xFF) {
                    int marker = is.read();
                    if (firstPass && marker == M_APP0) {
                        firstPass = false;
                        len = getShort(is);
                        if (len < 16) {
                            Utilities.skip(is, len - 2);
                            continue;
                        }
                        byte bcomp[] = new byte[JFIF_ID.length];
                        int r = is.read(bcomp);
                        if (r != bcomp.length) throw new BadElementException(MessageLocalization.getComposedMessage("1.corrupted.jfif.marker", errorID));
                        boolean found = true;
                        for (int k = 0; k < bcomp.length; ++k) {
                            if (bcomp[k] != JFIF_ID[k]) {
                                found = false;
                                break;
                            }
                        }
                        if (!found) {
                            Utilities.skip(is, len - 2 - bcomp.length);
                            continue;
                        }
                        Utilities.skip(is, 2);
                        int units = is.read();
                        int dx = getShort(is);
                        int dy = getShort(is);
                        if (units == 1) {
                            dpiX = dx;
                            dpiY = dy;
                        } else if (units == 2) {
                            dpiX = (int) (dx * 2.54f + 0.5f);
                            dpiY = (int) (dy * 2.54f + 0.5f);
                        }
                        Utilities.skip(is, len - 2 - bcomp.length - 7);
                        continue;
                    }
                    if (marker == M_APPE) {
                        len = getShort(is) - 2;
                        byte[] byteappe = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteappe[k] = (byte) is.read();
                        }
                        if (byteappe.length >= 12) {
                            String appe = new String(byteappe, 0, 5, "ISO-8859-1");
                            if (appe.equals("Adobe")) {
                                invert = true;
                            }
                        }
                        continue;
                    }
                    if (marker == M_APP2) {
                        len = getShort(is) - 2;
                        byte[] byteapp2 = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteapp2[k] = (byte) is.read();
                        }
                        if (byteapp2.length >= 14) {
                            String app2 = new String(byteapp2, 0, 11, "ISO-8859-1");
                            if (app2.equals("ICC_PROFILE")) {
                                int order = byteapp2[12] & 0xff;
                                int count = byteapp2[13] & 0xff;
                                if (order < 1) order = 1;
                                if (count < 1) count = 1;
                                if (icc == null) icc = new byte[count][];
                                icc[order - 1] = byteapp2;
                            }
                        }
                        continue;
                    }
                    firstPass = false;
                    int markertype = marker(marker);
                    if (markertype == VALID_MARKER) {
                        Utilities.skip(is, 2);
                        if (is.read() != 0x08) {
                            throw new BadElementException(MessageLocalization.getComposedMessage("1.must.have.8.bits.per.component", errorID));
                        }
                        scaledHeight = getShort(is);
                        setTop(scaledHeight);
                        scaledWidth = getShort(is);
                        setRight(scaledWidth);
                        colorspace = is.read();
                        bpc = 8;
                        break;
                    } else if (markertype == UNSUPPORTED_MARKER) {
                        throw new BadElementException(MessageLocalization.getComposedMessage("1.unsupported.jpeg.marker.2", errorID, String.valueOf(marker)));
                    } else if (markertype != NOPARAM_MARKER) {
                        Utilities.skip(is, getShort(is) - 2);
                    }
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        plainWidth = getWidth();
        plainHeight = getHeight();
        if (icc != null) {
            int total = 0;
            for (int k = 0; k < icc.length; ++k) {
                if (icc[k] == null) {
                    icc = null;
                    return;
                }
                total += icc[k].length - 14;
            }
            byte[] ficc = new byte[total];
            total = 0;
            for (int k = 0; k < icc.length; ++k) {
                System.arraycopy(icc[k], 14, ficc, total, icc[k].length - 14);
                total += icc[k].length - 14;
            }
            try {
                ICC_Profile icc_prof = ICC_Profile.getInstance(ficc);
                tagICC(icc_prof);
            } catch (IllegalArgumentException e) {
            }
            icc = null;
        }
    }
