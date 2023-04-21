    public synchronized void build(BufferedReader reader, BufferedWriter writer) throws IOException {
        this.writer = writer;
        this.echoHead();
        {
            String line = reader.readLine();
            if (line == null) throw new EOFException();
            String[] fields = StringUtils.split(line, '\t');
            if (fields.length < 7) throw new EOFException();
            this.next = fields[0];
            this.knownSingular = fields[1];
            this.knownPlural = fields[2];
            this.changeSingular = fields[3];
            this.changePlural = fields[4];
            this.fixSingular = fields[5];
            this.fixPlural = fields[6];
        }
        int lineNumber = 1;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            lineNumber++;
            String[] fields = StringUtils.split(line, '\t');
            String code = fields[0].trim();
            line = fields.length > 1 ? fields[1].trim() : "";
            if (code.equals("version")) {
                openVersion(line);
            } else if (code.equals("improvement")) {
                this.currentList = this.changes;
            } else if (code.equals("fix")) {
                this.currentList = this.fixes;
            } else if (code.equals("known")) {
                this.currentList = this.known;
            } else if (code.length() == 0) {
                if (line.length() != 0) {
                    if (this.currentList == null) {
                        System.err.println("Line " + lineNumber + " is not preceded by a kind");
                    } else {
                        this.currentList.add(line);
                    }
                }
            } else {
                wrongLine(lineNumber);
            }
            if (fields.length > 2) {
                wrongLine(lineNumber);
            }
        }
        this.closeVersion();
        this.echoBottom();
        this.writer.flush();
    }
