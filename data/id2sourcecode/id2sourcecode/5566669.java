    public ConvertTT2BinaryNioBuffer(String outFile, String encodingTextFile) throws IOException {
        fileVocF = outFile + ".vocf";
        fileVocE = outFile + ".voce";
        vE = new Vocabulary();
        vF = new Vocabulary();
        vLoaded = new File(fileVocF).exists() && new File(fileVocE).exists();
        if (vLoaded) {
            TranslationTableToolsExtra.readVocab(fileVocE, encodingTextFile, vE);
            TranslationTableToolsExtra.readVocab(fileVocF, encodingTextFile, vF);
        }
        outE = new FileOutputStream(outFile + ".data");
        fcE = outE.getChannel();
        outIdx = new FileOutputStream(outFile + ".idx");
        fcIdx = outIdx.getChannel();
    }
