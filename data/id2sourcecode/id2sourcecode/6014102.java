    public World(URL url, InputOutputClient io, boolean noSerCliente) throws IOException {
        InputStream is = null;
        if (url.toString().toLowerCase().endsWith(".xml")) {
            if (!url.toString().startsWith("jar:") && !url.toString().startsWith("zip:")) {
                worlddir = url.toString().substring(0, url.toString().lastIndexOf("/") + 1);
                worldurl = new URL(worlddir);
            } else {
                worlddir = url.toString().substring(0, url.toString().lastIndexOf("/") + 1);
                worldurl = new URL(worlddir);
            }
            is = url.openStream();
        } else {
            worlddir = "jar:" + url.toString() + "!/";
            worldurl = new URL(worlddir);
            URLClassLoader ucl = new URLClassLoader(new URL[] { url }, this.getClass().getClassLoader());
            is = ucl.getResourceAsStream("world.xml");
            if (is == null) throw new IOException("Resource world.xml could not be found in URL " + url);
            this.setResourceJarFile(url);
        }
        try {
            loadWorldFromStream(is, io, noSerCliente);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            throw new IOException(pce);
        } catch (SAXException se) {
            se.printStackTrace();
            throw new IOException(se);
        } catch (IOException ioe) {
            throw (ioe);
        } catch (XMLtoWorldException x2we) {
            write(UIMessages.getInstance().getMessage("load.world.xml.exception") + " " + x2we.getMessage());
        }
    }
