    public static void main(String[] args) throws IOException {
        long max = 0, mean = 0, min = Long.MAX_VALUE;
        ElementDescriptor elementDescriptor = new ElementDescriptor("div", "class", "question");
        ElementDescriptor elementDescriptor2 = new ElementDescriptor("div", "class", "reponse");
        ElementDescriptor discriminantDescriptor = new ElementDescriptor("span", "class", "highlight");
        elementDescriptor2.getParents().add(elementDescriptor);
        List<ElementDescriptor> l = new ArrayList<ElementDescriptor>();
        l.add(elementDescriptor);
        l.add(elementDescriptor2);
        for (int i = 0; i < 1; i++) {
            SAXFAQHTMLParsingHandler parsingHandler = new SAXFAQHTMLParsingHandler(l, discriminantDescriptor, true);
            SAXFAQParser parser = new SAXFAQParser(parsingHandler);
            File f = new File("exemple1.txt");
            FileInputStream fileInputStream = new FileInputStream(f);
            long start = System.currentTimeMillis();
            parser.parse(fileInputStream, "iso-8859-1");
            List questions = parsingHandler.getListOfElements();
            System.out.println(questions.size() + " results found");
            for (int j = 0; j < questions.size(); j++) {
                System.out.println("result : " + j);
                System.out.println(questions.get(j).toString());
            }
            long stop = System.currentTimeMillis();
            long value = stop - start;
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
            mean = (max + min) / 2;
            fileInputStream.close();
        }
        System.out.println("Max : " + max + " ms");
        System.out.println("Mean : " + mean + " ms");
        System.out.println("Min : " + min + " ms");
    }
