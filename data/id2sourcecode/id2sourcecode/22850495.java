    public static void main(String args[]) throws IOException {
        BasicConfigurator.configure();
        lgr.debug("This is a logging message!");
        System.out.println(new Date() + " Starting.");
        System.out.println(new Date() + " Reading.");
        File td = new File("tmp0");
        td.mkdir();
        File children[] = td.listFiles();
        for (int i = 0; i < children.length; i++) {
            children[i].delete();
        }
        td.delete();
        td.mkdir();
        File td2 = new File("output");
        td2.mkdir();
        createHtmlHeader("output/cvreport2.html");
        CSVReader reader = new CSVReader(new FileReader("cvreport.data"));
        int numberLines = 0;
        String nextLine[];
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[0].equals("*")) {
                System.out.println(new Date() + " COMMENT: " + nextLine[1]);
            } else {
                numberLines++;
                createCash(nextLine[0], nextLine[2], nextLine[6]);
                if (nextLine[6].equals("")) {
                    System.out.println(nextLine[0]);
                }
                if (nextLine[1].equals("OPENACCT")) {
                    actionOpenacct(nextLine[2], nextLine[3], nextLine[6], nextLine[0]);
                }
                if (nextLine[1].equals("EXP")) {
                    actionExp(nextLine[0], nextLine[2], nextLine[3], nextLine[4], nextLine[6]);
                }
                if (nextLine[1].equals("BUY")) {
                    actionBuy(nextLine[0], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
                    actionBuy(nextLine[0], "allsecs3", nextLine[3], nextLine[4], nextLine[5]);
                }
                if (nextLine[1].equals("SELL")) {
                    actionSell(nextLine[0], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
                    actionSell(nextLine[0], "allsecs3", nextLine[3], nextLine[4], nextLine[5]);
                }
                if (nextLine[1].equals("DIV")) {
                    actionDiv(nextLine[0], nextLine[3], nextLine[6]);
                }
                if (nextLine[1].equals("INC")) {
                    actionInc(nextLine[0], nextLine[3], nextLine[6]);
                }
                if (nextLine[1].equals("UPD")) {
                    actionUpd(nextLine[0], nextLine[2], nextLine[3], nextLine[5]);
                    actionUpd(nextLine[0], "allsecs3", nextLine[3], nextLine[5]);
                }
                if (nextLine[1].equals("XFR")) {
                    actionXfr(nextLine[0], nextLine[3], nextLine[6]);
                }
            }
        }
        System.out.println(new Date() + " Reading finished.");
        System.out.println(new Date() + " Data points: " + numberLines);
        System.out.println(new Date() + " Creating Networth Graph");
        totalCash();
        stockReportINV();
        stockReportRESP();
        stockReportRRSP();
        stockReportALL();
        uniqExp();
        uniqInc();
        System.out.println(new Date() + " Total Dividends.");
        uniqDiv();
        totalDiv();
        monthlyHeader();
        monthlyDisplayValues("INVESTMENTS");
        monthlyDisplayValues("RESP");
        monthlyDisplayValues("RRSP");
        monthlyInc();
        monthlyDiv();
        monthlyExp();
        monthlyFooter();
        uniqSec();
        piePort("allsecs2");
        System.out.println(new Date() + " Finished: " + numberLines);
        createHtmlFooter("output/cvreport2.html");
        html_pdf();
    }
