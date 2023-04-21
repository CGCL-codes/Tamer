    public void run(BufferedReader reader, BufferedWriter writer) throws SijappException {
        this.localDefines.clear();
        for (Enumeration keys = this.defines.keys(); keys.hasMoreElements(); ) {
            String key = new String((String) keys.nextElement());
            String value = new String((String) this.defines.get(key));
            this.localDefines.put(key, value);
        }
        this.reader = reader;
        this.writer = writer;
        this.lineNum = 1;
        this.stop = false;
        this.skip = false;
        try {
            String line;
            while ((line = this.reader.readLine()) != null) {
                Scanner.Token[] tokens = Scanner.scan(line);
                if (tokens.length == 0) {
                    if (!skip) {
                        this.writer.write(line, 0, line.length());
                        this.writer.newLine();
                    }
                } else {
                    this.eval(tokens);
                    if (this.stop) {
                        return;
                    }
                }
                this.lineNum++;
            }
        } catch (SijappException e) {
            throw (new SijappException(this.lineNum + ": " + e.getMessage()));
        } catch (IOException e) {
            throw (new SijappException("An I/O error occured"));
        }
    }
