    public boolean copyRangeFrom(AudioTrail srcTrail, Span copySpan, long insertPos, int mode, Object source, AbstractCompoundEdit ce, int[] trackMap, BlendContext bcPre, BlendContext bcPost) throws IOException {
        if (trackMap.length != this.getChannelNum()) throw new IllegalArgumentException("trackMap : " + trackMap);
        if (trackMap.length == 0) return true;
        final boolean hasBlend = (bcPre != null) && (bcPre.getLen() > 0) || (bcPost != null) && (bcPost.getLen() > 0);
        final AudioStake writeStake;
        final long len = copySpan.getLength();
        final int bufLen = (int) Math.min(len, BUFSIZE);
        final double progWeight = 1.0 / len;
        writeStake = alloc(new Span(insertPos, insertPos + len));
        try {
            switch(mode) {
                case MODE_INSERT:
                    if (hasBlend) {
                        insertRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight, bcPre, bcPost);
                    } else {
                        insertRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight);
                    }
                    break;
                case MODE_MIX:
                    if (hasBlend) {
                        mixRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight, bcPre, bcPost);
                    } else {
                        mixRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight);
                    }
                    break;
                case MODE_OVERWRITE:
                    if (hasBlend) {
                        overwriteRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight, bcPre, bcPost);
                    } else {
                        overwriteRangeFrom(srcTrail, copySpan.start, writeStake, insertPos, len, bufLen, trackMap, progWeight);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("mode: " + mode);
            }
            writeStake.flush();
            this.editAdd(source, writeStake, ce);
            return true;
        } catch (InterruptedException e1) {
            writeStake.dispose();
            return false;
        } catch (IOException e1) {
            writeStake.dispose();
            throw e1;
        }
    }
