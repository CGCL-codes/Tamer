    public void writeLine(IGStaticValue aValue, SourceLocation aLocation) throws ZamiaException {
        switch(getDir()) {
            case NONE:
                throw new ZamiaException("Attempt to access a closed file.");
            case IN:
                String fileName = getFileName(aLocation);
                throw new ZamiaException("Attempt to write to or flush file \"" + fileName + "\" which is opened only for reading.");
        }
        File file = getFile(aLocation);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(aValue.toString());
            writer.newLine();
        } catch (FileNotFoundException e) {
            throw new ZamiaException("File to write to is not found: " + file.getAbsolutePath(), aLocation);
        } catch (IOException e) {
            throw new ZamiaException("Error while writing to file " + file.getAbsolutePath() + ":\n" + e.getMessage(), aLocation);
        } finally {
            close(writer);
        }
    }
