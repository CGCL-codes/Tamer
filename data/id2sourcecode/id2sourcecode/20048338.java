    public void process(String filename) {
        StopWatch watch = new StopWatch();
        watch.tic();
        Console.println(Console.TARGET_MAIN, "Saving file " + filename + ".");
        Console.startProgress("Saving file.");
        Session s = mainFrame.getSession();
        VisualONDEXGraph vog = mainFrame.getVisualONDEXGraph();
        VisualGDSBuilder vgb = new VisualGDSBuilder(s, vog, visible, visual);
        try {
            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
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
            XMLOutputFactory2 xmlof = (XMLOutputFactory2) XMLOutputFactory2.newInstance();
            xmlof.configureForSpeed();
            XMLStreamWriter2 xmlw = xmlof.createXMLStreamWriter(writer, "UTF-8");
            backend.exchange.xml.export.ondex.Export builder = new backend.exchange.xml.export.ondex.Export(s);
            builder.buildDocument(xmlw, new MemoryONDEXIterator<AbstractConcept>(vgb.getConcepts()), new MemoryONDEXIterator<AbstractRelation>(vgb.getRelations()));
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
