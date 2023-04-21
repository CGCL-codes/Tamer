        public void write(OutputStream out) throws IOException {
            StringBuffer sb = new StringBuffer();
            sb.append(ResourceUtils.CLASSPATH_URL_PREFIX);
            sb.append(location);
            URL url = ResourceUtils.getURL(sb.toString());
            copy(url.openStream(), out, 8096);
        }
