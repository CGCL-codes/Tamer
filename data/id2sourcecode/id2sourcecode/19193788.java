    public static void main(String[] args) throws IOException {
        java.io.Reader fileReader = new java.io.InputStreamReader(new prisms.util.FileSegmentizerInputStream(args[0]));
        String outFileName;
        int dotIdx = args[0].lastIndexOf('.');
        if (dotIdx >= 0) outFileName = args[0].substring(0, dotIdx) + ".fmt.json"; else outFileName = args[0] + ".fmt.json";
        final java.io.Writer[] fileWriter = new java.io.Writer[] { new java.io.OutputStreamWriter(new prisms.util.FileSegmentizerOutputStream(outFileName)) };
        java.io.Writer writer = new java.io.Writer() {

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                System.out.print(new String(cbuf, off, len));
                fileWriter[0].write(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                System.out.flush();
                fileWriter[0].flush();
            }

            @Override
            public void close() throws IOException {
            }
        };
        final JsonStreamWriter jsonWriter = new JsonStreamWriter(writer);
        jsonWriter.setFormatIndent("\t");
        SAJParser.ParseHandler handler = new SAJParser.ParseHandler() {

            public void startObject(ParseState state) {
                try {
                    jsonWriter.startObject();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void startProperty(ParseState state, String name) {
                try {
                    jsonWriter.startProperty(name);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void separator(ParseState state) {
            }

            public void endProperty(ParseState state, String propName) {
            }

            public void endObject(ParseState state) {
                try {
                    jsonWriter.endObject();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void startArray(ParseState state) {
                try {
                    jsonWriter.startArray();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void endArray(ParseState state) {
                try {
                    jsonWriter.endArray();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void valueBoolean(ParseState state, boolean value) {
                try {
                    jsonWriter.writeBoolean(value);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

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

            public void valueNumber(ParseState state, Number value) {
                try {
                    jsonWriter.writeNumber(value);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void valueNull(ParseState state) {
                try {
                    jsonWriter.writeNull();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write JSON", e);
                }
            }

            public void whiteSpace(ParseState state, String ws) {
            }

            public void comment(ParseState state, String fullComment, String content) {
            }

            public Object finalValue() {
                return null;
            }

            public void error(ParseState state, String error) {
                System.err.println("\nError at Line " + state.getLineNumber() + ", char " + state.getCharNumber() + "--(Line " + jsonWriter.getLineNumber() + " formatted)");
            }
        };
        try {
            new SAJParser().parse(fileReader, handler);
        } catch (SAJParser.ParseException e) {
            System.out.println(e + "  File may be exported. Trying import.");
            fileReader.close();
            fileWriter[0].close();
            try {
                fileReader = new java.io.InputStreamReader(new prisms.util.ImportStream(new prisms.util.FileSegmentizerInputStream(args[0])));
                fileWriter[0] = new java.io.OutputStreamWriter(new prisms.util.FileSegmentizerOutputStream(outFileName));
                new SAJParser().parse(fileReader, handler);
            } catch (SAJParser.ParseException e2) {
                e.printStackTrace();
                e2.printStackTrace();
            }
        } finally {
            fileReader.close();
            fileWriter[0].flush();
            fileWriter[0].close();
        }
    }
