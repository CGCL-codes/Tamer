    private void writeReadMeTxt() {
        try {
            ArchiveInfo ai = new ArchiveInfo(versionedGenome, null);
            ai.setDataType(ArchiveInfo.DATA_TYPE_VALUE_GRAPH);
            ai.setInitialGraphStyle(Text2USeq.GRAPH_STYLES[graphStyle]);
            ai.setOriginatingDataSource(workingWigFile.toString());
            if (color != null) ai.setInitialColor(color);
            if (description != null) ai.setDescription(description);
            File readme = ai.writeReadMeFile(saveDirectory);
            files2Zip.add(0, readme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
