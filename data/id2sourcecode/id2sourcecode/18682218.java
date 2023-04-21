            public int read() throws IOException {
                if (index < count) {
                    return buf[index++];
                } else {
                    index = 0;
                    reset();
                    int readCount = reader.read(characters);
                    if (readCount < 0) {
                        return -1;
                    } else {
                        writer.write(characters, 0, readCount);
                        writer.flush();
                        return buf[index++];
                    }
                }
            }
