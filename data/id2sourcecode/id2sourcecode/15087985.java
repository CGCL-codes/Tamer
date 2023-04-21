    public void parse(Project project, Object source, RootHandler handler) throws BuildException {
        AntXMLContext context = handler.context;
        File buildFile = null;
        URL url = null;
        String buildFileName = null;
        if (source instanceof File) {
            buildFile = (File) source;
        } else if (source instanceof URL) {
            url = (URL) source;
        } else if (source instanceof Resource) {
            FileProvider fp = (FileProvider) ((Resource) source).as(FileProvider.class);
            if (fp != null) {
                buildFile = fp.getFile();
            } else {
                URLProvider up = (URLProvider) ((Resource) source).as(URLProvider.class);
                if (up != null) {
                    url = up.getURL();
                }
            }
        }
        if (buildFile != null) {
            buildFile = FILE_UTILS.normalize(buildFile.getAbsolutePath());
            context.setBuildFile(buildFile);
            buildFileName = buildFile.toString();
        } else if (url != null) {
            try {
                context.setBuildFile((File) null);
                context.setBuildFile(url);
            } catch (java.net.MalformedURLException ex) {
                throw new BuildException(ex);
            }
            buildFileName = url.toString();
        } else {
            throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
        }
        InputStream inputStream = null;
        InputSource inputSource = null;
        ZipFile zf = null;
        try {
            XMLReader parser = JAXPUtils.getNamespaceXMLReader();
            String uri = null;
            if (buildFile != null) {
                uri = FILE_UTILS.toURI(buildFile.getAbsolutePath());
                inputStream = new FileInputStream(buildFile);
            } else {
                uri = url.toString();
                int pling = -1;
                if (uri.startsWith("jar:file") && (pling = uri.indexOf("!")) > -1) {
                    zf = new ZipFile(org.apache.tools.ant.launch.Locator.fromJarURI(uri), "UTF-8");
                    inputStream = zf.getInputStream(zf.getEntry(uri.substring(pling + 1)));
                } else {
                    inputStream = url.openStream();
                }
            }
            inputSource = new InputSource(inputStream);
            if (uri != null) {
                inputSource.setSystemId(uri);
            }
            project.log("parsing buildfile " + buildFileName + " with URI = " + uri + (zf != null ? " from a zip file" : ""), Project.MSG_VERBOSE);
            DefaultHandler hb = handler;
            parser.setContentHandler(hb);
            parser.setEntityResolver(hb);
            parser.setErrorHandler(hb);
            parser.setDTDHandler(hb);
            parser.parse(inputSource);
        } catch (SAXParseException exc) {
            Location location = new Location(exc.getSystemId(), exc.getLineNumber(), exc.getColumnNumber());
            Throwable t = exc.getException();
            if (t instanceof BuildException) {
                BuildException be = (BuildException) t;
                if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                    be.setLocation(location);
                }
                throw be;
            }
            throw new BuildException(exc.getMessage(), t == null ? exc : t, location);
        } catch (SAXException exc) {
            Throwable t = exc.getException();
            if (t instanceof BuildException) {
                throw (BuildException) t;
            }
            throw new BuildException(exc.getMessage(), t == null ? exc : t);
        } catch (FileNotFoundException exc) {
            throw new BuildException(exc);
        } catch (UnsupportedEncodingException exc) {
            throw new BuildException("Encoding of project file " + buildFileName + " is invalid.", exc);
        } catch (IOException exc) {
            throw new BuildException("Error reading project file " + buildFileName + ": " + exc.getMessage(), exc);
        } finally {
            FileUtils.close(inputStream);
            ZipFile.closeQuietly(zf);
        }
    }
