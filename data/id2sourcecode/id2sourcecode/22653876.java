    protected void doRealize() throws MediaException {
        int isolateId = MIDletSuiteUtils.getIsolateId();
        if (this.source == null) {
            hNative = nInit(isolateId, pID, Manager.TONE_DEVICE_LOCATOR, Manager.TONE_DEVICE_LOCATOR, -1);
        } else {
            hNative = nInit(isolateId, pID, DefaultConfiguration.MIME_AUDIO_TONE, source.getLocator(), -1);
        }
        if (hNative == 0) {
            throw new MediaException("Unable to realize tone player");
        }
        if (stream == null) {
            return;
        }
        int chunksize = 128;
        byte[] tmpseqs = new byte[chunksize];
        byte[] seqs = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(chunksize);
        try {
            int read;
            while ((read = stream.read(tmpseqs, 0, chunksize)) != -1) {
                baos.write(tmpseqs, 0, read);
            }
            seqs = baos.toByteArray();
            baos.close();
            tmpseqs = null;
            System.gc();
        } catch (IOException ex) {
            throw new MediaException("unable to realize: fail to read from source");
        }
        try {
            this.setSequence(seqs);
        } catch (Exception e) {
            throw new MediaException("unable to realize: " + e.getMessage());
        }
    }
