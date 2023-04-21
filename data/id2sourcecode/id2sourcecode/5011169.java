    private void writeModel(MModel mModel, String name, ZipOutputStream zip, Writer writer) throws IOException {
        StringWriter stringWriter = new StringWriter();
        XMIWriter xmiWriter = new XMIWriter(mModel, stringWriter);
        try {
            xmiWriter.gen();
        } catch (IncompleteXMIException e) {
            e.printStackTrace();
            throw new IOException("Writing XMI failed: " + e.getMessage());
        }
        zip.putNextEntry(new ZipEntry(name));
        writer.write(stringWriter.toString());
        writer.flush();
        zip.closeEntry();
    }
