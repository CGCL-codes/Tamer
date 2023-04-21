    public void packageClass(String zwtClass, File outputDir) throws PackagerException {
        LOGGER.info("Packaging " + zwtClass);
        try {
            this.serializeVisitor = new ZECMA262SerializerVisitor();
            this.serializeVisitor.setFormat(false);
            this.serializeVisitor.setIncludeComments(false);
            if (this.obfuscate) {
                this.serializeVisitor.setOptimize(true);
            } else {
                this.serializeVisitor.setOptimize(false);
            }
            outputDir.mkdirs();
            File mergeFile = new File(outputDir, zwtClass + ".js");
            this.data = new PrintWriter(mergeFile);
            this.packageClass(zwtClass);
            this.data.flush();
            this.data.close();
        } catch (IOException e) {
            throw new PackagerException("Error writing output.", e);
        }
        if (this.splitOutput) {
            File pathOutputDir = new File(outputDir, zwtClass);
            pathOutputDir.mkdir();
            for (Entry<String, StringWriter> entry : this.zwtPathData.entrySet()) {
                FileWriter pathOutputWriter;
                try {
                    String keyDigest = this.digest(entry.getValue().toString());
                    File pathMergeFile = new File(pathOutputDir, keyDigest + ".js");
                    pathOutputWriter = new FileWriter(pathMergeFile);
                } catch (NoSuchAlgorithmException e) {
                    throw new PackagerException("Error writing path output.", e);
                } catch (IOException e) {
                    throw new PackagerException("Error writing path output.", e);
                }
                try {
                    pathOutputWriter.write(entry.getValue().toString());
                } catch (IOException e) {
                    throw new PackagerException("Error writing path output.", e);
                } finally {
                    try {
                        pathOutputWriter.flush();
                        pathOutputWriter.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
