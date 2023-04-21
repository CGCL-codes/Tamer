    public void execute() throws MojoExecutionException {
        if (in.exists()) {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Transforming file: " + in.getPath());
            }
            InputStream styleIn;
            try {
                if (style.startsWith("classpath:")) {
                    style = style.substring(10);
                    URLClassLoader loader = getProjectClassLoader();
                    URL url = loader.getResource(style);
                    styleIn = url.openStream();
                } else {
                    File file = new File(style);
                    styleIn = new FileInputStream(file);
                }
            } catch (IOException x) {
                throw new MojoExecutionException("Error reading stylesheet '" + style + "'.", x);
            }
            try {
                TransformerFactory tf = TransformerFactory.newInstance();
                StreamSource styleSrc = new StreamSource(styleIn);
                Transformer t = tf.newTransformer(styleSrc);
                Iterator<String> names = params.keySet().iterator();
                while (names.hasNext()) {
                    String name = names.next();
                    String value = params.get(name);
                    t.setParameter(name, value);
                }
                StreamSource inSrc = new StreamSource(in);
                if (!out.getParentFile().exists()) out.getParentFile().mkdirs();
                FileOutputStream outStream = new FileOutputStream(out);
                StreamResult outRes = new StreamResult(outStream);
                t.transform(inSrc, outRes);
                outStream.close();
            } catch (Exception x) {
                throw new MojoExecutionException("Error transforming '" + in.getPath() + "'.", x);
            }
        } else {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Do nothing because input file '" + in.getPath() + "' doesn't exist.");
            }
        }
    }
