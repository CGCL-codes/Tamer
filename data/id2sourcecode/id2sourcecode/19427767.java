    private void writeIndexInfo(BufferedWriter bw) throws Exception {
        bw.write("<info>\n");
        IndexInfo indexInfo = new IndexInfo(reader, indexPath);
        bw.write(" <indexPath>" + Util.xmlEscape(indexPath) + "</indexPath>\n");
        bw.write(" <fields count='" + indexInfo.getFieldNames().size() + "'>\n");
        for (String fname : indexInfo.getFieldNames()) {
            bw.write("  <field name='" + Util.xmlEscape(fname) + "'/>\n");
        }
        bw.write(" </fields>\n");
        bw.write(" <numDocs>" + reader.numDocs() + "</numDocs>\n");
        bw.write(" <maxDoc>" + reader.maxDoc() + "</maxDoc>\n");
        bw.write(" <numDeletedDocs>" + reader.numDeletedDocs() + "</numDeletedDocs>\n");
        bw.write(" <numTerms>" + indexInfo.getNumTerms() + "</numTerms>\n");
        bw.write(" <hasDeletions>" + reader.hasDeletions() + "</hasDeletions>\n");
        bw.write(" <optimized>" + reader.isOptimized() + "</optimized>\n");
        bw.write(" <lastModified>" + indexInfo.getLastModified() + "</lastModified>\n");
        bw.write(" <indexVersion>" + indexInfo.getVersion() + "</indexVersion>\n");
        bw.write(" <indexFormat>\n");
        bw.write("  <id>" + indexInfo.getIndexFormat() + "</id>\n");
        bw.write("  <genericName>" + indexInfo.getFormatDetails().genericName + "</genericName>\n");
        bw.write("  <capabilities>" + indexInfo.getFormatDetails().capabilities + "</capabilities>\n");
        bw.write(" </indexFormat>\n");
        bw.write(" <directoryImpl>" + indexInfo.getDirImpl() + "</directoryImpl>\n");
        Directory dir = indexInfo.getDirectory();
        if (dir != null) {
            bw.write(" <files count='" + dir.listAll().length + "'>\n");
            String[] files = dir.listAll();
            Arrays.sort(files);
            for (String file : files) {
                bw.write("  <file name='" + file + "' size='" + dir.fileLength(file) + "' func='" + IndexGate.getFileFunction(file) + "'/>\n");
            }
            bw.write(" </files>\n");
            bw.write(" <commits count='" + IndexReader.listCommits(dir).size() + "'>\n");
            for (Object o : IndexReader.listCommits(dir)) {
                IndexCommit ic = (IndexCommit) o;
                bw.write("  <commit tstamp='" + new Date(ic.getTimestamp()).toString() + "' segment='" + ic.getSegmentsFileName() + "' optimized='" + ic.isOptimized() + "' deleted='" + ic.isDeleted() + "' files='" + ic.getFileNames().size() + "'>\n");
                for (Object p : ic.getFileNames()) {
                    bw.write("   <file name='" + p.toString() + "'/>\n");
                }
                Map<String, String> userData = ic.getUserData();
                if (userData != null && userData.size() > 0) {
                    bw.write("   <userData size='" + userData.size() + "'>" + userData.toString() + "</userData>\n");
                }
                bw.write("  </commit>\n");
            }
            bw.write(" </commits>\n");
        }
        TermStats[] topTerms = indexInfo.getTopTerms();
        if (topTerms != null) {
            bw.write(" <topTerms count='" + topTerms.length + "'>\n");
            for (TermStats ts : topTerms) {
                String val = null;
                if (decode) {
                    Decoder d = decoders.get(ts.field);
                    if (d != null) {
                        val = d.decodeTerm(ts.field, ts.termtext);
                    }
                }
                if (!decode || val == null) {
                    val = ts.termtext.utf8ToString();
                }
                val = Util.xmlEscape(val);
                bw.write("  <term field='" + Util.xmlEscape(ts.field) + "' text='" + val + "' docFreq='" + ts.docFreq + "'/>\n");
            }
        }
        bw.write(" </topTerms>\n");
        bw.write("</info>\n");
    }
