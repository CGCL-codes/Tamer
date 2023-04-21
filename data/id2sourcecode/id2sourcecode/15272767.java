    private void writeReadMeTxt(File sourceFile) {
        try {
            ArchiveInfo ai = new ArchiveInfo(versionedGenome, null);
            ai.setDataType(ArchiveInfo.DATA_TYPE_VALUE_GRAPH);
            ai.setInitialGraphStyle(GRAPH_STYLES[graphStyle]);
            ai.setOriginatingDataSource(sourceFile.toString());
            if (color != null) ai.setInitialColor(color);
            if (description != null) ai.setDescription(description);
            File readme = ai.writeReadMeFile(workingBinarySaveDirectory);
            files2Zip.add(0, readme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
