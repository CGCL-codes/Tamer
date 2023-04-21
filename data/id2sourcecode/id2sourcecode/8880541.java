    public synchronized ConstantString CLEARFILE(Program program, MaverickString status, boolean locked) throws MaverickException {
        try {
            if (cacheGroup != null) {
                cacheGroup.close();
            }
            int modulo = frame0.getModulo();
            while (modulo % 2 == 0) modulo >>= 1;
            byte[] data = { Group.GROUP_TERMINATOR };
            int inuse = data.length * modulo;
            int size = 0;
            int count = 0;
            frame0.setFirstPrimaryFrame(0, 0, modulo, framesize, inuse, (byte) split_threshold, size, count, data, 0, data.length);
            lhFrame frame = getFrame();
            frame.setPrimaryFrame(0, 0, modulo, framesize, data, 0, data.length);
            lk.seek(0);
            frame0.write(lk);
            for (int i = 1; i < modulo; i++) {
                frame.write(lk);
            }
            lklength = modulo * framesize;
            lk.setLength(lklength);
            ov.setLength(0);
            overflowList = null;
            return ConstantString.RETURN_SUCCESS;
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
    }
