    private void publishAll(LWMap map) throws IOException {
        try {
            LWMap saveMap = (LWMap) map.clone();
            Iterator i = Publisher.resourceVector.iterator();
            while (i.hasNext()) {
                Vector vector = (Vector) i.next();
                Resource r = (Resource) (vector.elementAt(1));
                Boolean b = (Boolean) (vector.elementAt(0));
                System.out.println("RESOURCE = " + r.getSpec());
                URL url = new URL(r.getSpec());
                File file = new File(url.getFile());
                if (file.isFile() && b.booleanValue()) {
                    Publisher.resourceTable.getModel().setValueAt("Processing", Publisher.resourceVector.indexOf(vector), Publisher.STATUS_COL);
                    String pid = getDR().ingest(file.getName(), "obj-binary.xml", url.openConnection().getContentType(), file, r.getProperties()).getIdString();
                    Publisher.resourceTable.getModel().setValueAt("Done", Publisher.resourceVector.indexOf(vector), Publisher.STATUS_COL);
                    PublishUtil.replaceResource(saveMap, r, new AssetResource(getDR().getAsset(new tufts.oki.dr.fedora.PID(pid))));
                }
            }
            publishMap(saveMap);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(VUE.getInstance(), "Map cannot be exported " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
