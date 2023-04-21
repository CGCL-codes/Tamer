    @Test
    public void test1() {
        try {
            String resource = "before/test1.json";
            InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
            Reader reader = new InputStreamReader(is);
            StringWriter writer = new StringWriter();
            FileUtilities.copy(reader, writer);
            String before = writer.toString();
            Beautifier beautifier = new JsonBeautifier();
            beautifier.setEditMode("json");
            beautifier.setLineSeparator("\n");
            beautifier.setTabWidth(4);
            beautifier.setIndentWidth(4);
            beautifier.setUseSoftTabs(true);
            beautifier.setWrapMargin(80);
            beautifier.setWrapMode("none");
            String after = beautifier.beautify(before);
            assertTrue("json test 1 failed. Expected\n" + before + "\n\n but was\n" + after, true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail(e.getMessage());
        }
    }
