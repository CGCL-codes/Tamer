    @Override
    public void run() {
        byte buffer[] = new byte[512];
        int read;
        if (unbuffered) {
            try {
                int avail;
                while ((read = in.read(buffer, 0, 1)) != -1) {
                    out.write(buffer, 0, read);
                    while ((avail = in.available()) > 0) {
                        read = in.read(buffer, 0, avail);
                        out.write(buffer, 0, read);
                    }
                }
            } catch (IOException e) {
                except = e;
            }
        } else {
            BufferedInputStream buffered = new BufferedInputStream(in);
            try {
                while ((read = buffered.read(buffer)) > 0) {
                    out.write(buffer, 0, read);
                    out.flush();
                }
            } catch (IOException e) {
                except = e;
            }
        }
        if (closeInStream) {
            try {
                in.close();
            } catch (IOException e) {
                if (except == null) {
                    except = e;
                }
            }
        }
        if (closeOutStream) {
            try {
                out.close();
            } catch (IOException e) {
                if (except == null) {
                    except = e;
                }
            }
        }
    }
