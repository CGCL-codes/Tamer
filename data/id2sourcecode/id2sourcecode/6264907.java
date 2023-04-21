        public void copy(final POIFSFileSystem poiFs, final POIFSDocumentPath path, final String name, final DocumentInputStream stream) throws IOException {
            final DirectoryEntry de = getPath(poiFs, path);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int c;
            while ((c = stream.read()) != -1) out.write(c);
            stream.close();
            out.close();
            final InputStream in = new ByteArrayInputStream(out.toByteArray());
            de.createDocument(name, in);
        }
