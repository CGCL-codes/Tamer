   * Load one blueprint from inputstream
   */
    private Blueprint loadBlueprint(InputStream in, String tag, String name, boolean readOnly) throws IOException {
        StringWriter html = new StringWriter(512);
        readwrite(new InputStreamReader(in, "UTF8"), html);
        in.close();
