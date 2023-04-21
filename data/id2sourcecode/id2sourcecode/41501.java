            public Integer doInBackground() {
                int i, k;
                long j, numPieces;
                MessageDigest md5, mdSHA;
                FileInputStream fins;
                FileOutputStream fout;
                if (limitSize > 1024000) {
                    inputbuffersize = 1024000;
                    quo = (int) (limitSize / 1024000);
                    rem = (int) (limitSize % 1024000);
                } else {
                    inputbuffersize = (int) limitSize;
                    quo = 1;
                    rem = 0;
                }
                byte[] bufRaw = new byte[inputbuffersize];
                md5 = mdSHA = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                    mdSHA = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                }
                try {
                    fins = new FileInputStream(f);
                    j = f.length();
                    numPieces = j / limitSize;
                    if (j > limitSize * numPieces) ++numPieces;
                    fileoutputname = fileoutputname + ".part" + numPieces + "_" + numPieces;
                    char[] ofname = fileoutputname.toCharArray();
                    setZeroFilename(ofname);
                    setProgress(0);
                    for (i = 1; i < numPieces; ++i) {
                        incrementFilename(ofname);
                        System.out.println(new String(ofname));
                        fout = new FileOutputStream(new String(ofname));
                        for (k = 0; k < quo; ++k) {
                            fins.read(bufRaw);
                            if (computeMD5) md5.update(bufRaw);
                            if (computeSHA1) mdSHA.update(bufRaw);
                            fout.write(bufRaw);
                        }
                        fins.read(bufRaw, 0, rem);
                        if (computeMD5) md5.update(bufRaw, 0, rem);
                        if (computeSHA1) mdSHA.update(bufRaw, 0, rem);
                        fout.write(bufRaw, 0, rem);
                        fout.close();
                        setProgress((int) (i * 100 / numPieces));
                    }
                    incrementFilename(ofname);
                    System.out.println(new String(ofname));
                    fout = new FileOutputStream(new String(ofname));
                    do {
                        k = fins.read(bufRaw);
                        if (k > 0) {
                            fout.write(bufRaw, 0, k);
                            if (computeMD5) md5.update(bufRaw, 0, k);
                            if (computeSHA1) mdSHA.update(bufRaw, 0, k);
                        }
                    } while (k > 0);
                    fout.close();
                    setProgress(100);
                    fins.close();
                } catch (FileNotFoundException e) {
                    errcode = 1;
                    errmessage = e.getMessage();
                } catch (IOException e) {
                    errcode = 2;
                    errmessage = e.getMessage();
                }
                if (computeMD5) {
                    byte dig[];
                    try {
                        dig = md5.digest();
                        DataOutputStream fmd5 = new DataOutputStream(new FileOutputStream(dialMD5.getFile()));
                        fmd5.writeBytes(ProgramWindow.arrayBytetohexString(dig));
                        fmd5.close();
                    } catch (IOException oe) {
                        errmessage = oe.getMessage();
                        errcode = 3;
                    }
                }
                if (computeSHA1) {
                    byte dig[];
                    try {
                        dig = mdSHA.digest();
                        DataOutputStream fsha = new DataOutputStream(new FileOutputStream(dialSHA1.getFile()));
                        fsha.writeBytes(ProgramWindow.arrayBytetohexString(dig));
                        fsha.close();
                    } catch (IOException oe) {
                        errmessage = oe.getMessage();
                        errcode = 4;
                    }
                }
                return errcode;
            }
