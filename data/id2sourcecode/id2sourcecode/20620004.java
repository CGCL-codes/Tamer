    public IGStaticValue openFile(IGStaticValue aFileName, IGObject.OIDir aDir, IGType aFileOpenStatusType, SourceLocation aLocation) throws ZamiaException {
        if (getDir() != IGObject.OIDir.NONE) {
            return aFileOpenStatusType.findEnumLiteral("STATUS_ERROR");
        }
        File file = createFile(aFileName);
        FileReader reader = null;
        FileWriter writer = null;
        FileOutputStream fos = null;
        switch(aDir) {
            case IN:
                if (!file.exists()) {
                    return aFileOpenStatusType.findEnumLiteral("NAME_ERROR");
                }
                try {
                    reader = new FileReader(file);
                } catch (FileNotFoundException e) {
                    return aFileOpenStatusType.findEnumLiteral("MODE_ERROR");
                } finally {
                    close(reader);
                }
                break;
            case OUT:
            case APPEND:
                try {
                    writer = new FileWriter(file);
                    fos = new FileOutputStream(file);
                    fos.getChannel().lock().release();
                } catch (OverlappingFileLockException e) {
                    return aFileOpenStatusType.findEnumLiteral("NAME_ERROR");
                } catch (IOException e) {
                    return aFileOpenStatusType.findEnumLiteral("NAME_ERROR");
                } finally {
                    close(writer);
                    close(fos);
                }
                if (aDir == IGObject.OIDir.APPEND) {
                    try {
                        writer = new FileWriter(file, true);
                    } catch (IOException e) {
                        return aFileOpenStatusType.findEnumLiteral("MODE_ERROR");
                    } finally {
                        close(writer);
                    }
                }
                break;
        }
        setDir(aDir);
        setValue(aFileName, aLocation);
        return aFileOpenStatusType.findEnumLiteral("OPEN_OK");
    }
