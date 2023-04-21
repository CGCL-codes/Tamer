    public List<IMaudeJob> createJobs(InputStream input) throws ParseException {
        List<IMaudeJob> jobs = new ArrayList<IMaudeJob>();
        if (isFullMaude()) try {
            int inputChars = input.available();
            FullMaudeCommandsParser p = new FullMaudeCommandsParser(new FullMaudeCommandsLexer(input));
            p.program();
            BaseTermsJoinerTreeParser bt = new BaseTermsJoinerTreeParser();
            List<String> commands = bt.program(p.getAST());
            int charCounter = 0;
            for (String s : commands) {
                jobs.add(new MaudeJob(s));
                charCounter += s.length();
            }
        } catch (Exception e) {
            throw new ParseException(e);
        } else {
            try {
                InputStreamReader isr = new InputStreamReader(input);
                StringWriter output = new StringWriter();
                char[] buffer = new char[1024];
                int n;
                while (-1 != (n = isr.read(buffer))) output.write(buffer, 0, n);
                jobs.add(new MaudeJob(output.toString()));
            } catch (Exception e) {
                throw new ParseException(e);
            }
        }
        return jobs;
    }
