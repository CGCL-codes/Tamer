    public void updateOutputStream(OutputStream diOs, boolean c14n11) throws CanonicalizationException, IOException {
        if (diOs == outputStream) {
            return;
        }
        if (bytes != null) {
            diOs.write(bytes);
        } else if (inputOctetStreamProxy == null) {
            CanonicalizerBase c14nizer = null;
            if (c14n11) {
                c14nizer = new Canonicalizer11_OmitComments();
            } else {
                c14nizer = new Canonicalizer20010315OmitComments();
            }
            c14nizer.setWriter(diOs);
            c14nizer.engineCanonicalize(this);
        } else {
            if (inputOctetStreamProxy.markSupported()) {
                inputOctetStreamProxy.reset();
            }
            byte[] buffer = new byte[4 * 1024];
            int bytesread = 0;
            while ((bytesread = inputOctetStreamProxy.read(buffer)) != -1) {
                diOs.write(buffer, 0, bytesread);
            }
        }
    }
