    public void replace(Reader reader, Writer writer, VariableResolver resolver) throws IOException {
        BufferedReader breader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        BufferedWriter bwriter = writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
        try {
            boolean firstLine = true;
            for (String line; (line = breader.readLine()) != null; ) {
                if (firstLine) firstLine = false; else bwriter.newLine();
                bwriter.write(replace(line, resolver));
            }
        } finally {
            try {
                breader.close();
            } finally {
                bwriter.close();
            }
        }
    }
