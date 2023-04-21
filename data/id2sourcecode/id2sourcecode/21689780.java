    private void saveIndex() throws IOException {
        if (idxFc == null) {
            idxFos = new FileOutputStream(new File(dir, INDEX_FILE), false);
            idxFc = idxFos.getChannel();
        }
        byte[] idx = new byte[14];
        Utils.codeInt(size, idx, 0);
        codePointer(head, idx, 4);
        codePointer(tail, idx, 9);
        idxFc.truncate(0);
        idxFos.write(idx);
        idxFos.flush();
    }
