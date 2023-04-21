    public static BufferedReader initReader(String[] args) {
        InputStream is = null;
        if (args.length >= 1) {
            Vector<InputStream> inputs = new Vector<InputStream>();
            for (String arg : args) {
                if (arg.startsWith("http://")) {
                    try {
                        URL url = new URL(arg);
                        is = url.openStream();
                        if (arg.contains(".gz")) {
                            is = new GZIPInputStream(is);
                        }
                        inputs.add(is);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    File file = new File(arg);
                    if (file.exists() && file.canRead()) {
                        InputStream instr = null;
                        try {
                            instr = new FileInputStream(file);
                            if (arg.contains(".gz")) {
                                instr = new GZIPInputStream(instr);
                            }
                            inputs.add(instr);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (args.length > 1) {
                is = new SequenceInputStream(inputs.elements());
            } else {
                is = inputs.firstElement();
            }
        } else {
            is = new BufferedInputStream(System.in);
            is.mark(10);
            byte magic[] = new byte[2];
            try {
                is.read(magic);
                is.reset();
            } catch (IOException e) {
                throw new IllegalArgumentException("Fatal error: Exception reading from stdin");
            }
            int magicNum = ((int) magic[0] & 0xff) | ((magic[1] << 8) & 0xff00);
            if (GZIPInputStream.GZIP_MAGIC == magicNum) {
                try {
                    is = new GZIPInputStream(is);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Fatal error: Initializing GZipInputStream for stdin");
                }
            }
        }
        in = new BufferedReader(new InputStreamReader(is));
        return (in);
    }
