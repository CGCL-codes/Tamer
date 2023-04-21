    public static CMLDocument createCMLDocument(URL urlx, String type) throws Exception {
        if (type == null) {
            type = getTypeFromFile(urlx.toString());
        }
        if (type == null) throw new CMLException("Unknown file type: " + urlx);
        CMLDocument cmlDoc = null;
        BufferedReader bReader = new BufferedReader(new InputStreamReader(urlx.openStream()));
        String id = makeFileStem(urlx.toString());
        if (false) {
        } else if (type.equalsIgnoreCase(CML) || type.equalsIgnoreCase(XML)) {
            cmlDoc = DOCUMENT_FACTORY.createDocument();
            cmlDoc.parse(urlx);
        } else if (type.equalsIgnoreCase(CIF)) {
            cmlDoc = new CIFImpl();
            ((CIFImpl) cmlDoc).setSU(true);
            cmlDoc.parse(urlx);
        } else if (type.equalsIgnoreCase(MMCIF)) {
            cmlDoc = new MMCIFImpl(urlx);
        } else if (type.equalsIgnoreCase(GAMESS)) {
            cmlDoc = new GAMESSImpl();
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(CASTEP)) {
            cmlDoc = new CASTEPImpl();
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(FORMAT)) {
            if (parseFile == null) throw new CMLException("Format: No PARSEFILE given");
            cmlDoc = new FormatImpl();
            ((FormatImpl) cmlDoc).setParserUrl(new URL(Util.makeAbsoluteURL(parseFile)));
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(TEST)) {
            if (parseFile == null) throw new CMLException("Test: No PARSEFILE given");
            cmlDoc = new PreStyleImpl();
            ((PreStyleImpl) cmlDoc).setParserUrl(new URL(Util.makeAbsoluteURL(parseFile)));
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(G94)) {
            cmlDoc = new G94Impl();
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(JCAMP)) {
            cmlDoc = new JCAMPImpl();
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(JME)) {
            cmlDoc = new JMEImpl(bReader, id);
        } else if (type.equalsIgnoreCase(MDLMOL)) {
            cmlDoc = new MDLMolImpl(bReader, id);
        } else if (type.equalsIgnoreCase(MOL2)) {
            cmlDoc = new MOL2Impl(bReader, id);
        } else if (type.equalsIgnoreCase(MIF)) {
            cmlDoc = new MIFImpl();
            ((MIFImpl) cmlDoc).setSU(true);
            cmlDoc.parse(urlx);
        } else if (type.equalsIgnoreCase(MOPAC)) {
            if (parseFile == null) throw new CMLException("MOPAC: No PARSEFILE given");
            cmlDoc = new MOPACFImpl();
            ((MOPACF) cmlDoc).setParserUrl(new URL(Util.makeAbsoluteURL(parseFile)));
            cmlDoc.parse(urlx);
        } else if (type.equalsIgnoreCase(MOPACIn)) {
            cmlDoc = new MOPACInImpl();
            ((NonCMLDocument) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(PDB)) {
            if (parseFile == null) {
                System.out.println("PDB: No PARSEFILE given; using hardcoded");
                cmlDoc = new PDBImpl();
                ((NonCMLDocument) cmlDoc).parse(urlx);
            } else {
                cmlDoc = new PDBFImpl();
                ((PDBF) cmlDoc).setParserUrl(new URL(Util.makeAbsoluteURL(parseFile)));
                cmlDoc.parse(urlx);
            }
        } else if (type.equalsIgnoreCase(PDBConect)) {
            cmlDoc = new PDBConectImpl();
            ((NonCMLDocument) cmlDoc).createMoleculeElement(PDBConect, id);
            ((NonCMLDocumentImpl) cmlDoc).parse(urlx);
        } else if (type.equalsIgnoreCase(SDF)) {
            cmlDoc = new SDFImpl(bReader, id);
        } else if (type.equalsIgnoreCase(SMILES)) {
            cmlDoc = new SMILESImpl(bReader, id);
        } else if (type.equalsIgnoreCase(SWISS)) {
            if (parseFile == null) throw new CMLException("SWISS: No PARSEFILE given");
            cmlDoc = new SwissFImpl();
            ((SwissF) cmlDoc).setParserUrl(new URL(Util.makeAbsoluteURL(parseFile)));
            cmlDoc.parse(urlx);
        } else if (type.equalsIgnoreCase(VAMP)) {
            cmlDoc = new VAMPImpl(bReader, id);
        } else if (type.equalsIgnoreCase(XYZ)) {
            cmlDoc = new XYZImpl(bReader, id);
        } else {
            System.out.println("Bad/unsupported non-cml type: " + type);
        }
        return cmlDoc;
    }
