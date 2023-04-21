    private void decodeFile(String pdf, String xml, String output) {
        int itemSelectedCount = 0;
        int[] itemSelectedPage = null;
        Vector_Int itemSelectedX1;
        Vector_Int itemSelectedY1;
        Vector_Int itemSelectedWidth;
        Vector_Int itemSelectedHeight;
        int[] values = new int[5];
        File file = new File(xml);
        if (file != null && file.exists()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                Document doc = factory.newDocumentBuilder().parse(file);
                NodeList nodes = doc.getElementsByTagName("TablePositions");
                Element currentElement = (Element) nodes.item(0);
                NodeList catNodes = currentElement.getChildNodes();
                List catValues = new ArrayList();
                int items = catNodes.getLength();
                for (int i = 0; i < items; i++) {
                    Node next = catNodes.item(i);
                    if (next instanceof Element) catValues.add(next);
                }
                int size = catValues.size();
                int i = 0;
                Element next = (Element) catValues.get(i);
                String key = next.getNodeName();
                String value = next.getAttribute("value");
                if (key.endsWith("Count")) {
                    itemSelectedCount = Integer.parseInt(value);
                    itemSelectedPage = new int[itemSelectedCount];
                }
                itemSelectedPage = new int[itemSelectedCount];
                itemSelectedX1 = new Vector_Int(itemSelectedCount);
                itemSelectedY1 = new Vector_Int(itemSelectedCount);
                itemSelectedWidth = new Vector_Int(itemSelectedCount);
                itemSelectedHeight = new Vector_Int(itemSelectedCount);
                int pages = 0;
                for (i = 0; i < size; i++) {
                    next = (Element) catValues.get(i);
                    key = next.getNodeName();
                    value = next.getAttribute("value");
                    if (key.endsWith("page")) {
                        itemSelectedPage[pages] = Integer.parseInt(value);
                        pages++;
                    }
                    if (key.endsWith("x1")) itemSelectedX1.addElement(Integer.parseInt(value));
                    if (key.endsWith("y1")) itemSelectedY1.addElement(Integer.parseInt(value));
                    if (key.endsWith("x2")) itemSelectedWidth.addElement(Integer.parseInt(value));
                    if (key.endsWith("y2")) itemSelectedHeight.addElement(Integer.parseInt(value));
                }
                int[][] openValues = new int[itemSelectedCount][5];
                i = 0;
                int[] x1Array = itemSelectedX1.get();
                int[] x2Array = itemSelectedWidth.get();
                int[] y1Array = itemSelectedY1.get();
                int[] y2Array = itemSelectedHeight.get();
                int pageValue = 0;
                if (showMessages) System.out.println("\nOpening file :" + pdf);
                while (i < itemSelectedCount) {
                    openValues[i][0] = itemSelectedPage[i];
                    openValues[i][1] = x1Array[i];
                    openValues[i][2] = x2Array[i];
                    openValues[i][3] = y1Array[i];
                    openValues[i][4] = y2Array[i];
                    values = openValues[i];
                    i++;
                    String name = "demo";
                    int pointer = pdf.lastIndexOf(separator);
                    if (pointer != -1) name = pdf.substring(pointer + 1, pdf.length() - 4);
                    if (output == "" || output == null) outputDir = user_dir + "tables" + separator + name + separator; else outputDir = output + separator + "tables" + separator + name + separator;
                    File page_path = new File(outputDir + separator);
                    if (page_path.exists() == false) page_path.mkdirs();
                    try {
                        decodePdf = new PdfDecoder(false);
                        decodePdf.setExtractionMode(PdfDecoder.TEXT);
                        decodePdf.init(true);
                        decodePdf.openPdfFile(pdf);
                    } catch (Exception e) {
                        System.err.println("Exception " + e + " in pdf code");
                    }
                    if (!decodePdf.isExtractionAllowed()) {
                        System.out.println("Text extraction not allowed");
                    } else if (decodePdf.isEncrypted() && !decodePdf.isPasswordSupplied()) {
                        System.out.println("Encrypted settings");
                        System.out.println("Please look at SimpleViewer for code sample to handle such files");
                        System.out.println("Or get support/consultancy");
                    } else {
                        try {
                            for (int k = 0; k < 5; k++) {
                                int page = itemSelectedPage[pageValue];
                                pageValue++;
                                decodePdf.decodePage(page);
                                PdfGroupingAlgorithms currentGrouping = decodePdf.getGroupingObject();
                                int x1, y1, x2, y2;
                                if (defX1 == -1) {
                                    k++;
                                    x1 = values[k];
                                    k++;
                                    x2 = values[k];
                                    k++;
                                    y1 = values[k];
                                    k++;
                                    y2 = values[k];
                                } else {
                                    x1 = defX1;
                                    y1 = defY1;
                                    x2 = defX2;
                                    y2 = defY2;
                                }
                                if (showMessages) System.out.println("\nExtracting text from Page " + page + " in rectangle(" + x1 + "," + y1 + " " + x2 + "," + y2 + ")");
                                String ending = ".xml";
                                if (isCSV) {
                                    if (showMessages) System.out.println("Table will be in CSV format");
                                    ending = ".csv";
                                } else {
                                    if (showMessages) System.out.println("Table will be in xml format");
                                }
                                Map tableContent = null;
                                String tableText = null;
                                try {
                                    tableContent = currentGrouping.extractTextAsTable(x1, y1, x2, y2, page, isCSV, false, false, false, 0, false);
                                    tableText = (String) tableContent.get("content");
                                } catch (PdfException e) {
                                    decodePdf.closePdfFile();
                                    System.err.println("Exception " + e.getMessage() + " with table extraction");
                                }
                                if (tableText == null) {
                                    if (showMessages) System.out.println("No text found");
                                } else {
                                    OutputStreamWriter output_stream = new OutputStreamWriter(new FileOutputStream(outputDir + pageValue + ending), "UTF-8");
                                    if (showMessages) System.out.println("Writing to " + outputDir + pageValue + ending);
                                    if (!isCSV) output_stream.write("<xml><BODY>\n\n");
                                    output_stream.write(tableText);
                                    if (!isCSV) output_stream.write("\n</body></xml>");
                                    output_stream.close();
                                }
                                decodePdf.flushObjectValues(false);
                            }
                        } catch (Exception e) {
                            decodePdf.closePdfFile();
                            System.err.println("Exception " + e.getMessage());
                            e.printStackTrace();
                        }
                        decodePdf.flushObjectValues(true);
                        if (showMessages) System.out.println("Text read as table");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            decodePdf.closePdfFile();
        } else {
            System.out.println("File Not Found.");
        }
    }
