    public void save() {
        try {
            File f = new File(getFileName());
            boolean saveFile = true;
            if (f.exists()) {
                int res = Util.confirmDialog("File already exists", "You have already a local file with the same name.\nWould you like to overwrite it?");
                if (res == JOptionPane.CANCEL_OPTION) {
                    saveFile = false;
                }
            }
            if (saveFile) {
                OutputStream fout = new FileOutputStream(getFileName());
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                DOMSource source = new DOMSource(this.toXml());
                StreamResult result = new StreamResult(fout);
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }
