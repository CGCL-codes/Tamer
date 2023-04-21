    public void writeInstruments(String filename) {
        ObjectOutputStream out = null;
        ZipOutputStream zipout = null;
        try {
            zipout = new ZipOutputStream(new FileOutputStream(filename));
            zipout.setLevel(0);
            for (String i : shapes.keySet()) {
                FileShape shape = shapes.importShape(i);
                zipout.putNextEntry(new ZipEntry(shape.getFilename()));
                shape.saveShapeToOutputStream(zipout);
                zipout.closeEntry();
            }
            zipout.putNextEntry(new ZipEntry(INSTRUMENT_CONTAINER_NAME));
            out = new ObjectOutputStream(zipout);
            out.writeObject(instrumentContainer);
            out.flush();
            zipout.closeEntry();
            zipout.finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipout != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
