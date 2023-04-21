    public Correlation getCorrelationBetween(String sFile, String sMeasure1, String sMeasure2) {
        Correlation cRes = new Correlation();
        try {
            File fTempFile = File.createTempFile("jinsect", "Rscript");
            PrintStream pOut = new PrintStream(fTempFile);
            pOut.println("Dataset <- read.table(\"" + sFile + "\", header=TRUE, sep=\"\", na.strings=\"NA\", dec=\".\", strip.white=TRUE)");
            pOut.println("cor(Dataset$" + sMeasure1 + ",Dataset$" + sMeasure2 + ", use = \"all.obs\", method = \"spearman\");");
            pOut.println("cor(Dataset$" + sMeasure1 + ",Dataset$" + sMeasure2 + ", use = \"all.obs\", method = \"pearson\");");
            pOut.flush();
            pOut.close();
            String[] saCmd = { "R", "--no-save", "--slave" };
            ProcessBuilder pbP = new ProcessBuilder(saCmd);
            Process p = pbP.start();
            InputStream isIn = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(isIn));
            OutputStream osOut = p.getOutputStream();
            PrintStream pR = new PrintStream(osOut);
            FileReader fr = new FileReader(fTempFile);
            int cChar;
            while ((cChar = fr.read()) != -1) pR.write(cChar);
            pR.flush();
            pR.close();
            osOut.close();
            String sStr = "";
            String sSpearman = "";
            String sPearson = "";
            while ((sStr = br.readLine()) != null) {
                if (sSpearman.length() == 0) {
                    if (sStr.indexOf("[1]") > -1) {
                        Iterator iIter = Arrays.asList(sStr.split("\\s")).iterator();
                        while (iIter.hasNext()) {
                            String sToken = (String) iIter.next();
                            if (sToken.matches("\\d+[.,]\\d+")) {
                                sSpearman = sToken;
                                cRes.Spearman = Double.valueOf(sSpearman);
                            }
                        }
                    }
                } else if (sPearson.length() == 0) {
                    if (sStr.indexOf("[1]") > -1) {
                        Iterator iIter = Arrays.asList(sStr.split("\\s")).iterator();
                        while (iIter.hasNext()) {
                            String sToken = (String) iIter.next();
                            if (sToken.matches("\\d+[.,]\\d+")) {
                                sPearson = sToken;
                                cRes.Pearson = Double.valueOf(sPearson);
                            }
                        }
                    }
                }
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Process was interrupted");
            }
            if (p.exitValue() != 0) System.err.println("Exit value was non-zero");
            br.close();
            isIn.close();
            fTempFile.delete();
            return cRes;
        } catch (IOException ioe) {
            System.out.println("Execution failed: " + ioe.getMessage());
            return cRes;
        }
    }
