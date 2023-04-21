            public void valueString(ParseState state, Reader value) throws IOException {
                java.io.Writer stringWriter = jsonWriter.writeStringAsWriter();
                int read = value.read();
                while (read >= 0) {
                    stringWriter.write(read);
                    read = value.read();
                }
                value.close();
                stringWriter.close();
            }
