    private void _copyResources(String argSource, String argTarget) throws IOException {
        File outRef = _getFileRef(argTarget);
        OutputStream out = new FileOutputStream(outRef);
        InputStream in = this.getClass().getResourceAsStream(argSource);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        out.close();
    }
