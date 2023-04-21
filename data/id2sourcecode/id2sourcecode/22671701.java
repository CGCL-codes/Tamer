    private void loadRootFile() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(root.getFile()));
            StringWriter out = new StringWriter();
            int b;
            while ((b = in.read()) != -1) out.write(b);
            out.flush();
            out.close();
            in.close();
            s = out.toString();
        } catch (IOException ie) {
            m_logCat.info("An error occured while loading " + root.getFile(), ie);
        }
    }
