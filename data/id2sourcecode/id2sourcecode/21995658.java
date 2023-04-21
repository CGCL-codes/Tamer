        boolean init(String name) throws IOException {
            String libarch = name + "_" + getOs() + "_" + getCpu();
            String md5jar = libarch + "_md5.jar";
            String md5name = libarch + ".md5";
            trace("md5jar = " + md5jar);
            URL md5url = getLoader().getResource(md5jar);
            if (md5url == null) {
                throw new IOException("Can't get " + md5jar);
            }
            trace("md5url:" + md5url);
            URL jarurl = new URL("jar:" + md5url + "!/" + md5name);
            JarURLConnection jarconn = (JarURLConnection) jarurl.openConnection();
            JarFile jar = jarconn.getJarFile();
            md5entry = jar.getJarEntry(md5name);
            InputStream in = jar.getInputStream(md5entry);
            md5map = loadMd5Hashtable(in);
            return true;
        }
