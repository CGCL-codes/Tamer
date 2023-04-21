    public static void write__I__(MJIEnv env, int objref, int b) {
        int fd = env.getIntField(objref, "fd");
        long off = env.getLongField(objref, "off");
        try {
            Object fs = content.get(fd);
            if (fs != null) {
                if (fs instanceof FileOutputStream) {
                    FileOutputStream fos = (FileOutputStream) fs;
                    FileChannel fc = fos.getChannel();
                    fc.position(off);
                    fos.write(b);
                    env.setLongField(objref, "off", fc.position());
                } else {
                    env.throwException("java.io.IOException", "write attempt on file opened for read access");
                }
            } else {
                if (env.getIntField(objref, "state") == FD_OPENED) {
                    reopen(env, objref);
                    write__I__(env, objref, b);
                } else {
                    env.throwException("java.io.IOException", "write attempt on closed file");
                }
            }
        } catch (ArrayIndexOutOfBoundsException aobx) {
            env.throwException("java.io.IOException", "file not open");
        } catch (IOException iox) {
            env.throwException("java.io.IOException", iox.getMessage());
        }
    }
