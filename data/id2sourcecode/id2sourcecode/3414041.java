    private void saveDataTree(String file) {
        Map dataToSerialize = new LinkedHashMap();
        Map dataFromTree = theDataTreePanel.getDataList();
        StringBuilder builder = new StringBuilder();
        Iterator itr = dataFromTree.keySet().iterator();
        while (itr.hasNext()) {
            Datum currentDatum = (Datum) itr.next();
            String currentNode = (String) dataFromTree.get(currentDatum);
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(currentDatum);
                oos.close();
                if (out.toByteArray().length > 0) {
                    dataToSerialize.put(currentDatum, currentNode);
                }
            } catch (Exception e) {
                myLogger.warn("saveDataTree: object: " + currentDatum.getName() + " type: " + currentDatum.getData().getClass().getName() + " does not serialize.");
                myLogger.warn("saveDataTree: message: " + e.getMessage());
                if (builder.length() == 0) {
                    builder.append("Due to error, these data sets could not\n");
                    builder.append("included in the saved file...");
                }
                builder.append("Data set: ");
                builder.append(currentDatum.getName());
                builder.append(" type: ");
                builder.append(currentDatum.getData().getClass().getName());
                builder.append("\n");
            }
        }
        try {
            File theFile = new File(Utils.addSuffixIfNeeded(file, ".zip"));
            FileOutputStream fos = new FileOutputStream(theFile);
            java.util.zip.ZipOutputStream zos = new ZipOutputStream(fos);
            Map data = theDataTreePanel.getDataList();
            ZipEntry thisEntry = new ZipEntry("DATA");
            zos.putNextEntry(thisEntry);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(dataToSerialize);
            oos.flush();
            zos.closeEntry();
            fos.close();
            sendMessage("Data saved to " + theFile.getAbsolutePath());
        } catch (Exception ee) {
            sendErrorMessage("Data could not be saved: " + ee);
            ee.printStackTrace();
        }
        if (builder.length() != 0) {
            JOptionPane.showMessageDialog(this, builder.toString(), "These data sets not saved...", JOptionPane.INFORMATION_MESSAGE);
        }
    }
