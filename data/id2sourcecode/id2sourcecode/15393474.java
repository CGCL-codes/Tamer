    protected synchronized String generateSessionId() {
        byte random[] = new byte[16];
        String jvmRoute = getJvmRoute();
        String result = null;
        StringBuffer buffer = new StringBuffer();
        do {
            int resultLenBytes = 0;
            if (result != null) {
                buffer = new StringBuffer();
                duplicates++;
            }
            while (resultLenBytes < this.sessionIdLength) {
                getRandomBytes(random);
                random = getDigest().digest(random);
                for (int j = 0; j < random.length && resultLenBytes < this.sessionIdLength; j++) {
                    byte b1 = (byte) ((random[j] & 0xf0) >> 4);
                    byte b2 = (byte) (random[j] & 0x0f);
                    if (b1 < 10) buffer.append((char) ('0' + b1)); else buffer.append((char) ('A' + (b1 - 10)));
                    if (b2 < 10) buffer.append((char) ('0' + b2)); else buffer.append((char) ('A' + (b2 - 10)));
                    resultLenBytes++;
                }
            }
            if (jvmRoute != null) {
                buffer.append('.').append(jvmRoute);
            }
            result = buffer.toString();
        } while (sessions.containsKey(result));
        return (result);
    }
