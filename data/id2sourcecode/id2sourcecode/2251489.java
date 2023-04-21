    public void run() {
        this.writer.writeLine(this.reader.readLine());
        String[] line = this.reader.readLine();
        while (line != null) {
            for (PostProcessorI processor : this.processors) {
                line = processor.processEvent(line);
            }
            this.writer.writeLine(line);
            line = this.reader.readLine();
        }
        this.writer.finish();
    }
