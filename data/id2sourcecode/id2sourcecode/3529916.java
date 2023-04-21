    public void marshal(final Exchange exchange, final Object graph, final OutputStream stream) throws Exception {
        final Message message = exchange.getIn();
        final File file = message.getBody(File.class);
        final String fileName = file.getName();
        final InputStream is = exchange.getContext().getTypeConverter().convertTo(InputStream.class, graph);
        final ZipOutputStream zipOutput = new ZipOutputStream(stream);
        zipOutput.setLevel(compressionLevel);
        try {
            zipOutput.putNextEntry(new ZipEntry(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("Error while adding a new Zip File Entry (File name: '" + fileName + "').", e);
        }
        IOHelper.copy(is, zipOutput);
        zipOutput.close();
    }
