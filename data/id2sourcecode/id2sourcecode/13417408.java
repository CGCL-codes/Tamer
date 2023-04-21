        public void save() {
            try {
                OutputStream os = null;
                URL urlParserDat = getClass().getResource("parser/parser.dat");
                if (urlParserDat.getProtocol().equals("file")) {
                    os = new FileOutputStream(urlParserDat.getFile());
                } else {
                    URLConnection urlcParserDat = urlParserDat.openConnection();
                    urlcParserDat.setDoInput(false);
                    urlcParserDat.setDoOutput(true);
                    urlcParserDat.setUseCaches(false);
                    urlcParserDat.connect();
                    os = urlcParserDat.getOutputStream();
                }
                DataOutputStream s = new DataOutputStream(new BufferedOutputStream(os));
                s.writeInt(actionTable.length);
                for (int i = 0; i < actionTable.length; i++) {
                    s.writeInt(actionTable[i].length);
                    for (int j = 0; j < actionTable[i].length; j++) {
                        for (int k = 0; k < 3; k++) {
                            s.writeInt(actionTable[i][j][k]);
                        }
                    }
                }
                s.writeInt(gotoTable.length);
                for (int i = 0; i < gotoTable.length; i++) {
                    s.writeInt(gotoTable[i].length);
                    for (int j = 0; j < gotoTable[i].length; j++) {
                        for (int k = 0; k < 2; k++) {
                            s.writeInt(gotoTable[i][j][k]);
                        }
                    }
                }
                s.writeInt(errorMessages.length);
                for (int i = 0; i < errorMessages.length; i++) {
                    s.writeInt(errorMessages[i].length());
                    for (int j = 0; j < errorMessages[i].length(); j++) {
                        s.writeChar(errorMessages[i].charAt(j));
                    }
                }
                s.writeInt(errors.length);
                for (int i = 0; i < errors.length; i++) {
                    s.writeInt(errors[i]);
                }
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to save parser.dat. File may now be corrupted!");
            }
        }
