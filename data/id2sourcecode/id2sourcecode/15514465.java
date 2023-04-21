    @Override
    public FormStatus execute(Dictionary<String, String> dictionary) {
        FormStatus retVal = FormStatus.InfoError;
        String executable = null, inputFile = null, cmd = null, nextLine = null;
        String stdOutFileName = null, stdErrFileName = null;
        FileWriter stdOut = null, stdErr = null;
        if (dictionary != null) {
            executable = dictionary.get("executable");
            inputFile = dictionary.get("inputFile");
            stdOutFileName = dictionary.get("stdOutFileName");
            stdErrFileName = dictionary.get("stdErrFileName");
            if (executable != null && inputFile != null && stdOutFileName != null && stdErrFileName != null) {
                cmd = executable + " " + inputFile;
                try {
                    job = Runtime.getRuntime().exec(cmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("LocalExecutorAction Message: " + "Launching command: " + "\"" + cmd + "\"");
                InputStream istream = job.getInputStream();
                InputStreamReader istreamReader = new InputStreamReader(istream);
                BufferedReader reader = new BufferedReader(istreamReader);
                try {
                    stdOut = new FileWriter(stdOutFileName, true);
                    System.out.println("File location = " + stdOutFileName);
                    BufferedWriter stdOutBW = new BufferedWriter(stdOut);
                    while ((nextLine = reader.readLine()) != null) {
                        stdOut.write(nextLine);
                        System.out.println(nextLine);
                    }
                    stdOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (retVal.equals(FormStatus.InfoError)) {
            return retVal;
        } else {
            status = FormStatus.Processed;
            return retVal;
        }
    }
