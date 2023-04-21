    protected void loadList() {
        if ((System.currentTimeMillis() - this.timeStamp) >= 43200000) {
            this.timeStamp = System.currentTimeMillis();
            int letter;
            StringBuffer scan = new StringBuffer(markStop.length() + 1);
            StringBuffer dataBuffer = new StringBuffer();
            boolean store = false;
            try {
                InputStream in = this.url.openStream();
                while ((letter = in.read()) != -1) {
                    char cLetter = (char) letter;
                    if (store) {
                        if (cLetter == '\r') {
                            String spamer = dataBuffer.toString().trim();
                            if (!spamer.equals("")) {
                                this.spamers.add(spamer);
                                dataBuffer.delete(0, dataBuffer.length());
                            }
                        } else {
                            dataBuffer.append(cLetter);
                        }
                    }
                    scan.append(cLetter);
                    if (scan.length() > markStop.length()) {
                        scan.delete(0, 1);
                    }
                    String scanStr = scan.toString();
                    if (scanStr.endsWith(markStart)) {
                        store = true;
                    }
                    if (scanStr.equals(markStop)) {
                        break;
                    }
                }
            } catch (java.io.IOException ioe) {
                System.err.println("Error in '" + this.getClass().getName() + "' " + ioe.toString());
            }
        }
    }
