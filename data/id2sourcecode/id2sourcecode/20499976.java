    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("work/tokentest.xml", "r");
        CharSequence markup = Charset.forName("utf-8").decode(file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
        System.out.println(markup = cleanXml(markup));
        System.out.println("------------------------------------------------------------------------------");
        System.out.println(addParagraphTags(markup, InnigCollections.toSet("p", "li", "h1", "h2", "h3", "h4", "ih"), InnigCollections.toSet("a", "b", "i", "br")));
    }
