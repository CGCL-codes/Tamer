    private void writeResourceContent(IMarkupWriter writer, LineNumberReader reader, int lineNumber) {
        try {
            writer.beginEmpty("br");
            writer.begin("table");
            writer.attribute("class", "location-content");
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                int currentLine = reader.getLineNumber();
                if (currentLine > lineNumber + RANGE) {
                    break;
                }
                if (currentLine < lineNumber - RANGE) {
                    continue;
                }
                writer.begin("tr");
                if (currentLine == lineNumber) {
                    writer.attribute("class", "target-line");
                }
                writer.begin("td");
                writer.attribute("class", "line-number");
                writer.print(currentLine);
                writer.end();
                writer.begin("td");
                writer.print(line);
                writer.end("tr");
                writer.println();
            }
            reader.close();
            reader = null;
        } catch (Exception ex) {
        } finally {
            writer.end("table");
            close(reader);
        }
    }
