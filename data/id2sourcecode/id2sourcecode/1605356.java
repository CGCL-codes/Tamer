        public InputSource resolveEntity(String publicId, String systemId) {
            if (publicId != null || systemId == null) return null;
            URL url = null;
            try {
                url = new URL(systemId);
            } catch (Exception e) {
                return null;
            }
            try {
                return new InputSource(url.openStream());
            } catch (Exception e) {
                ;
            }
            if (!url.getProtocol().equals("file")) {
                return null;
            }
            String fileName = url.getFile();
            if (fileName == null) {
                return null;
            }
            try {
                return new InputSource(new FileInputStream(fileName));
            } catch (Exception ee) {
                ;
            }
            if (base != null) {
                try {
                    File newFile = new File(base, fileName);
                    return new InputSource(new FileInputStream(newFile));
                } catch (Exception ee) {
                    ;
                }
            }
            String dtdp = System.getProperty("DTDPATH");
            String ps = System.getProperty("path.separator");
            if (dtdp != null) {
                StringTokenizer tokens = new StringTokenizer(dtdp, ps);
                while (tokens.hasMoreTokens()) {
                    try {
                        File newFile = new File(tokens.nextToken(), fileName);
                        return new InputSource(new FileInputStream(newFile));
                    } catch (Exception ee) {
                        ;
                    }
                }
            }
            String fs = System.getProperty("file.separator");
            String distr = "cwi" + fs + "GraphXML" + fs + "XML";
            String classes = System.getProperty("java.class.path");
            if (classes != null) {
                StringTokenizer tokens = new StringTokenizer(classes, ps);
                while (tokens.hasMoreTokens()) {
                    try {
                        File newDir = new File(tokens.nextToken(), distr);
                        File newFile = new File(newDir, fileName);
                        return new InputSource(new FileInputStream(newFile));
                    } catch (Exception ee) {
                        ;
                    }
                }
            }
            return null;
        }
