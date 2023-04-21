    public ID3v2Tag readTag(InputStream in) throws ID3v2ParseException, IOException {
        ID3v2Tag tag = new ID3v2Tag(this);
        CRC32 crc32 = new CRC32();
        ID3v2Header header = new ID3v2Header();
        int position = read(header, in);
        if (header.useCRC) {
            logger.info("Spec240::readTag(InputStream): " + "A CRC32 (" + header.crc + ") was found");
            in = new CheckedInputStream(in, crc32);
        }
        tag.setUseFooter(header.useFooter);
        tag.setUseUnsynchronisation(header.useUnsynchronisation);
        tag.setUseCRC(header.useCRC);
        tag.setExperimental(header.isExperimental);
        tag.setUpdate(header.isUpdate);
        tag.setRestrictions(header.restrictions);
        logger.debug("Spec240::readTag(InputStream): " + "Reading frames.");
        FrameReader frameReader = new FrameReader(tag, in);
        while (position < header.tagSize) {
            int frameSize;
            ID3v2Frame currentFrame = frameReader.readFrame(true);
            if (currentFrame == null) break;
            if (currentFrame.getType() == ID3v2Frame.UNDEFINED_FRAME_TYPE) logger.info("Spec240::readTag(InputStream): " + "Frame('" + currentFrame.getID() + "').getType() == ID3v2Frame.UNDEFINED_FRAME_TYPE.");
            position += frameReader.getFrameSize();
            tag.add(currentFrame);
        }
        logger.debug("Spec240::readTag(InputStream): " + "Read " + tag.getFrameCount() + " frames.");
        int paddingSize = header.tagSize - position;
        if (paddingSize >= 4) paddingSize = paddingSize - 4;
        in.skip(paddingSize);
        logger.debug("Spec240::readTag(InputStream): " + "Read " + paddingSize + " bytes of padding.");
        if (header.useCRC) {
            if (header.crc != crc32.getValue()) {
                String msg = "Wrong CRC32 (computed value: " + crc32.getValue() + "; value found: " + header.crc;
                logger.error("Spec240::readTag(InputStream): " + msg);
                throw new ID3v2ParseException(msg);
            } else {
                logger.debug("Spec240::readTag(InputStream): CRC is correct.");
            }
        }
        if (header.useFooter) {
            logger.debug("Spec240::readTag(InputStream): Footer is present");
            if (paddingSize != 0) throw new ID3v2ParseException("Tag must not have padding if a footer is present");
            ID3v2Header footer = toFooter(header);
            read(footer, in);
            position += getSize(footer);
        }
        setTagSize(tag, position);
        tag.setPreferredSize(header.tagSize, ID3Constants.ABSOLUTE);
        return tag;
    }
