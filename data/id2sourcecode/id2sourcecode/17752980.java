        static String loadText(String name) {
            URL url = getLocation(name);
            if (url == null) {
                return null;
            }
            StringBuffer sbuf = new StringBuffer(1024);
            char[] cbuf = new char[1024];
            try {
                InputStreamReader isr = new InputStreamReader(url.openStream());
                while (true) {
                    int len = isr.read(cbuf, 0, 1024);
                    if (len < 0) {
                        break;
                    }
                    sbuf.append(cbuf, 0, len);
                }
                isr.close();
            } catch (IOException ioe) {
                System.err.println("Warning - could not open \"" + name + "\"");
                return null;
            }
            return sbuf.toString();
        }
