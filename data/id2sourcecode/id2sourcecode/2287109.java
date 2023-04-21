    protected void writeAMF3Collection(Collection<?> c) throws IOException {
        if (debugMore) logMore.debug("writeAMF3Collection(c=%s)", c);
        Channel channel = getChannel();
        if (channel != null && channel.isLegacyCollectionSerialization()) writeAMF3Array(c.toArray()); else {
            ArrayCollection ac = (c instanceof ArrayCollection ? (ArrayCollection) c : new ArrayCollection(c));
            writeAMF3Object(ac);
        }
    }
