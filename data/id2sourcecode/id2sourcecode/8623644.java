        @Override
        public void run() {
            URL url = null;
            try {
                String s = System.getProperty("com.raelity.jvi.motd");
                if (s != null) System.err.println("DEBUG MOTD: " + s);
                if (s == null) s = "http://jvi.sourceforge.net/motd";
                URI uri = new URI(s);
                url = uri.toURL();
            } catch (MalformedURLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            if (url == null) return;
            try {
                URLConnection c = url.openConnection();
                InputStream in = c.getInputStream();
                byte b[] = new byte[BUF_LEN];
                ByteBuffer bb = ByteBuffer.wrap(b);
                StringBuilder sb = new StringBuilder();
                Charset cset = Charset.forName("US-ASCII");
                int n;
                int total = 0;
                while ((n = in.read(b)) > 0 && total < MAX_MSG) {
                    bb.position(0);
                    bb.limit(n);
                    CharBuffer cb = cset.decode(bb);
                    sb.append(cb.toString());
                    total += n;
                }
                in.close();
                if (!outputOnly) motd = new Motd(sb.toString()); else new Motd(sb.toString()).output();
            } catch (IOException ex) {
            }
        }
