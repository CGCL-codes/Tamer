        public static synchronized JarFile get(URL url, boolean useCaches) throws IOException {
            JarFile jf;
            if (useCaches) {
                jf = (JarFile) cache.get(url);
                if (jf != null) return jf;
            }
            if ("file".equals(url.getProtocol())) {
                String fn = url.getFile();
                fn = gnu.java.net.protocol.file.Connection.unquote(fn);
                File f = new File(fn);
                jf = new JarFile(f, true, ZipFile.OPEN_READ);
            } else {
                URLConnection urlconn = url.openConnection();
                InputStream is = urlconn.getInputStream();
                byte[] buf = new byte[READBUFSIZE];
                File f = File.createTempFile("cache", "jar");
                FileOutputStream fos = new FileOutputStream(f);
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                jf = new JarFile(f, true, ZipFile.OPEN_READ | ZipFile.OPEN_DELETE);
            }
            if (useCaches) cache.put(url, jf);
            return jf;
        }
