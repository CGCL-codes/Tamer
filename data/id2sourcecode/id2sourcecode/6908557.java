    public static int read____I(MJIEnv env, int objref) {
        int fd = env.getIntField(objref, "fd");
        long off = env.getLongField(objref, "off");
        try {
            Object fs = content.get(fd);
            if (fs != null) {
                if (fs instanceof FileInputStream) {
                    FileInputStream fis = (FileInputStream) fs;
                    FileChannel fc = fis.getChannel();
                    fc.position(off);
                    int r = fis.read();
                    env.setLongField(objref, "off", fc.position());
                    return r;
                } else {
                    env.throwException("java.io.IOException", "read attempt on file opened for write access");
                    return -1;
                }
            } else {
                if (env.getIntField(objref, "state") == FD_OPENED) {
                    reopen(env, objref);
                    return read____I(env, objref);
                } else {
                    env.throwException("java.io.IOException", "read attempt on closed file");
                    return -1;
                }
            }
        } catch (ArrayIndexOutOfBoundsException aobx) {
            env.throwException("java.io.IOException", "file not open");
            return -1;
        } catch (IOException iox) {
            env.throwException("java.io.IOException", iox.getMessage());
            return -1;
        }
    }
