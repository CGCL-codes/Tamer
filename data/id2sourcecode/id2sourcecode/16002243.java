    @Test
    public void testConvertToModule1() throws IOException {
        LearnerGraph gr = buildLearnerGraph("A- {call, read} ->B<-{call, read}-C-{call, {write,aa}}->A", "testConstructErlGraph1", config);
        {
            Set<Label> alphabet = gr.pathroutines.computeAlphabet();
            Set<String> labels = new TreeSet<String>();
            Iterator<Label> iter = alphabet.iterator();
            labels.add(iter.next().toErlangTerm());
            labels.add(iter.next().toErlangTerm());
            Assert.assertEquals("[{" + ErlangLabel.missingFunction + ",'call','read'}, {" + ErlangLabel.missingFunction + ",'call',{'write','aa'}}]", labels.toString());
        }
        File file = new File("ErlangExamples/locker/locker.erl");
        ErlangModule mod = ErlangModule.loadModule(ErlangModule.setupErlangConfiguration(file));
        final String expected = "[{{LOCKERPATH,25,handle_call,1,{'Func',[],[{'Alt',[],[" + "{'Atom',[],['lock','read','unlock']}," + "{'Tuple',[],[{'Atom',[],['write']},{'Any',[]}]}]}],{'Any',[]}}},2,'call','read'}, " + "{{LOCKERPATH,25,handle_call,1,{'Func',[],[{'Alt',[],[" + "{'Atom',[],['lock','read','unlock']}," + "{'Tuple',[],[{'Atom',[],['write']},{'Any',[]}]}]}],{'Any',[]}}},2,'call',{'write','aa'}}]";
        {
            LearnerGraph transformed = gr.transform.interpretLabelsOnGraph(mod.behaviour.new ConverterErlToMod());
            Set<Label> alphabet = transformed.pathroutines.computeAlphabet();
            StringBuffer quotedFileName = new StringBuffer();
            ErlangLabel.ErlangString.getSingleton().dump(file.getAbsolutePath(), quotedFileName);
            Set<String> labels = new TreeSet<String>();
            Iterator<Label> iter = alphabet.iterator();
            labels.add(iter.next().toErlangTerm());
            labels.add(iter.next().toErlangTerm());
            Assert.assertEquals(expected, labels.toString().replace(quotedFileName.toString(), "LOCKERPATH"));
        }
        {
            LearnerGraph transformed = gr.transform.interpretLabelsOnGraph(mod.behaviour.new ConverterErlToMod());
            Set<Label> alphabet = transformed.pathroutines.computeAlphabet();
            StringBuffer quotedFileName = new StringBuffer();
            ErlangLabel.ErlangString.getSingleton().dump(file.getAbsolutePath(), quotedFileName);
            Set<String> labels = new TreeSet<String>();
            Iterator<Label> iter = alphabet.iterator();
            labels.add(iter.next().toErlangTerm());
            labels.add(iter.next().toErlangTerm());
            Assert.assertEquals(expected, labels.toString().replace(quotedFileName.toString(), "LOCKERPATH"));
        }
    }
