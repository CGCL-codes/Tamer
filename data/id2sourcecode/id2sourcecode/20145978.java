    private Long[] getMessagePositions() throws IOException {
        if (messagePositions == null) {
            List<Long> posList = new ArrayList<Long>();
            log.debug("Channel size [" + getChannel().size() + "] bytes");
            int bufferSize = (int) Math.min(getChannel().size(), DEFAULT_BUFFER_SIZE);
            CharSequence cs = null;
            ByteBuffer buffer = read(0, bufferSize);
            cs = decoder.decode(buffer);
            log.debug("Buffer [" + cs + "]");
            long offset = 0;
            for (; ; ) {
                Matcher matcher = null;
                if (CapabilityHints.isHintEnabled(CapabilityHints.KEY_MBOX_RELAXED_PARSING)) {
                    matcher = RELAXED_FROM__LINE_PATTERN.matcher(cs);
                } else {
                    matcher = FROM__LINE_PATTERN.matcher(cs);
                }
                while (matcher.find()) {
                    log.debug("Found match at [" + (offset + matcher.start()) + "]");
                    posList.add(Long.valueOf(offset + matcher.start()));
                }
                if (offset + bufferSize >= getChannel().size()) {
                    break;
                } else {
                    offset += bufferSize - FROM__PREFIX.length() - 2;
                    bufferSize = (int) Math.min(getChannel().size() - offset, DEFAULT_BUFFER_SIZE);
                    buffer = read(offset, bufferSize);
                    cs = decoder.decode(buffer);
                }
            }
            messagePositions = posList.toArray(new Long[posList.size()]);
        }
        return messagePositions;
    }
