    String[] getFilesAndRanges(String spid, DatumRange tr) throws IOException {
        TimeParser tp = TimeParser.create("$Y$m$dT$H$M$SZ");
        String tstart = tp.format(tr.min(), tr.min());
        String tstop = tp.format(tr.max(), tr.max());
        InputStream ins = null;
        try {
            URL url = new URL(String.format("http://cdaweb.gsfc.nasa.gov/WS/cdasr/1/dataviews/sp_phys/datasets/%s/data/%s,%s/ALL-VARIABLES?format=cdf", spid, tstart, tstop));
            URLConnection urlc;
            urlc = url.openConnection();
            urlc.setConnectTimeout(300);
            ins = urlc.getInputStream();
            InputSource source = new InputSource(ins);
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;
            doc = builder.parse(source);
            XPath xp = XPathFactory.newInstance().newXPath();
            NodeList set = (NodeList) xp.evaluate("/DataResult/FileDescription", doc.getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
            String[] result = new String[set.getLength()];
            for (int i = 0; i < set.getLength(); i++) {
                Node item = set.item(i);
                result[i] = xp.evaluate("Name/text()", item) + "|" + xp.evaluate("StartTime/text()", item) + "|" + xp.evaluate("EndTime/text()", item);
            }
            return result;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw ex;
        } catch (SAXException ex) {
            Logger.getLogger(CDAWebDB.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CDAWebDB.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(CDAWebDB.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            if (ins != null) ins.close();
        }
    }
