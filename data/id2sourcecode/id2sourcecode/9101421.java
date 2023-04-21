        public String normalize(String str) {
            char[] buffer = str.toCharArray();
            int write = 0;
            int lastWrite = 0;
            boolean wroteOne = false;
            int read = 0;
            while (read < buffer.length) {
                if (isXMLSpace(buffer[read])) {
                    if (wroteOne) buffer[write++] = ' ';
                    do {
                        read++;
                    } while (read < buffer.length && isXMLSpace(buffer[read]));
                } else {
                    buffer[write++] = buffer[read++];
                    wroteOne = true;
                    lastWrite = write;
                }
            }
            return new String(buffer, 0, lastWrite);
        }
