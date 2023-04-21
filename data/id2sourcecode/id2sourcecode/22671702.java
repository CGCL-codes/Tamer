    private void saveRootFile() {
        String path = root.getFile().toString();
        root.getFile().delete();
        File res = new File(path);
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(res));
            StringReader in = new StringReader(s);
            int b;
            while ((b = in.read()) != -1) out.write(b);
            out.flush();
            out.close();
            in.close();
            root.setFile(res);
        } catch (IOException ie2) {
            m_logCat.info("An error occured while saving " + path, ie2);
        }
    }
