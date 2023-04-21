    private static File writeScriptTempfile(final Framework framework, final InputStream stream, final String source, final Reader reader) throws IOException {
        final File scriptfile;
        scriptfile = File.createTempFile("ctl-exec", ".tmp", new File(framework.getProperty("framework.var.dir")));
        final FileWriter writer = new FileWriter(scriptfile);
        if (null != source) {
            writer.write(source);
        } else if (null != reader) {
            ScriptfileUtils.writeReader(reader, writer);
        } else if (null != stream) {
            ScriptfileUtils.writeStream(stream, writer);
        }
        writer.close();
        scriptfile.deleteOnExit();
        return scriptfile;
    }
