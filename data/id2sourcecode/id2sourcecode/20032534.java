    public void buildFromXMLNode(Element node) throws InkMLComplianceException {
        super.buildFromXMLNode(node);
        if (node.hasAttribute(INKML_ATTR_TYPE)) {
            this.type = Type.getValue(loadAttribute(node, INKML_ATTR_TYPE, null));
        }
        if (node.hasAttribute(INKML_ATTR_CONTINUATION)) {
            this.continuation = Continuation.getValue(node.getAttribute(INKML_ATTR_CONTINUATION));
        }
        if (this.continuation == Continuation.END || this.continuation == Continuation.MIDDLE) {
            this.priorRef = loadAttribute(node, INKML_ATTR_PRIORREF, null);
        }
        this.brushRef = loadAttribute(node, INKML_ATTR_BRUSHREF, null);
        if (node.hasAttribute(INKML_ATTR_DURATION)) {
            this.duration = Double.parseDouble(node.getAttribute(INKML_ATTR_DURATION));
        }
        if (node.hasAttribute(INKML_ATTR_TIMEOFFSET)) {
            this.duration = Double.parseDouble(node.getAttribute(INKML_ATTR_TIMEOFFSET));
        }
        final List<Formatter> formatter = new ArrayList<Formatter>();
        for (InkChannel c : this.getSourceFormat()) {
            formatter.add(c.formatterFactory());
        }
        final String input = node.getTextContent().trim();
        int total = 0;
        char[] chars = input.toCharArray();
        boolean onemore = false;
        for (int i = 0; i < chars.length; i++) {
            switch(chars[i]) {
                case ',':
                    total++;
                    onemore = false;
                    break;
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    onemore = true;
            }
        }
        if (onemore) total++;
        addPoints(new PointConstructionBlock(total) {

            public void addPoints() throws InkMLComplianceException {
                Scanner stringScanner = new Scanner(input);
                Pattern pattern = Pattern.compile(",|F|T|\\*|\\?|[\"'!]?-?(\\.[0-9]+|[0-9]+\\.[0-9]+|[0-9]+)");
                int i = 0;
                while (true) {
                    String result = stringScanner.findWithinHorizon(pattern, input.length());
                    if (result == null) {
                        break;
                    }
                    if (result.equals(",") || i >= formatter.size()) {
                        next();
                        i = 0;
                        continue;
                    }
                    set(formatter.get(i).getChannel().getName(), formatter.get(i).consume(result));
                    i++;
                }
            }
        });
    }
