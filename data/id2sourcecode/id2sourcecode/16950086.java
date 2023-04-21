    private void writeReadMeTxt() {
        try {
            ArchiveInfo ai = new ArchiveInfo(versionedGenome, null);
            ai.setDataType(ArchiveInfo.DATA_TYPE_VALUE_GRAPH);
            ai.setInitialGraphStyle(ArchiveInfo.GRAPH_STYLE_VALUE_STAIRSTEP);
            String source = null;
            if (samFiles.length == 1) source = samFiles[0].toString(); else source = samFiles[0].getParent();
            ai.setOriginatingDataSource(source);
            ai.setInitialColor("#0066FF");
            File readme = ai.writeReadMeFile(tempDirectory);
            files2Zip.add(0, readme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
