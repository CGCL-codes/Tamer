    public static void copy(final File aFile, final File aNewFile) throws FileError {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            try {
                in = new FileInputStream(aFile);
                out = new FileOutputStream(aNewFile, false);
                in.getChannel().transferTo(0, in.getChannel().size(), out.getChannel());
            } finally {
                if (in != null) in.close();
                if (out != null) out.close();
            }
        } catch (final FileNotFoundException e) {
            throw new FileError("Can't access file!", e);
        } catch (final IOException e) {
            throw new FileError(e);
        }
    }
