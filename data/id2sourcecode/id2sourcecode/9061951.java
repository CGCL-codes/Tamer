    public void copy(Reader input, Writer output) throws IOException {
        ContractChecker.mustNotBeNull(input, "input");
        ContractChecker.mustNotBeNull(output, "output");
        char[] buffer = new char[this.bufferSize];
        int read = -1;
        try {
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
        } finally {
            try {
                input.close();
            } finally {
                output.close();
            }
        }
    }
