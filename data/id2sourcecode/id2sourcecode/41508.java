            public Integer doInBackground() {
                MessageDigest md5, mdSHA;
                md5 = mdSHA = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                    mdSHA = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                }
                try {
                    FileInputStream fins;
                    FileOutputStream fout = new FileOutputStream(outFile);
                    int n;
                    byte[] bufRaw = new byte[1024000];
                    setProgress(0);
                    for (int i = 0; i < inFilesList.length; ) {
                        System.out.println("Joining " + inFilesList[i]);
                        fins = new FileInputStream(inFilesList[i]);
                        do {
                            n = fins.read(bufRaw);
                            if (n > 0) {
                                if (computeMD5) md5.update(bufRaw, 0, n);
                                if (computeSHA1) mdSHA.update(bufRaw, 0, n);
                                fout.write(bufRaw, 0, n);
                            }
                        } while (n == bufRaw.length);
                        fins.close();
                        setProgress(++i * 100 / inFilesList.length);
                    }
                    fout.close();
                } catch (FileNotFoundException e) {
                    errcode = 1;
                    errmessage = e.getMessage();
                } catch (IOException e) {
                    errcode = 2;
                    errmessage = e.getMessage();
                }
                setProgress(100);
                if (computeMD5) {
                    byte dig[];
                    String oldH;
                    try {
                        dig = md5.digest();
                        BufferedReader fmd5 = new BufferedReader(new FileReader(dialMD5.getFile()));
                        oldH = fmd5.readLine();
                        fmd5.close();
                        if (oldH.compareToIgnoreCase(ProgramWindow.arrayBytetohexString(dig)) == 0) md5result = 1; else md5result = -1;
                    } catch (IOException oe) {
                        errcode = 3;
                        errmessage = oe.getMessage();
                    }
                }
                if (computeSHA1) {
                    byte dig[];
                    String oldH;
                    try {
                        dig = mdSHA.digest();
                        BufferedReader fsha = new BufferedReader(new FileReader(dialSHA1.getFile()));
                        oldH = fsha.readLine();
                        fsha.close();
                        if (oldH.compareToIgnoreCase(ProgramWindow.arrayBytetohexString(dig)) == 0) sharesult = 1; else sharesult = -1;
                    } catch (IOException oe) {
                        errcode = 4;
                        errmessage = oe.getMessage();
                    }
                }
                return errcode;
            }
