    private void processRaw() throws IOException {
        int pos;
        int i;
        while (state != STATE_DONE) {
            switch(state) {
                case STATE_AWAITING_CHUNK_HEADER:
                    pos = rawPos;
                    while (pos < rawCount) {
                        if (rawData[pos] == '\n') {
                            break;
                        }
                        pos++;
                    }
                    if (pos >= rawCount) {
                        return;
                    }
                    String header = new String(rawData, rawPos, pos - rawPos + 1);
                    for (i = 0; i < header.length(); i++) {
                        if (Character.digit(header.charAt(i), 16) == -1) break;
                    }
                    try {
                        chunkSize = Integer.parseInt(header.substring(0, i), 16);
                    } catch (NumberFormatException e) {
                        error = true;
                        throw new IOException("Bogus chunk size");
                    }
                    rawPos = pos + 1;
                    chunkRead = 0;
                    if (chunkSize > 0) {
                        state = STATE_READING_CHUNK;
                    } else {
                        state = STATE_AWAITING_TRAILERS;
                    }
                    break;
                case STATE_READING_CHUNK:
                    if (rawPos >= rawCount) {
                        return;
                    }
                    int copyLen = Math.min(chunkSize - chunkRead, rawCount - rawPos);
                    if (chunkData.length < chunkCount + copyLen) {
                        int cnt = chunkCount - chunkPos;
                        if (chunkData.length < cnt + copyLen) {
                            byte tmp[] = new byte[cnt + copyLen];
                            System.arraycopy(chunkData, chunkPos, tmp, 0, cnt);
                            chunkData = tmp;
                        } else {
                            System.arraycopy(chunkData, chunkPos, chunkData, 0, cnt);
                        }
                        chunkPos = 0;
                        chunkCount = cnt;
                    }
                    System.arraycopy(rawData, rawPos, chunkData, chunkCount, copyLen);
                    rawPos += copyLen;
                    chunkCount += copyLen;
                    chunkRead += copyLen;
                    if (chunkSize - chunkRead <= 0) {
                        state = STATE_AWAITING_CHUNK_EOL;
                    } else {
                        return;
                    }
                    break;
                case STATE_AWAITING_CHUNK_EOL:
                    if (rawPos + 1 >= rawCount) {
                        return;
                    }
                    if (rawData[rawPos] != '\r') {
                        error = true;
                        throw new IOException("missing CR");
                    }
                    if (rawData[rawPos + 1] != '\n') {
                        error = true;
                        throw new IOException("missing LF");
                    }
                    rawPos += 2;
                    state = STATE_AWAITING_CHUNK_HEADER;
                    break;
                case STATE_AWAITING_TRAILERS:
                    pos = rawPos;
                    while (pos < rawCount) {
                        if (rawData[pos] == '\n') {
                            break;
                        }
                        pos++;
                    }
                    if (pos >= rawCount) {
                        return;
                    }
                    if (pos == rawPos) {
                        error = true;
                        throw new IOException("LF should be proceeded by CR");
                    }
                    if (rawData[pos - 1] != '\r') {
                        error = true;
                        throw new IOException("LF should be proceeded by CR");
                    }
                    if (pos == (rawPos + 1)) {
                        state = STATE_DONE;
                        closeUnderlying();
                        return;
                    }
                    String trailer = new String(rawData, rawPos, pos - rawPos);
                    i = trailer.indexOf(':');
                    if (i == -1) {
                        throw new IOException("Malformed tailer - format should be key:value");
                    }
                    String key = (trailer.substring(0, i)).trim();
                    String value = (trailer.substring(i + 1, trailer.length())).trim();
                    responses.add(key, value);
                    rawPos = pos + 1;
                    break;
            }
        }
    }
