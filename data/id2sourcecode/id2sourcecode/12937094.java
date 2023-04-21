    private void parse() throws IOException {
        if (inputFile == null || outputFile == null || outputPath == null) {
            showParametersExit();
        }
        StringReader sr;
        String str;
        printHeader();
        System.out.println("Input file: " + inputFile);
        System.out.println("Output file: " + outputFile);
        System.out.println("Output path: " + outputPath);
        System.out.println("Annotate functions on: " + includeFunc);
        System.out.println("Agreement on: " + agreement);
        System.out.println("Grammar check on: " + markGrammarError);
        System.out.println("Merge labels: " + mergeLabels);
        sr = new StringReader("empty");
        TagEncoder tagEncdr = new TagEncoder(sr);
        readwrite(tagEncdr, inputFile, outputPath + "/" + "tagencoder.out");
        Preprocess preprocess = new Preprocess(sr);
        readwrite(preprocess, outputPath + "/" + "tagencoder.out", outputPath + "/" + "preprocess.out");
        Phrase_FOREIGN frgn = new Phrase_FOREIGN(sr);
        readwrite(frgn, outputPath + "/" + "preprocess.out", outputPath + "/" + "phrase_FOREIGN.out");
        Phrase_MWE mwe = new Phrase_MWE(sr);
        readwrite(mwe, outputPath + "/" + "phrase_FOREIGN.out", outputPath + "/" + "phrase_MWE.out");
        Phrase_MWEP1 mwep1 = new Phrase_MWEP1(sr);
        readwrite(mwep1, outputPath + "/" + "phrase_MWE.out", outputPath + "/" + "phrase_MWEP1.out");
        Phrase_MWEP2 mwep2 = new Phrase_MWEP2(sr);
        readwrite(mwep2, outputPath + "/" + "phrase_MWEP1.out", outputPath + "/" + "phrase_MWEP2.out");
        Phrase_AdvP advp = new Phrase_AdvP(sr);
        readwrite(advp, outputPath + "/" + "phrase_MWEP2.out", outputPath + "/" + "phrase_AdvP.out");
        Phrase_AP ap = new Phrase_AP(sr);
        readwrite(ap, outputPath + "/" + "phrase_AdvP.out", outputPath + "/" + "phrase_AP.out");
        Case_AP cap = new Case_AP(sr);
        readwrite(cap, outputPath + "/" + "phrase_AP.out", outputPath + "/" + "case_AP.out");
        Phrase_APs aps = new Phrase_APs(sr);
        readwrite(aps, outputPath + "/" + "case_AP.out", outputPath + "/" + "phrase_APs.out");
        Phrase_NP np = new Phrase_NP(sr);
        if (agreement) {
            np.set_doAgreementCheck(true);
        }
        if (markGrammarError) {
            np.set_markGrammarError(true);
        }
        readwrite(np, outputPath + "/" + "phrase_APs.out", outputPath + "/" + "phrase_NP.out");
        if (agreement && !markGrammarError) {
            Phrase_NP2 np2 = new Phrase_NP2(sr);
            readwrite(np2, outputPath + "/" + "phrase_NP.out", outputPath + "/" + "phrase_NP2.out");
        }
        Phrase_VP vp = new Phrase_VP(sr);
        if (agreement && !markGrammarError) {
            readwrite(vp, outputPath + "/" + "phrase_NP2.out", outputPath + "/" + "phrase_VP.out");
        } else {
            readwrite(vp, outputPath + "/" + "phrase_NP.out", outputPath + "/" + "phrase_VP.out");
        }
        Case_NP cnp = new Case_NP(sr);
        readwrite(cnp, outputPath + "/" + "phrase_VP.out", outputPath + "/" + "case_NP.out");
        Phrase_NPs nps = new Phrase_NPs(sr);
        readwrite(nps, outputPath + "/" + "case_NP.out", outputPath + "/" + "phrase_NPs.out");
        Phrase_PP pp = new Phrase_PP(sr);
        readwrite(pp, outputPath + "/" + "phrase_NPs.out", outputPath + "/" + "phrase_PP.out");
        Clean1 cl1 = new Clean1(sr);
        readwrite(cl1, outputPath + "/" + "phrase_PP.out", outputPath + "/" + "clean1.out");
        if (includeFunc) {
            Func_TIMEX f_time = new Func_TIMEX(sr);
            readwrite(f_time, outputPath + "/" + "clean1.out", outputPath + "/" + "func_timex.out");
            Func_QUAL f_qual = new Func_QUAL(sr);
            readwrite(f_qual, outputPath + "/" + "func_timex.out", outputPath + "/" + "func_qual.out");
            Func_SUBJ f_subj = new Func_SUBJ(sr);
            if (agreement) {
                f_subj.set_doAgreementCheck(true);
            }
            if (markGrammarError) {
                f_subj.set_markGrammarError(true);
            }
            readwrite(f_subj, outputPath + "/" + "func_qual.out", outputPath + "/" + "func_subj.out");
            Func_COMP f_comp = new Func_COMP(sr);
            readwrite(f_comp, outputPath + "/" + "func_subj.out", outputPath + "/" + "func_comp.out");
            Func_OBJ f_obj = new Func_OBJ(sr);
            readwrite(f_obj, outputPath + "/" + "func_comp.out", outputPath + "/" + "func_obj.out");
            Func_OBJ2 f_obj2 = new Func_OBJ2(sr);
            readwrite(f_obj2, outputPath + "/" + "func_obj.out", outputPath + "/" + "func_obj2.out");
            Func_OBJ3 f_obj3 = new Func_OBJ3(sr);
            readwrite(f_obj3, outputPath + "/" + "func_obj2.out", outputPath + "/" + "func_obj3.out");
            Func_SUBJ2 f_subj2 = new Func_SUBJ2(sr);
            readwrite(f_subj2, outputPath + "/" + "func_obj3.out", outputPath + "/" + "func_subj2.out");
        }
        Clean2 cl2 = new Clean2(sr);
        if (includeFunc) readwrite(cl2, outputPath + "/" + "func_subj2.out", outputPath + "/" + "clean2.out"); else readwrite(cl2, outputPath + "/" + "clean1.out", outputPath + "/" + "clean2.out");
        Phrase_Per_Line ppl = new Phrase_Per_Line(sr);
        TagDecoder tagDecdr = new TagDecoder(sr);
        if ((outputType == OutputFormatter.OutputType.plain || outputType == OutputFormatter.OutputType.phrase_per_line) && !mergeLabels) {
            if (outputType == OutputFormatter.OutputType.plain) {
                readwrite(tagDecdr, outputPath + "/" + "clean2.out", outputFile);
            } else {
                readwrite(ppl, outputPath + "/" + "clean2.out", outputPath + "/" + "phrase_per_line.out");
                readwrite(tagDecdr, outputPath + "/" + "phrase_per_line.out", outputFile);
            }
        } else {
            readwrite(tagDecdr, outputPath + "/" + "clean2.out", outputPath + "/" + "decoded.out");
            OutputFormatter of = new OutputFormatter();
            formatter(of, outputPath + "/" + "decoded.out", outputFile, outputType, mergeLabels);
        }
    }
