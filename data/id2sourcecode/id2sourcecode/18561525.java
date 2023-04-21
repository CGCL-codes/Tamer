    protected void writeRtfContentWithException() throws IOException {
        if (m_writer == null) {
            return;
        }
        if (url == null) {
            throw new ExternalGraphicException("The attribute 'url' of <fo:external-graphic> is null.");
        }
        String linkToRoot = System.getProperty("jfor_link_to_root");
        if (linkToRoot != null) {
            m_writer.write("{\\field {\\* \\fldinst { INCLUDEPICTURE \"");
            m_writer.write(linkToRoot);
            File urlFile = new File(url.getFile());
            m_writer.write(urlFile.getName());
            m_writer.write("\" \\\\* MERGEFORMAT \\\\d }}}");
            return;
        }
        getRtfFile().getLog().logInfo("Writing image '" + url + "'.");
        byte[] data = null;
        try {
            final BufferedInputStream bin = new BufferedInputStream(url.openStream());
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while (true) {
                final int datum = bin.read();
                if (datum == -1) break;
                bout.write(datum);
            }
            bout.flush();
            data = bout.toByteArray();
        } catch (Exception e) {
            throw new ExternalGraphicException("The attribute 'src' of <fo:external-graphic> has a invalid value: '" + url + "' (" + e + ")");
        }
        if (data == null) {
            return;
        }
        String file = url.getFile();
        int type = determineImageType(data, file.substring(file.lastIndexOf(".") + 1));
        if (type >= ImageConstants.I_TO_CONVERT_BASIS) {
            int to = ImageConstants.CONVERT_TO[type - ImageConstants.I_TO_CONVERT_BASIS];
            if (to == ImageConstants.I_JPG) {
                try {
                    final IJpegEncoder jpgEncoder = new JpegEncoderFactory().getEncoder();
                    data = jpgEncoder.encodeJPEG(graphicCompressionRate, data);
                    type = to;
                } catch (JPEGException e) {
                    throw new IOException("JPEG conversion error, src = '" + url + "' (" + e + ")");
                }
            } else {
                type = ImageConstants.I_NOT_SUPPORTED;
            }
        }
        if (type == ImageConstants.I_NOT_SUPPORTED) {
            throw new ExternalGraphicException("The tag <fo:external-graphic> does not support " + file.substring(file.lastIndexOf(".") + 1) + " - image type.");
        }
        String rtfImageCode = ImageConstants.RTF_TAGS[type];
        writeGroupMark(true);
        writeStarControlWord("shppict");
        writeGroupMark(true);
        writeControlWord("pict");
        StringBuffer buf = new StringBuffer(data.length * 3);
        writeControlWord(rtfImageCode);
        if (type == ImageConstants.I_PNG) {
            width = ImageUtil.getIntFromByteArray(data, 16, 4, true);
            height = ImageUtil.getIntFromByteArray(data, 20, 4, true);
        } else if (type == ImageConstants.I_JPG) {
            int basis = -1;
            byte ff = (byte) 0xff;
            byte c0 = (byte) 0xc0;
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                if (b != ff) continue;
                if (i == data.length - 1) continue;
                b = data[i + 1];
                if (b != c0) continue;
                basis = i + 5;
                break;
            }
            if (basis != -1) {
                width = ImageUtil.getIntFromByteArray(data, basis + 2, 2, true);
                height = ImageUtil.getIntFromByteArray(data, basis, 2, true);
            }
        } else if (type == ImageConstants.I_EMF) {
            width = ImageUtil.getIntFromByteArray(data, 151, 4, false);
            height = ImageUtil.getIntFromByteArray(data, 155, 4, false);
        }
        if (width != -1) {
            writeControlWord("picw" + width);
        }
        if (height != -1) {
            writeControlWord("pich" + height);
        }
        if (widthDesired != -1) {
            if (perCentW) {
                writeControlWord("picscalex" + widthDesired);
            } else {
                writeControlWord("picscalex" + widthDesired * 100 / width);
            }
        } else if (scaleUniform && heightDesired != -1) {
            if (perCentH) {
                writeControlWord("picscalex" + heightDesired);
            } else {
                writeControlWord("picscalex" + heightDesired * 100 / height);
            }
        }
        if (heightDesired != -1) {
            if (perCentH) {
                writeControlWord("picscaley" + heightDesired);
            } else {
                writeControlWord("picscaley" + heightDesired * 100 / height);
            }
        } else if (scaleUniform && widthDesired != -1) {
            if (perCentW) {
                writeControlWord("picscaley" + widthDesired);
            } else {
                writeControlWord("picscaley" + widthDesired * 100 / width);
            }
        }
        for (int i = 0; i < data.length; i++) {
            int iData = data[i];
            if (iData < 0) iData += 256;
            if (iData < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(iData));
        }
        int len = buf.length();
        char[] chars = new char[len];
        buf.getChars(0, len, chars, 0);
        m_writer.write(chars);
        writeGroupMark(false);
        writeGroupMark(false);
    }
