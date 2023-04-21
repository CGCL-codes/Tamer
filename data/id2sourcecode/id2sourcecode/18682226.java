            public void write(int b) throws IOException {
                if (count < buf.length) {
                    buf[count++] = (byte) b;
                } else {
                    int readCount = reader.read(characters);
                    if (readCount > 0) {
                        writer.write(characters, 0, readCount);
                    }
                    count = 0;
                    index = 0;
                    pos = 0;
                    write(b);
                }
            }
