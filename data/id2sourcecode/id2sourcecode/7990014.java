    public void write(OutputStream outp, FileWriter human_readable) throws IOException, DasmError {
        dexFile.writeTo(outp, human_readable, true);
    }
