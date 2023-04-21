    public boolean saveAs(String context, String path, InputStream source) {
        System.out.println("Saving to " + context + path);
        String realPath = makeRealPath(context, path);
        System.out.println("Saving as " + realPath);
        if (realPath == null) {
            return false;
        } else {
            File toWrite = new File(realPath);
            File parent = toWrite.getParentFile();
            if (parent != null) {
                if (!parent.exists()) {
                    if (!parent.mkdirs()) {
                        System.out.println("Cannot mkdir " + parent);
                        return false;
                    }
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(toWrite);
                byte[] buf = new byte[1024];
                while (true) {
                    int read = source.read(buf);
                    if (read == -1) {
                        break;
                    }
                    if (read == 0) {
                        continue;
                    }
                    fos.write(buf, 0, read);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (source != null) {
                    try {
                        source.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
