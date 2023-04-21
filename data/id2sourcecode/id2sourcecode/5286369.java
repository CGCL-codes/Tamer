    public String highlight(URL url, String printedFilenamePostfix) throws IOException, TranslationException {
        Class<?> classXhtmlRendererFactory;
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean rendered = false;
        try {
            try {
                classXhtmlRendererFactory = Class.forName("com.uwyn.jhighlight.renderer.XhtmlRendererFactory");
                try {
                    Method getRenderer = classXhtmlRendererFactory.getMethod("getRenderer", String.class);
                    int lastSlashPos = url.getPath().lastIndexOf('/');
                    String filename = url.getPath().substring(lastSlashPos + 1);
                    int lastDotPos = filename.lastIndexOf('.');
                    if (lastDotPos >= 0) {
                        String extension = filename.substring(lastDotPos + 1);
                        Object renderer = getRenderer.invoke(classXhtmlRendererFactory, extension);
                        if (renderer != null) {
                            Method highlight = renderer.getClass().getMethod("highlight", String.class, InputStream.class, OutputStream.class, String.class, Boolean.TYPE);
                            String printedFilename = filename;
                            if (printedFilenamePostfix != null) {
                                printedFilename += printedFilenamePostfix;
                            }
                            highlight.invoke(renderer, printedFilename, in, bos, ENCODING, false);
                            rendered = true;
                        }
                    }
                } catch (Exception e) {
                    throw new TranslationException(e);
                }
            } catch (ClassNotFoundException e) {
            }
            if (!rendered) {
                int readLenght;
                byte[] buf = new byte[2048];
                while ((readLenght = in.read(buf)) > 0) {
                    bos.write(buf, 0, readLenght);
                }
            }
        } finally {
            in.close();
        }
        return new String(bos.toByteArray(), ENCODING);
    }
