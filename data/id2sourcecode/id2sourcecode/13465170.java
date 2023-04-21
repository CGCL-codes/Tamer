    public String[] getElements() throws IOException {
        {
            Vector<String> v = new Vector<String>();
            PushbackInputStream in = null;
            try {
                URLConnection urlConn = dtdURL.openConnection();
                in = new PushbackInputStream(new BufferedInputStream(urlConn.getInputStream()));
                while (scanForLTBang(in)) {
                    String elementType = getString(in);
                    if (elementType.equals("ELEMENT")) {
                        skipWhiteSpace(in);
                        String elementName = getString(in);
                        v.addElement(elementName);
                    }
                }
                in.close();
                String[] elements = new String[v.size()];
                v.copyInto(elements);
                return elements;
            } catch (Exception exc) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignore) {
                    }
                }
                throw new IOException(Konstanten.ErrorReadingDTD + ": " + exc.toString());
            }
        }
    }
