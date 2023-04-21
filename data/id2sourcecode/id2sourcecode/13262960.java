        public Object makeResource(Object o, Object hashName) throws Throwable {
            if (hashName == null) {
                return null;
            }
            StringBuffer sb = new StringBuffer();
            URL url = (URL) hashName;
            InputStream stream;
            if (url.toExternalForm().startsWith("jar:")) {
                JarFile jf = ((JarURLConnection) url.openConnection()).getJarFile();
                String[] jarURL = url.toExternalForm().split("!");
                JarEntry je = jf.getJarEntry(jarURL[1].substring(1));
                stream = jf.getInputStream(je);
            } else {
                stream = url.openStream();
            }
            if (isBinary(url)) {
                ArrayList<Byte> bytesList = new ArrayList<Byte>();
                byte[] b = new byte[16];
                int readBytes = -1;
                while ((readBytes = stream.read(b)) != -1) {
                    for (int i = 0; i < readBytes; i++) {
                        bytesList.add(b[i]);
                    }
                }
                byte[] bytes = new byte[bytesList.size()];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = bytesList.get(i);
                }
                return bytes;
            } else {
                BufferedReader bis = new BufferedReader(new InputStreamReader(stream));
                byte b;
                while ((b = (byte) bis.read()) != -1) {
                    sb.append(String.valueOf((char) b));
                }
                return sb;
            }
        }
