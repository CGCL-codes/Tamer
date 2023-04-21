    public void process(String filename) {
        StopWatch watch = new StopWatch();
        watch.tic();
        Console.println(Console.TARGET_MAIN, "Saving file " + filename + ".");
        Console.startProgress("Saving file.");
        Session s = mainFrame.getSession();
        VisualONDEXGraph vog = mainFrame.getVisualONDEXGraph();
        VisualGDSBuilder vgb = new VisualGDSBuilder(s, vog, visible, visual);
        try {
            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.bea.xml.stream.XMLOutputFactoryBase");
            BufferedWriter writer;
            if (packed) {
                String zipname = filename + ".zip";
                ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipname));
                File file = new File(filename);
                zip.putNextEntry(new ZipEntry(file.getName()));
                writer = new BufferedWriter(new OutputStreamWriter(zip));
            } else {
                writer = new BufferedWriter(new FileWriter(filename));
            }
            XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(writer);
            XmlBuilder.buildDocument(s, xmlw, vgb.getConcepts(), vgb.getRelations());
            xmlw.flush();
            xmlw.close();
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            Console.printmsg(Console.TARGET_MAIN, ioe);
            Console.stopProgress();
        } catch (XMLStreamException xmlse) {
            Console.printmsg(Console.TARGET_MAIN, xmlse);
            Console.stopProgress();
        } catch (URISyntaxException urise) {
            Console.printmsg(Console.TARGET_MAIN, urise);
            Console.stopProgress();
        }
        double time = watch.toc();
        Console.println(Console.TARGET_MAIN, "Saving finished. - " + time + " s");
        Console.stopProgress();
    }
