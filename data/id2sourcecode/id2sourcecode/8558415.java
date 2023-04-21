    public static final File saveEntry(final ZipFile zf, final ZipEntry target, final String parentDir) throws Exception, IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            if (!target.isDirectory()) {
                final File file = new File(parentDir + File.separator + target.getName());
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (final Exception e) {
                    System.out.println("�����ļ��������" + file.getAbsolutePath());
                    throw e;
                }
                in = zf.getInputStream(target);
                out = new FileOutputStream(file);
                final byte[] bytes = new byte[10240];
                int readed = -1;
                while (true) {
                    readed = in.read(bytes);
                    if (readed == -1) break;
                    out.write(bytes, 0, readed);
                }
                return file;
            }
        } catch (final Exception e) {
            throw e;
        } finally {
            IOUtils.closeIO(in, out);
        }
        return null;
    }
