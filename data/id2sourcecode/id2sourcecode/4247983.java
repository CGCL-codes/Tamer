    private ByteArrayInputStream createInputStream(final File projectFile) throws IOException {
        final boolean hasXmlTag;
        final BufferedReader reader = new BufferedReader(new FileReader(projectFile));
        try {
            final String line = reader.readLine();
            if (null == line) {
                throw new IOException("Error parsing project file " + projectFile + ": it is empty.");
            }
            hasXmlTag = line.startsWith("<?xml");
        } finally {
            reader.close();
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        if (!hasXmlTag) {
            buffer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
            buffer.write(("<!DOCTYPE OBJECT SYSTEM \"" + EdaVersionedProjectHandler.Version.VERSION0.getDtd() + "\">").getBytes());
        }
        final FileInputStream fileInput = new FileInputStream(projectFile);
        try {
            final byte[] tmp = new byte[4096];
            do {
                final int bytesRead = fileInput.read(tmp);
                if (-1 == bytesRead) {
                    break;
                }
                buffer.write(tmp, 0, bytesRead);
            } while (true);
        } finally {
            fileInput.close();
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer.toByteArray());
        return inputStream;
    }
