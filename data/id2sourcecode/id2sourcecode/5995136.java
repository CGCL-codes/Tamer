    public void play() {
        FileParameter filename = (FileParameter) options.elementAt(1);
        YesNoParameter ask = (YesNoParameter) options.elementAt(0);
        YesNoParameter setSavefile = (YesNoParameter) options.elementAt(2);
        YesNoParameter useStandard = (YesNoParameter) options.elementAt(3);
        if (ask.getAnswer() && new File(filename.getSelectedFile()).equals(frame.saveFile) && (!frame.isChanged())) return;
        boolean flag = true;
        File file = null;
        if (useStandard.getAnswer()) if (frame.saveFile != null) {
            file = frame.saveFile;
            flag = false;
        }
        if ((ask.getAnswer() && filename.getSelectedFile().equals("") && flag) || (!ask.getAnswer())) {
            int[] filters = { CDX };
            file = frame.getFileDialog().showDialog(frame, lang.get("filetitlecdoxsave"), lang.get("fileapprovesavebutton"), null, filters);
        } else if (flag) file = new File(filename.getSelectedFile());
        if (file == null) return;
        filename.setSelectedFile(file.getPath());
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            org.w3c.dom.Element e = doc.createElementNS("http://cdox.sf.net/schema/fileformat", "cdox");
            e.setAttribute("xmlns", "http://cdox.sf.net/schema/fileformat");
            e.setAttribute("version", "1");
            doc.appendChild(e);
            if (frame.getData() != null) {
                org.w3c.dom.Element d = (org.w3c.dom.Element) e.appendChild(doc.createElementNS("http://cdox.sf.net/schema/fileformat", "description"));
                frame.getData().storeData(d);
            }
            Cover[] c = frame.getEditPane().getCovers();
            for (int i = 0; i < c.length; i++) c[i].saveMyself(doc, out);
            frame.getEditPane().saveMyPreviewSelf(out);
            out.putNextEntry(new ZipEntry("documents.cdx"));
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(out);
            t.transform(src, res);
            out.closeEntry();
            out.finish();
            out.close();
        } catch (IOException ioe) {
            CDoxFrame.handleError(ioe, false);
        } catch (ParserConfigurationException pce) {
            CDoxFrame.handleError(pce, true);
        } catch (TransformerConfigurationException tce) {
            CDoxFrame.handleError(tce, true);
        } catch (TransformerException te) {
            CDoxFrame.handleError(te, true);
        }
        frame.setSave(false);
        if (setSavefile.getAnswer()) {
            frame.saveFile = file;
            frame.setMyTitle(file.getName());
        }
    }
