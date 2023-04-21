    private void saveFlow() {
        boolean overwrite = true;
        forLoad = false;
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FisFileFilter(OLD_suff, OLD_desc));
        chooser.addChoosableFileFilter(new FisFileFilter(XML_suff, XML_desc));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setCurrentDirectory(new File(last_xml_dir));
        chooser.setAccessory(buildPreview());
        chooser.addPropertyChangeListener(this);
        if (last_xml_file != null) {
            File tmp_file = new File(last_xml_file);
            chooser.setSelectedFile(tmp_file);
            setupPreview(tmp_file);
        }
        int r = chooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String t = chooser.getSelectedFile().getAbsolutePath();
            setXMLDirectory(t);
            if (!chooser.getFileFilter().getDescription().equals(OLD_desc)) {
                if (!t.endsWith(XML_suff)) t = t + XML_suff;
                if ((new File(t)).exists()) {
                    File file = new File(t);
                    if (file != null) {
                        Document document;
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        try {
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            builder.setErrorHandler(new XmlErrorHandler());
                            builder.setEntityResolver(new XmlEntityResolver());
                            document = builder.parse(file);
                            Element element = document.getDocumentElement();
                            if (!(getClass().getName().equals(element.getAttribute("name").trim()))) {
                                JOptionPane.showMessageDialog(this, "This XML file is not from GlobalDesktop", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                        }
                        document = null;
                        factory = null;
                        int ans = JOptionPane.showConfirmDialog(this, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                        if (ans == JOptionPane.YES_OPTION) {
                            overwrite = true;
                            try {
                                copyFile(t, t + "~");
                            } catch (IOException e) {
                                int err_ans = JOptionPane.showConfirmDialog(this, "Can't create backup file " + t + "~" + "\nDo you wish to continue anyways?", "Warning", JOptionPane.YES_NO_OPTION);
                                if (err_ans != JOptionPane.YES_OPTION) {
                                    overwrite = false;
                                }
                            }
                        } else {
                            overwrite = false;
                        }
                    }
                }
                if (overwrite) {
                    XmlSerializationFactory sf = new XmlSerializationFactory(this, t);
                    try {
                        sf.saveDesktop();
                        sf = null;
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(this, "Error saving file!\n" + ex.getMessage());
                        if (System.getProperty("DEBUG") != null) ex.printStackTrace();
                        return;
                    }
                    Dialogs.ShowMessageDialog(this, "Desktop saved successfully", "Done");
                    last_xml_file = t;
                    String[] title_strings = getTitleElements(getTitle());
                    if (title_strings[1] == null) {
                        setTitle(title_strings[0] + " : " + t);
                    } else {
                        setTitle(title_strings[0] + " @ " + title_strings[1] + " : " + t);
                    }
                }
            } else {
                if (!t.endsWith(OLD_suff)) t = t + OLD_suff;
                if ((new File(t)).exists()) {
                    int ans = JOptionPane.showConfirmDialog(this, "The file already exists, overwrite?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) overwrite = true; else overwrite = false;
                }
                if (overwrite) {
                    Vector bases = new Vector(flowPosVec.size());
                    Vector titles = new Vector(flowPosVec.size());
                    for (int i = 0; i < flowPosVec.size(); i++) {
                        bases.addElement(((FlowPosition) flowPosVec.elementAt(i)).desktopBase.base);
                        titles.addElement(((FlowPosition) flowPosVec.elementAt(i)).title);
                    }
                    SerializationFactory sf = new SerializationFactory(t);
                    try {
                        sf.saveDesktop(bases, titles);
                        sf = null;
                    } catch (Exception ex) {
                        Dialogs.ShowErrorDialog(this, "Error saving file!");
                        return;
                    }
                    Dialogs.ShowMessageDialog(this, "Desktop saved successfully", "Done");
                }
            }
        }
    }
