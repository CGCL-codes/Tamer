    public static long skip__J__J(MJIEnv env, int objref, long nBytes) {
        int fd = env.getIntField(objref, "fd");
        long off = env.getLongField(objref, "off");
        try {
            Object fs = content.get(fd);
            if (fs != null) {
                if (fs instanceof FileInputStream) {
                    FileInputStream fis = (FileInputStream) fs;
                    FileChannel fc = fis.getChannel();
                    fc.position(off);
                    long r = fis.skip(nBytes);
                    env.setLongField(objref, "off", fc.position());
                    return r;
                } else {
                    env.throwException("java.io.IOException", "skip attempt on file opened for write access");
                    return -1;
                }
            } else {
                env.throwException("java.io.IOException", "skip attempt on closed file");
                return -1;
            }
        } catch (ArrayIndexOutOfBoundsException aobx) {
            env.throwException("java.io.IOException", "file not open");
            return -1;
        } catch (IOException iox) {
            env.throwException("java.io.IOException", iox.getMessage());
            return -1;
        }
    }
