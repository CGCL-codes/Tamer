            public void flush() throws IOException {
                int readCount = reader.read(characters);
                if (readCount > 0) {
                    writer.write(characters, 0, readCount);
                }
                count = 0;
                index = 0;
                pos = 0;
            }
