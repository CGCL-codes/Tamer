    public void copy(InputStream input, OutputStream output) throws IOException {
        ContractChecker.mustNotBeNull(input, "input");
        ContractChecker.mustNotBeNull(output, "output");
        byte[] buffer = new byte[this.bufferSize];
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
