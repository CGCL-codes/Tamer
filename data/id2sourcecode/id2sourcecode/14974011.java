    public final void setContentFrom(final Representation rep) {
        try {
            final ReadableByteChannel rb = rep.getChannel();
            final long expectedSize = rep.getSize();
            setContent(readContent(rb, (int) expectedSize));
            if (expectedSize < 0) {
                setSize(new Long(getContent().length));
            } else {
                setSize(expectedSize);
            }
            assert (rep != null);
            assert (rep.getMediaType() != null);
            mediaType = rep.getMediaType().getName();
            final CharacterSet cs = rep.getCharacterSet();
            if (cs != null) {
                charSetString = rep.getCharacterSet().getName();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        if (content == null) {
            throw new RuntimeException("NULL obval object!!!");
        }
    }
