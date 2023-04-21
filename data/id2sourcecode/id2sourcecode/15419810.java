    protected void process(BufferedReader reader, BufferedWriter writer, Action action) throws ParsingException, IOException {
        PropertiesActionProcessingAdvisor advisor = getAdvisorFor(action);
        PropertiesFileItemAdvice advice = advisor.onStartProcessing();
        processAdvice(advice, null, writer, action);
        PropertiesFileItem currentItem = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (isBlankLine(line)) {
                writer.append(LINE_SEPARATOR);
            } else {
                if (isComment(line)) {
                    currentItem = readComment(reader, line);
                } else {
                    currentItem = readPropertyMapping(reader, line);
                }
                advice = advisor.process(currentItem);
                processAdvice(advice, currentItem, writer, action);
            }
        }
        advice = advisor.onEndProcessing();
        processAdvice(advice, null, writer, action);
        writer.flush();
    }
