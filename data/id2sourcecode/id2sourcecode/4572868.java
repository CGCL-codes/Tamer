        private Object readResolve() {
            if (words == null) words = new HashMap<String, Double>();
            if (multipliers == null) multipliers = new HashMap<String, Multiplier>();
            if (conjunctions == null) conjunctions = new HashMap<String, Boolean>();
            if (decimalSymbol == null) decimalSymbol = ".";
            if (digitGroupingSymbol == null) digitGroupingSymbol = ",";
            if (imports == null) {
                imports = new HashMap<URL, String>();
            } else {
                for (Map.Entry<URL, String> entry : imports.entrySet()) {
                    URL url = entry.getKey();
                    String encoding = entry.getValue();
                    XStream xstream = getXStream(url, getClass().getClassLoader());
                    BomStrippingInputStreamReader in = null;
                    try {
                        in = new BomStrippingInputStreamReader(url.openStream(), encoding);
                        Config c = (Config) xstream.fromXML(in);
                        words.putAll(c.words);
                        multipliers.putAll(c.multipliers);
                        conjunctions.putAll(c.conjunctions);
                    } catch (IOException ioe) {
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            return this;
        }
