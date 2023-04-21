    private void importXml(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        SageXmlReader fileReader = null;
        String fileName = null;
        SageXmlReader pastedStringReader = null;
        String mediaFileID = null;
        boolean impFaves = false;
        boolean impFavesOverwrite = false;
        boolean impAirs = false;
        boolean impTVFiles = false;
        boolean impMFOverwrite = false;
        boolean impMFRename = false;
        int impMFRedate = REDATE_MODE_NONE;
        boolean forceUnviewableChannel = false;
        boolean impShowOverwrite = false;
        String directoryXml = null;
        try {
            MultipartParser mp = new MultipartParser(req, Integer.MAX_VALUE, true, true, charset);
            Part part;
            while ((part = mp.readNextPart()) != null) {
                if (part.isParam()) {
                    ParamPart param = (ParamPart) part;
                    if (param.getName().equals("pastedXml")) {
                        String pastedString = param.getStringValue().trim();
                        if (pastedString.length() > 0) {
                            pastedStringReader = new SageXmlReader(null);
                            pastedStringReader.read(pastedString);
                        }
                    } else if (param.getName().equals("directoryXML")) {
                        directoryXml = param.getStringValue();
                        directoryXml = cleanPath(directoryXml);
                    } else if (param.getName().equals("impFaves")) impFaves = true; else if (param.getName().equals("impFavesOverwrite")) impFavesOverwrite = true; else if (param.getName().equals("impShowOverwrite")) impShowOverwrite = true; else if (param.getName().equals("impAirs")) impAirs = true; else if (param.getName().equals("forceUnviewableChannel")) forceUnviewableChannel = true; else if (param.getName().equals("impTVFiles")) impTVFiles = true; else if (param.getName().equals("impMFOverwrite")) impMFOverwrite = true; else if (param.getName().equals("impMFRedate")) {
                        String val = param.getStringValue().trim();
                        if (val.equalsIgnoreCase("redateFromFile")) impMFRedate = REDATE_MODE_MEDIAFILE; else if (val.equalsIgnoreCase("redateFromAiring")) impMFRedate = REDATE_MODE_AIRING; else impMFRedate = REDATE_MODE_NONE;
                    } else if (param.getName().equals("impMFRename")) impMFRename = param.getStringValue().trim().equalsIgnoreCase("rename"); else if (param.getName().equals("MediaFileID")) mediaFileID = param.getStringValue();
                }
                if (part.isFile() && part.getName().equals("xmlFile")) {
                    FilePart filePart = (FilePart) part;
                    if (filePart.getFileName() != null && filePart.getFilePath().trim().length() > 0) {
                        fileName = filePart.getFilePath().trim();
                        fileReader = new SageXmlReader(null);
                        fileReader.read(filePart.getInputStream());
                    }
                }
            }
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println();
            out.println();
            out.println("<body><pre>");
            out.println("Exception while reading posted data:\n" + e.toString());
            e.printStackTrace(out);
            out.println("</pre>");
            out.close();
            log("Exception while processing servlet", e);
        }
        htmlHeaders(resp);
        PrintWriter out = resp.getWriter();
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>Import Sage XML</title></head>");
            out.println("<body>");
            printTitle(out, "Import Sage XML Info Results:");
            out.println("<div id=\"content\">");
            if (mediaFileID != null) {
                Airing mf;
                try {
                    mf = new Airing(Airing.ID_TYPE_MEDIAFILE, Integer.parseInt(mediaFileID));
                    if (SageApi.booleanApi("IsDVD", new Object[] { mf.sageAiring })) {
                        throw new Exception("Cannot import data for DVDs");
                    }
                } catch (Exception e) {
                    out.println("Invalid MediaFileID: " + e);
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                if (pastedStringReader != null && fileReader != null) {
                    out.println("Error: can only use either pasted XML or uploaded file, not both");
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                if ((directoryXml != null) && (directoryXml.trim().length() > 0)) {
                    out.println("Error: directory import feature cannot be used when attaching to a single media file");
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                SageXmlReader reader = fileReader;
                if (pastedStringReader != null) reader = pastedStringReader;
                if (!reader.isReadOk()) {
                    out.println("Failed to read XML :<pre> ");
                    if (reader.getLastError() != null) out.println(Translate.encode(reader.getLastError()));
                    out.println("</pre>");
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                if (reader.getLastError() != null) {
                    out.println("XML imported with errors:<pre>" + Translate.encode(reader.getLastError()) + "</pre>");
                }
                net.sf.sageplugins.sagexmlinfo.Airing impAir;
                if (reader.getAiringsWithMediaFiles().size() == 1) impAir = (net.sf.sageplugins.sagexmlinfo.Airing) reader.getAiringsWithMediaFiles().get(0); else if (reader.getAiringsWithoutMediaFiles().size() == 1) impAir = (net.sf.sageplugins.sagexmlinfo.Airing) reader.getAiringsWithoutMediaFiles().get(0); else {
                    out.println("Error: XML contains " + reader.getAiringsWithMediaFiles().size() + reader.getAiringsWithoutMediaFiles().size() + " airings - it must contain 1 single airing for this function to work.");
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                out.println("<pre>");
                out.print("Import Show information for file: ");
                File files[] = (File[]) SageApi.Api("GetSegmentFiles", new Object[] { mf.sageAiring });
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        out.print(Translate.encode(files[i].getAbsolutePath()));
                    }
                }
                ImportStatus importStatus = new ImportStatus();
                associateFileToImportedAiring(out, impAir, files[0], mf.sageAiring, importStatus, forceUnviewableChannel, impShowOverwrite, impMFRename, impMFRedate, impMFOverwrite);
                out.println("</pre></div>");
                printMenu(out);
                out.println("</body></html>");
                out.close();
                return;
            } else {
                ArrayList<SageXmlReader> listOfXMLFiles = new ArrayList<SageXmlReader>();
                if ((directoryXml != null) && (directoryXml.trim().length() > 0)) {
                    File directory = new File(directoryXml);
                    String[] listing = directory.list();
                    if (listing == null) {
                        out.println(directory.getPath() + " is not a valid directory on the server");
                        out.println("</div>");
                        printMenu(out);
                        out.println("</body></html>");
                        out.close();
                        return;
                    }
                    for (int fileID = 0; fileID < listing.length; fileID++) {
                        if (listing[fileID] != null && listing[fileID].toLowerCase().endsWith(".xml")) {
                            File file = new File(directoryXml, listing[fileID]);
                            SageXmlReader tempFileReader = new SageXmlReader(null);
                            tempFileReader.read(new FileInputStream(file));
                            listOfXMLFiles.add(tempFileReader);
                        }
                    }
                    DecimalFormat wholeNumberDF = new DecimalFormat("#,##0");
                    out.println("<h3>Processing List of " + wholeNumberDF.format(listOfXMLFiles.size()) + " Files:<br>");
                } else if (pastedStringReader != null) {
                    out.println("<h3>Processing pasted XML:</h3>");
                    listOfXMLFiles.add(pastedStringReader);
                } else if (fileReader != null) {
                    out.println("<h3>Processing imported file: " + Translate.encode(fileName) + "</h3>");
                    listOfXMLFiles.add(fileReader);
                } else {
                    out.println("Error: no input data was entered");
                    out.println("</div>");
                    printMenu(out);
                    out.println("</body></html>");
                    out.close();
                    return;
                }
                for (SageXmlReader reader : listOfXMLFiles) {
                    if (reader.isReadOk()) {
                        if (reader.getLastError() != null) {
                            out.println("XML imported with errors:<pre>" + Translate.encode(reader.getLastError()) + "</pre>");
                        }
                        ProcessImportedData(out, reader, impFaves, impFavesOverwrite, impAirs, forceUnviewableChannel, impShowOverwrite, impTVFiles, impMFOverwrite, impMFRename, impMFRedate);
                    } else {
                        out.println("Failed to read XML file:<pre> ");
                        if (reader.getLastError() != null) out.println(Translate.encode(reader.getLastError()));
                        out.println("</pre>");
                    }
                    out.println("<br/><br/>");
                    out.flush();
                }
            }
            out.println("</div>");
            printMenu(out);
            out.println("</body></html>");
            out.flush();
            out.close();
        } catch (Throwable e) {
            if (!resp.isCommitted()) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html");
            }
            out.println();
            out.println();
            out.println("<body><pre>");
            out.println("Exception while processing servlet:\n" + e.toString());
            e.printStackTrace(out);
            out.println("</pre>");
            out.close();
            log("Exception while processing servlet", e);
        }
    }
