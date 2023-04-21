    @Override
    protected int initialize(Node xmlConfig) throws KETLThreadException {
        int res = super.initialize(xmlConfig);
        if (res != 0) return res;
        for (int i = 0; i < this.mInPorts.length; i++) {
            if (XMLHelper.getAttributeAsBoolean(this.mInPorts[i].getXMLConfig().getAttributes(), "TAB", false)) {
                this.mTabPortIndex = i;
            }
        }
        String filePath = this.getParameterValue(0, "FILEPATH");
        File fn;
        try {
            fn = new File(filePath);
            this.out = new PrintWriter(fn);
            this.xmlOut = new BufferedWriter(this.out);
            this.writeData("<?xml version=\"1.0\"?>\n" + "<?mso-application progid=\"Excel.Sheet\"?>" + "<ss:Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" " + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " + "xmlns:x=\"urn:schemas-microsoft-com:office:excel\"  " + "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"  " + "xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
            this.writeData("<ss:Styles><Style ss:ID=\"Default\" ss:Name=\"Normal\"><Font ss:Size=\"8\"/>" + "</Style><ss:Style ss:ID=\"1\"><Borders><Border ss:Position=\"Bottom\"" + " ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/></Borders></ss:Style>" + "<Style ss:ID=\"s22\"><NumberFormat" + " ss:Format=\"m/d/yy\\ h:mm;@\"/>" + "</Style>\n</ss:Styles>");
            if (this.mTabPortIndex == -1) this.createSheet("Results");
        } catch (IOException e) {
            throw new KETLThreadException(e, this);
        }
        return 0;
    }
