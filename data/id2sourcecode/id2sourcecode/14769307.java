    public Project readProject(String path) {
        pathGenerator.setBasePath(new File(path).getParent());
        BufferedReader reader = null;
        try {
            URL url = null;
            url = new URL(path);
            String encoding = XMLEncodingUtils.getEncoding(url.openStream());
            InputStream is = url.openStream();
            if (encoding != null) {
                Project proj = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(is, encoding));
                    proj = readProject(reader, true);
                } catch (UnsupportedEncodingException e) {
                    reader = new BufferedReader(new InputStreamReader(is));
                    try {
                        Project p = readProject(reader, false);
                    } catch (UnsupportedEncodingException e1) {
                        JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, e1.getLocalizedMessage()));
                        return null;
                    }
                }
                ProjectExtension.setPath(path);
                return proj;
            } else {
                reader = new BufferedReader(new InputStreamReader(is));
                Project p;
                try {
                    p = readProject(reader, false);
                } catch (UnsupportedEncodingException e) {
                    JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, e.getLocalizedMessage()));
                    return null;
                }
                ProjectExtension.setPath(path);
                return p;
            }
        } catch (MalformedURLException e) {
            File file = new File(path);
            return readProject(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
