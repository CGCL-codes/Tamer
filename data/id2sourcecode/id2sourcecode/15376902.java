    public static final Collection<File> unZip(final ZipInputStream in, final File outDir) {
        final Collection<File> result = new ArrayList<File>(4);
        try {
            ZipEntry entry;
            BufferedOutputStream out = null;
            final byte data[] = new byte[10240];
            while (true) {
                out = null;
                try {
                    entry = in.getNextEntry();
                    if (null == entry) break;
                    int count;
                    final File outFile = new File(outDir, entry.getName());
                    outFile.getParentFile().mkdirs();
                    out = new BufferedOutputStream(new FileOutputStream(outFile));
                    while ((count = in.read(data)) != -1) out.write(data, 0, count);
                    out.flush();
                    result.add(outFile);
                } catch (final Exception ioex) {
                    ioex.printStackTrace();
                } finally {
                    closeIO(out);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            closeIO(in);
        }
        return result;
    }
