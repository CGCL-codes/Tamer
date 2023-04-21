    public DeskTopFrame() {
        setTitle("DeskTop XML/ZIP Renderer");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (validation.isSelected()) validationMethod = "ON"; else validationMethod = "OFF";
                if (strict.isSelected()) strictType = "ON"; else strictType = "OFF";
            }
        };
        headerString = "<html><center><h4>A Java Implementation of IMS QTI Version 2: Desktop XML Test</h4><h5>Developed by Graham Smith with support from JISC </h5></center></html>";
        headerPanel = new JPanel();
        header = new JLabel(headerString);
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new BorderLayout());
        selectionPanel.setSize(680, 50);
        border = BorderFactory.createLoweredBevelBorder();
        selectionPanel.setBorder(border);
        XMLFilePanel = new JPanel();
        XMLFilePanel.setLayout(new BorderLayout());
        MainXMLFilePanel = new JPanel();
        fileNameField = new JTextField(40);
        MainXMLFilePanel.add(new JLabel("XML/ZIP File name:"));
        MainXMLFilePanel.add(fileNameField);
        fileOpenButton = new JButton("Browse");
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.addChoosableFileFilter(new XMLFilter());
        chooser.addChoosableFileFilter(new ZIPFilter());
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileOpenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    InFilename = chooser.getSelectedFile().getPath();
                    fileNameField.setText(InFilename);
                    extension = getFileExtension(InFilename).toUpperCase();
                    inFile = new File(InFilename);
                }
            }
        });
        MainXMLFilePanel.add(fileOpenButton);
        IncludeXMLFilePanel = new JPanel();
        IncludeFileNameField = new JTextField(40);
        IncludeXMLFilePanel.add(new JLabel("IncludeXML File name(s) (if any):"));
        IncludeXMLFilePanel.add(IncludeFileNameField);
        IncludeFileOpenButton = new JButton("Browse");
        IncludeInFilename = "";
        IncludeInFilenamelist = "";
        IncludeFileOpenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    IncludeInFilename = chooser.getSelectedFile().getPath();
                    IncludeInFilenamelist = IncludeInFilenamelist + IncludeInFilename + ";";
                    IncludeFileNameField.setText(IncludeInFilenamelist);
                }
            }
        });
        IncludeXMLFilePanel.add(IncludeFileOpenButton);
        XMLFilePanel.add(MainXMLFilePanel, BorderLayout.NORTH);
        XMLFilePanel.add(IncludeXMLFilePanel, BorderLayout.SOUTH);
        selectionPanel.add(XMLFilePanel, BorderLayout.NORTH);
        validationPanel = new JPanel();
        validation = new JCheckBox("Validation");
        validation.addActionListener(listener);
        validationPanel.add(validation);
        strict = new JCheckBox("Strict Type");
        strict.addActionListener(listener);
        validationPanel.add(strict);
        selectionPanel.add(validationPanel, BorderLayout.CENTER);
        Action = new JPanel();
        actionButton = new JButton("Render XML File");
        actionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (extension.equals("XML")) {
                    XMLCaller = new XMLHandlercallerDT2();
                    output = XMLCaller.run(InFilename, IncludeInFilenamelist, validationMethod, strictType);
                } else if (extension.equals("ZIP")) {
                    pkgeHandler = new PkgeHandlerDT();
                    PackageData packagedata = pkgeHandler.run(InFilename, validationMethod, strictType, true);
                    String[] HTMFilePaths = packagedata.getFilePaths();
                    String[] HTMFileHrefs = packagedata.getFileHrefs();
                    for (int i2 = 0; i2 < HTMFilePaths.length; i2++) {
                        output = output + HTMFilePaths[i2] + "\n";
                        output = output + HTMFileHrefs[i2] + "\n";
                        output = output + "\n\n";
                    }
                } else if (inFile.isDirectory()) {
                    multipleHandler = new MultipleZipHandler();
                    output = multipleHandler.run(InFilename, validationMethod, strictType);
                } else output = "INVALID FILENAME" + InFilename + " " + extension;
                outputText.setText(output);
            }
        });
        Action.add(actionButton);
        final JDialog notesDialog = new JDialog(this, "Notes on Usage");
        String notesString = "<html><body><center><h4>Notes on Usage</h4></center>" + "1. The XML\\ZIP filename.<br>" + "The renderer will accept either a single XML file or a zip file with an IMS Content Package. The operation of the renderer depends upon the file extension '*.zip' or '*.xml'." + "<p>" + "2. The Include filename.<br>" + "Enter the name of the XML file(s) for any XInclude elements needed. More files can be added by using the 'Browse' button repeatedly. If adding files to the text box by hand, space the filenames with a semicolon';'." + "</p>" + "<p>" + "3. Validation.<br>" + "If this box is checked, the XML is validated against the Schema or DTD specified in the file. The Schema and DTD files are held locally to avoid the necessity of an internet connection." + "Owing to the necessity to copy these large files, validation may take several seconds.<br>" + "In the case of package submission, note that it is only the XML of the submitted assessment items which is validated: the imsmanifest.xml is not. However malformed imsmanifest.xml will cause an error.<br>" + "</p>" + "<p>" + "5. StrictType Checking.<br>" + "The QTI specifications dictate stringent requirements for variable typing, particularly where expressions and operators are used." + "The rendering and responding engines in this version are very tolerant of type errors, but others are not. Checking this box submits the xml to type checking which other engines might demand. Type errors are notified by appending a 'WARNING' message to the returned HTML, either at the rendering or at the responding stage, depending upon where the error becomes significant." + "</p>" + "<p>" + "6. The Style and Appearance of the Rendered XML.<br>" + "The QTI version 2 specification provides means whereby authors can control the appearance of the rendered XML, through the inclusion of stylesheet references and certain XHTML tags. However, many XML examples, including those in the QTI documentation contain very little, if any, style information. In order to present a reasonably attractive and coherent appearance in such examples, the rendering engine adds some styling by default (the centering of the applets and some of the text, the use of bold face, the presentation of many of the interaction types as tables, and so forth). A future version will include examples which are rendered exactly as specified in the XML, so that authors can better appreciate what stylistic information they may need to include in XML." + "</p>" + "</body></html>";
        JLabel notesPane = new JLabel(notesString);
        notesPane.setSize(595, 495);
        notesPane.setBorder(border);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                notesDialog.setVisible(false);
            }
        });
        JPanel notesContentPane = new JPanel();
        notesContentPane.setLayout(new BorderLayout(50, 50));
        notesContentPane.add(notesPane, BorderLayout.NORTH);
        notesContentPane.add(closeButton, BorderLayout.SOUTH);
        notesDialog.setContentPane(notesContentPane);
        notesDialog.setSize(600, 550);
        notesDialog.setVisible(false);
        notesButton = new JButton("Notes on Usage");
        Action.add(notesButton);
        notesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                notesDialog.setVisible(true);
            }
        });
        selectionPanel.add(Action, BorderLayout.SOUTH);
        add(selectionPanel, BorderLayout.CENTER);
        outputPanel = new JPanel();
        Border titled = BorderFactory.createTitledBorder(border, "Output");
        outputPanel.setBorder(titled);
        outputText = new JTextArea(25, 60);
        outputPane = new JScrollPane(outputText);
        outputPanel.add(outputPane);
        add(outputPanel, BorderLayout.SOUTH);
    }
