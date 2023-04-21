    public int getIndexedLinePointer(final CharSequence target, int start, final String fileName, final boolean fileNameWnRelative) throws IOException {
        if (target.length() == 0) {
            return -1;
        }
        if (log.isTraceEnabled()) {
            log.trace("target: " + target + " fileName: " + fileName);
        }
        final CharStream stream = getFileStream(fileName, fileNameWnRelative);
        requireStream(stream, fileName);
        synchronized (stream) {
            int stop = stream.length();
            while (true) {
                final int midpoint = (start + stop) / 2;
                stream.seek(midpoint);
                stream.skipLine();
                final int offset = stream.position();
                if (log.isTraceEnabled()) {
                    log.trace("  " + start + ", " + midpoint + ", " + stop + " → " + offset);
                }
                if (offset == start) {
                    return -start - 1;
                } else if (offset == stop) {
                    if (start != 0 && stream.charAt(start - 1) != '\n') {
                        stream.seek(start + 1);
                        stream.skipLine();
                    } else {
                        stream.seek(start);
                    }
                    if (log.isTraceEnabled()) {
                        log.trace(". " + stream.position());
                    }
                    while (stream.position() < stop) {
                        final int result = stream.position();
                        final CharSequence word = stream.readLineWord();
                        if (log.isTraceEnabled()) {
                            log.trace("  . \"" + word + "\" → " + (0 == compare(target, word)));
                        }
                        final int compare = compare(target, word);
                        if (compare == 0) {
                            return result;
                        } else if (compare < 0) {
                            return -result - 1;
                        }
                    }
                    return -stop - 1;
                }
                final int result = stream.position();
                final CharSequence word = stream.readLineWord();
                final int compare = compare(target, word);
                if (log.isTraceEnabled()) {
                    log.trace(word + ": " + compare);
                }
                if (compare == 0) {
                    return result;
                }
                if (compare > 0) {
                    start = offset;
                } else {
                    assert compare < 0;
                    stop = offset;
                }
            }
        }
    }
