    public synchronized mvConstantString CLEARFILE(Program program, mvString status, boolean locked) throws mvException {
        try {
            if (cacheGroup != null) {
                cacheGroup.close();
            }
            int modulo = frame0.getModulo();
            while (modulo % 2 == 0) modulo >>= 1;
            frame0.setModulo(modulo);
            frame0.setRecordCount(0);
            frame0.setInUse(0);
            for (int i = 0; i < modulo; i++) {
                lhGroup g = getGroup(i);
                g.clear();
                g.write();
                g.close();
            }
            lklength = modulo * framesize;
            lk.setLength(lklength);
            ov.setLength(0);
            overflowList = null;
            return RETURN_SUCCESS;
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            throw new mvException(0, ioe);
        }
    }
