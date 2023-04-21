    protected void loadImage() throws FopImageException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream iccStream = new ByteArrayOutputStream();
        InputStream inStream = null;
        this.m_colorSpace = new ColorSpace(ColorSpace.DEVICE_UNKNOWN);
        byte[] readBuf = new byte[4096];
        int bytes_read;
        int index = 0;
        boolean cont = true;
        this.m_compressionType = new DCTFilter();
        this.m_compressionType.setApplied(true);
        boolean isOpen = false;
        try {
            inStream = this.m_href.openStream();
            isOpen = true;
            while ((bytes_read = inStream.read(readBuf)) != -1) {
                baos.write(readBuf, 0, bytes_read);
            }
        } catch (java.io.IOException ex) {
            throw new FopImageException("Error while loading image " + this.m_href.toString() + " : " + ex.getClass() + " - " + ex.getMessage());
        } finally {
            try {
                if (inStream != null && isOpen) inStream.close();
            } catch (java.io.IOException ex) {
                throw new FopImageException("Error on closing image " + this.m_href.toString() + " : " + ex.getClass() + " - " + ex.getMessage());
            }
        }
        this.m_bitmaps = baos.toByteArray();
        this.m_bitsPerPixel = 8;
        this.m_isTransparent = false;
        if (this.m_bitmaps.length > (index + 2) && uByte(this.m_bitmaps[index]) == 255 && uByte(this.m_bitmaps[index + 1]) == 216) {
            index += 2;
            while (index < this.m_bitmaps.length && cont) {
                if (this.m_bitmaps.length > (index + 2) && uByte(this.m_bitmaps[index]) == 255) {
                    if (uByte(this.m_bitmaps[index + 1]) == 192 || uByte(this.m_bitmaps[index + 1]) == 194) {
                        this.m_height = calcBytes(this.m_bitmaps[index + 5], this.m_bitmaps[index + 6]);
                        this.m_width = calcBytes(this.m_bitmaps[index + 7], this.m_bitmaps[index + 8]);
                        if (this.m_bitmaps[index + 9] == 1) {
                            this.m_colorSpace.setColorSpace(ColorSpace.DEVICE_GRAY);
                        } else if (this.m_bitmaps[index + 9] == 3) {
                            this.m_colorSpace.setColorSpace(ColorSpace.DEVICE_RGB);
                        } else if (this.m_bitmaps[index + 9] == 4) {
                            this.m_colorSpace.setColorSpace(ColorSpace.DEVICE_CMYK);
                        }
                        found_dimensions = true;
                        if (found_icc_profile) {
                            cont = false;
                            break;
                        }
                        index += calcBytes(this.m_bitmaps[index + 2], this.m_bitmaps[index + 3]) + 2;
                    } else if (uByte(this.m_bitmaps[index + 1]) == 226 && this.m_bitmaps.length > (index + 60)) {
                        byte[] icc_string = new byte[11];
                        System.arraycopy(this.m_bitmaps, index + 4, icc_string, 0, 11);
                        if ("ICC_PROFILE".equals(new String(icc_string))) {
                            int chunkSize = calcBytes(this.m_bitmaps[index + 2], this.m_bitmaps[index + 3]) + 2;
                            iccStream.write(this.m_bitmaps, index + 16, chunkSize - 18);
                        }
                        index += calcBytes(this.m_bitmaps[index + 2], this.m_bitmaps[index + 3]) + 2;
                    } else if ((uByte(this.m_bitmaps[index]) == 0xff && uByte(this.m_bitmaps[index + 1]) == 0xee && uByte(this.m_bitmaps[index + 2]) == 0 && uByte(this.m_bitmaps[index + 3]) == 14 && "Adobe".equals(new String(this.m_bitmaps, index + 4, 5)))) {
                        hasAPPEMarker = true;
                        index += calcBytes(this.m_bitmaps[index + 2], this.m_bitmaps[index + 3]) + 2;
                    } else {
                        index += calcBytes(this.m_bitmaps[index + 2], this.m_bitmaps[index + 3]) + 2;
                    }
                } else {
                    cont = false;
                }
            }
        } else {
            throw new FopImageException("\n1 Error while loading image " + this.m_href.toString() + " : JpegImage - Invalid JPEG Header.");
        }
        if (iccStream.size() > 0) {
            byte[] align = new byte[((iccStream.size()) % 8) + 8];
            try {
                iccStream.write(align);
            } catch (Exception e) {
                throw new FopImageException("\n1 Error while loading image " + this.m_href.toString() + " : " + e.getMessage());
            }
            this.m_colorSpace.setICCProfile(iccStream.toByteArray());
        }
        if (hasAPPEMarker && this.m_colorSpace.getColorSpace() == ColorSpace.DEVICE_CMYK) this.m_invertImage = true;
    }
