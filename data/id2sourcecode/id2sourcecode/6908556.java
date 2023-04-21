    public static void write___3BII__(MJIEnv env, int objref, int bref, int offset, int len) {
        int fd = env.getIntField(objref, "fd");
        long off = env.getLongField(objref, "off");
        try {
            Object fs = content.get(fd);
            if (fs != null) {
                if (fs instanceof FileOutputStream) {
                    FileOutputStream fos = (FileOutputStream) fs;
                    FileChannel fc = fos.getChannel();
                    fc.position(off);
                    byte[] buf = new byte[len];
                    for (int i = 0, j = offset; i < len; i++, j++) {
                        buf[i] = env.getByteArrayElement(bref, j);
                    }
                    fos.write(buf);
                    env.setLongField(objref, "off", fc.position());
                } else {
                    env.throwException("java.io.IOException", "write attempt on file opened for read access");
                }
            } else {
                if (env.getIntField(objref, "state") == FD_OPENED) {
                    reopen(env, objref);
                    write___3BII__(env, objref, bref, offset, len);
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
