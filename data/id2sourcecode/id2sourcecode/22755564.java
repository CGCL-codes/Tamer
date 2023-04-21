    public SegmentedTranscription readFromURL(String filename) throws SAXException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            XMLReader xmlReader = null;
            SAXParser saxParser = spf.newSAXParser();
            xmlReader = saxParser.getXMLReader();
            SegmentedTranscriptionSaxHandler handler = new SegmentedTranscriptionSaxHandler();
            xmlReader.setContentHandler(handler);
            URL url = new URL(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            xmlReader.parse(new InputSource(url.openStream()));
            SegmentedTranscription transcription = handler.getTranscription();
            xmlReader = null;
            if (!new File(transcription.getHead().getMetaInformation().getReferencedFile()).isAbsolute()) {
                transcription.getHead().getMetaInformation().resolveReferencedFile(filename);
            }
            transcription.resolveLinks(filename);
            return transcription;
        } catch (SAXException se) {
            throw (se);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new SAXException("I/O error:\n" + e.getMessage(), e);
        } catch (ParserConfigurationException pce) {
            throw new SAXException("Parser Configuration error:\n", pce);
        }
    }
