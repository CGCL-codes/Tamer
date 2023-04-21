    public void search(AthenBarcode barcode) {
        if (barcode == null) {
            throw new NullPointerException("barcode was null");
        }
        String page = AthenClientUtils.getServicesPath() + "/search";
        switch(barcode.getType()) {
            case UPC:
                page += "?upc=" + barcode;
                break;
            case ISBN13:
                page += "?isbn13=" + barcode;
                break;
            case ISBN10:
                page += "?isbn10=" + barcode;
                break;
        }
        m_xml = "";
        m_html = "";
        URL url;
        InputStreamReader input = null;
        StringBuffer xml = new StringBuffer(0);
        char[] buffer = new char[1024];
        StringReader sourceReader = null;
        StringWriter resultWriter = null;
        StreamSource source = null;
        StreamResult result = null;
        try {
            url = new URL(page);
            input = new InputStreamReader(url.openStream());
            while (true) {
                int read = input.read(buffer);
                if (read == -1) {
                    break;
                }
                xml.append(buffer, 0, read);
            }
            input.close();
            input = null;
            m_xml = xml.toString();
            if (m_t != null) {
                sourceReader = new StringReader(m_xml);
                resultWriter = new StringWriter();
                source = new StreamSource(sourceReader);
                result = new StreamResult(resultWriter);
                m_t.transform(source, result);
                m_html = resultWriter.toString();
                sourceReader.close();
                resultWriter.close();
                sourceReader = null;
                resultWriter = null;
            }
            display();
        } catch (Exception ex) {
            ex.printStackTrace();
            m_information.setContentType("text/plain");
            m_information.setText(ex.toString() + "\n\n" + m_xml);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (sourceReader != null) {
                    sourceReader.close();
                }
                if (resultWriter != null) {
                    resultWriter.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                m_information.setContentType("text/plain");
                m_information.setText(ex.toString() + "\n\n" + m_xml);
            }
        }
    }
