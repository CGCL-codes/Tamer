        public byte[] loadClassData(String name) {
            URL url;
            byte b[] = null;
            try {
                String s = codeBase.toString();
                if (s.startsWith("jar")) {
                    s = s.substring(s.indexOf("!") + 1, s.length());
                    url = W.getResource(s + name + ".class");
                } else {
                    url = new URL(s + name + ".class");
                }
                URLConnection c = url.openConnection();
                b = new byte[c.getContentLength()];
                InputStream is = c.getInputStream();
                is.read(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return b;
        }
