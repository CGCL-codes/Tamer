    public long[] getMessagePositions() throws IOException {
        if (messagePositions == null) {
            final long length = getChannel().size();
            log.debug("Channel size [" + String.valueOf(length) + "] bytes");
            if (0 == length) return new long[0];
            List posList = new ArrayList();
            final long frame = 32000;
            final long stepback = FROM__PATTERN.length() - 1;
            long size = (length < frame ? length : frame);
            long offset = 0;
            FileChannel chnnl = getChannel();
            ByteBuffer buffer = chnnl.map(FileChannel.MapMode.READ_ONLY, offset, size);
            CharBuffer cb = decoder.decode(buffer);
            if (Pattern.compile(INITIAL_FROM__PATTERN, Pattern.DOTALL).matcher(cb).matches()) {
                log.debug("Matched first message..");
                posList.add(new Long(0));
            }
            Pattern fromPattern = Pattern.compile(FROM__PATTERN);
            Matcher matcher;
            do {
                matcher = fromPattern.matcher(cb);
                while (matcher.find()) {
                    log.debug("Found match at [" + String.valueOf(offset + matcher.start()) + "]");
                    posList.add(new Long(offset + matcher.start() + 1));
                }
                if (size < frame) break;
                size = offset + size + frame < length ? frame : length - (offset + size + 1);
                offset += size - stepback;
                buffer = chnnl.map(FileChannel.MapMode.READ_ONLY, offset, size);
                cb = decoder.decode(buffer);
            } while (true);
            messagePositions = new long[posList.size()];
            int count = 0;
            for (Iterator i = posList.iterator(); i.hasNext(); count++) {
                messagePositions[count] = ((Long) i.next()).longValue();
            }
        }
        return messagePositions;
    }
