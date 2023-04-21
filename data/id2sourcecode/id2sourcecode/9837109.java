    @Test
    public void test2() {
        try {
            String resource = "before/test2.json";
            InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
            Reader reader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();
            FileUtilities.copy(reader, writer);
            String before = writer.toString();
            String answer = before;
            Beautifier beautifier = new JsonBeautifier();
            beautifier.setEditMode("json");
            beautifier.setLineSeparator("\n");
            beautifier.setTabWidth(4);
            beautifier.setIndentWidth(4);
            beautifier.setUseSoftTabs(true);
            beautifier.setWrapMargin(80);
            beautifier.setWrapMode("none");
            String after = beautifier.beautify(before);
            assertTrue("json test 2 failed.", answer.equals(after));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
