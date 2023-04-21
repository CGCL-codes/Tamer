    public static String toString(Format f) {
        final List<String> list = new ArrayList<String>();
        list.add(f.getEncoding().toUpperCase());
        if (f instanceof AudioFormat) {
            final AudioFormat af = (AudioFormat) f;
            list.add(intToStr((int) af.getSampleRate()));
            list.add(intToStr(af.getSampleSizeInBits()));
            list.add(intToStr(af.getChannels()));
            list.add(endianToStr(af.getEndian()));
            list.add(signedToStr(af.getSigned()));
            list.add(intToStr(af.getFrameSizeInBits()));
            list.add(intToStr((int) af.getFrameRate()));
            if (af.getDataType() != null && af.getDataType() != Format.byteArray) list.add(dataTypeToStr(af.getDataType()));
        } else if (f instanceof VideoFormat) {
            final VideoFormat vf = (VideoFormat) f;
            if (f.getClass() == JPEGFormat.class) {
                final JPEGFormat jf = (JPEGFormat) vf;
                list.add(dimensionToStr(jf.getSize()));
                list.add(intToStr(jf.getMaxDataLength()));
                if (jf.getDataType() != null && jf.getDataType() != Format.byteArray) list.add(dataTypeToStr(jf.getDataType()));
                list.add(floatToStr(jf.getFrameRate()));
            } else if (f.getClass() == GIFFormat.class) {
                final GIFFormat gf = (GIFFormat) vf;
                list.add(dimensionToStr(gf.getSize()));
                list.add(intToStr(gf.getMaxDataLength()));
                if (gf.getDataType() != null && gf.getDataType() != Format.byteArray) list.add(dataTypeToStr(gf.getDataType()));
                list.add(floatToStr(gf.getFrameRate()));
            } else if (f.getClass() == PNGFormat.class) {
                final PNGFormat pf = (PNGFormat) vf;
                list.add(dimensionToStr(pf.getSize()));
                list.add(intToStr(pf.getMaxDataLength()));
                if (pf.getDataType() != null && pf.getDataType() != Format.byteArray) list.add(dataTypeToStr(pf.getDataType()));
                list.add(floatToStr(pf.getFrameRate()));
            } else if (f.getClass() == VideoFormat.class) {
                list.add(dimensionToStr(vf.getSize()));
                list.add(intToStr(vf.getMaxDataLength()));
                if (vf.getDataType() != null && vf.getDataType() != Format.byteArray) list.add(dataTypeToStr(vf.getDataType()));
                list.add(floatToStr(vf.getFrameRate()));
            } else if (f.getClass() == RGBFormat.class) {
                final RGBFormat rf = (RGBFormat) vf;
                list.add(dimensionToStr(vf.getSize()));
                list.add(intToStr(vf.getMaxDataLength()));
                if (vf.getDataType() != null && vf.getDataType() != Format.byteArray) list.add(dataTypeToStr(vf.getDataType()));
                list.add(floatToStr(vf.getFrameRate()));
                list.add(intToStr(rf.getBitsPerPixel()));
                list.add(intToStr(rf.getRedMask()));
                list.add(intToStr(rf.getGreenMask()));
                list.add(intToStr(rf.getBlueMask()));
                list.add(intToStr(rf.getPixelStride()));
                list.add(intToStr(rf.getLineStride()));
                list.add(intToStr(rf.getFlipped()));
                list.add(rgbFormatEndianToStr(rf.getEndian()));
            } else throw new IllegalArgumentException("Unknown or unsupported format: " + f);
        } else {
            throw new IllegalArgumentException("" + f);
        }
        while (list.get(list.size() - 1) == null || list.get(list.size() - 1).equals(NOT_SPECIFIED)) list.remove(list.size() - 1);
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) b.append(SEP);
            b.append(list.get(i));
        }
        return b.toString();
    }
