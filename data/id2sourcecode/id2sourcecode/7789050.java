    private static void loadScriptFromResource(final String name, final Writer writer) {
        final InputStream is = JsUnitRhinoRunner.class.getResourceAsStream("/" + name);
        if (is != null) {
            try {
                final Reader reader = new InputStreamReader(is, "ISO-8859-1");
                copy(reader, writer);
            } catch (final UnsupportedEncodingException e) {
                throw new InternalError("Missing standard character set ISO-8859-1");
            } catch (final IOException e) {
                throw new InternalError("Cannot load resource " + name);
            } finally {
                close(is);
            }
        } else {
            throw new InternalError("Cannot find resource " + name);
        }
    }
