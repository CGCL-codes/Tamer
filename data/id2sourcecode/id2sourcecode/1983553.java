    private void loadIcons() {
        logger_.entering(new Object[] {});
        Vector files;
        Resource res;
        Properties props;
        try {
            files = getFiles((File) localTree_.getModel().getRoot());
            for (int i = 0; i < files.size(); i++) {
                res = new MemoryResource();
                props = new Properties();
                Resources.unzip(new FileInputStream((File) files.get(i)), res);
                props.load(res.getInputStream(AgentStructure.PROPERTIES));
                if (props.getProperty(AgentStructure.PROP_AGENT_ICON) != null) {
                    ImageIcon icon;
                    InputStream in;
                    ByteArrayOutputStream out;
                    byte[] buf;
                    int n;
                    in = res.getInputStream(props.getProperty("agent.icon"));
                    out = new ByteArrayOutputStream();
                    if (in != null) {
                        buf = new byte[1024];
                        n = 0;
                        while ((n = in.read(buf)) > -1) {
                            out.write(buf);
                        }
                        in.close();
                        out.close();
                        icon = new ImageIcon(out.toByteArray());
                        treeRenderer_.addNewFileIcon((File) files.get(i), icon);
                        localTree_.revalidate();
                        localTree_.repaint();
                    }
                }
            }
        } catch (Exception e) {
            logger_.warning("Loading Agent Icons");
            logger_.caught(e);
        }
        logger_.exiting();
    }
