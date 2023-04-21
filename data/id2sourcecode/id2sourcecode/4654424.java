    public void spliceMethod(String modelFileNameIn, String methodOwnerPath, String methodName, String methodText, String modelFileNameOut) throws SpliceException {
        InputStream xmiIn = null;
        ByteArrayOutputStream xmiOut = null;
        OutputStream modelOut = null;
        ByteArrayInputStream splicedXmiIn = null;
        try {
            if (modelFileNameIn.endsWith(".zargo") || modelFileNameIn.endsWith(".zuml")) {
                xmiIn = IOUtil.extractFromZipFile(new File(modelFileNameIn), ".xmi");
            } else if (modelFileNameIn.endsWith(".zip")) {
                xmiIn = IOUtil.extractFromZipFile(new File(modelFileNameIn), ".xml");
            } else {
                xmiIn = getFileInputStream(modelFileNameIn);
            }
            xmiOut = new ByteArrayOutputStream();
            spliceMethod(methodOwnerPath, methodName, methodText, xmiIn, xmiOut);
            xmiIn.close();
            xmiOut.close();
            splicedXmiIn = new ByteArrayInputStream(xmiOut.toByteArray());
            if (modelFileNameOut.endsWith(".zargo") || modelFileNameOut.endsWith(".zuml")) {
                replaceXmiInPoseidonFile(modelFileNameOut, splicedXmiIn);
            } else {
                modelOut = getFileOutputStream(modelFileNameOut);
                int c;
                while ((c = splicedXmiIn.read()) != -1) {
                    modelOut.write(c);
                }
                modelOut.close();
            }
            splicedXmiIn.close();
        } catch (IOException e) {
            throw new SpliceException("IO Error splicing method:", e);
        } finally {
            if (xmiIn != null) {
                try {
                    xmiIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            }
            if (xmiOut != null) {
                try {
                    xmiOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            }
            if (modelOut != null) {
                try {
                    xmiOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            }
            if (splicedXmiIn != null) {
                try {
                    splicedXmiIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            }
        }
    }
