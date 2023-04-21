    public void finish() {
        String checksum = "";
        String tmp = "";
        int imp;
        ARCH_DIR = DataGatherFrame.ARCH_DIR;
        String VTMP_DIR = DataGatherFrame.VTMP_DIR;
        try {
            BufferedReader in = new BufferedReader(new FileReader(VTMP_DIR + CONFIG_FILE));
            String line;
            String name;
            String path;
            String[] configField = new String[3];
            int x;
            int y;
            byte b;
            byte[] sum;
            try {
                hashSum = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithmException caught... nothing done about it.");
            }
            while ((line = in.readLine()) != null) {
                if (!(line.equals(""))) {
                    x = line.indexOf(':');
                    configField[0] = line.substring(0, x);
                    y = line.lastIndexOf(':');
                    configField[1] = line.substring(x + 1, y);
                    configField[2] = line.substring(y + 1, line.length());
                    for (int j = 0; j < configField.length; j++) System.out.println("configField[" + j + "]: " + configField[j]);
                    File nameFile = new File(ARCH_DIR + configField[0]);
                    nameFile.mkdir();
                    File f = new File(configField[1]);
                    FileInputStream fin = new FileInputStream(f);
                    DataInputStream din = new DataInputStream(fin);
                    File o = new File(ARCH_DIR + configField[0] + "/" + configField[0]);
                    FileOutputStream fout = new FileOutputStream(o);
                    DataOutputStream dout = new DataOutputStream(fout);
                    hashSum.reset();
                    try {
                        while (true) {
                            b = din.readByte();
                            dout.write(b);
                            hashSum.update(b);
                        }
                    } catch (EOFException e) {
                        fout.close();
                        fin.close();
                        dout.close();
                        din.close();
                        sum = hashSum.digest();
                        PrintWriter hashFile = new PrintWriter(new FileWriter(ARCH_DIR + configField[0] + "/" + configField[0] + ".digest"));
                        System.out.println("Hash: ");
                        for (int i = 0; i < sum.length; i++) {
                            tmp = Byte.toString(sum[i]);
                            imp = Byte.decode(tmp).intValue();
                            if (imp < 0) {
                                tmp = Integer.toHexString(imp);
                                String tmp1 = tmp.substring(6, tmp.length());
                                tmp = tmp1;
                            } else {
                                tmp = Integer.toHexString(imp);
                                if (tmp.length() == 1) tmp = "0" + tmp;
                            }
                            checksum += tmp;
                        }
                        hashFile.println(checksum);
                        System.out.println(checksum);
                        hashFile.close();
                        checksum = "";
                    } catch (IOException e) {
                        fout.close();
                        fin.close();
                        dout.close();
                        din.close();
                        System.out.println("An IOException occurred:\n");
                        e.printStackTrace();
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("An IOException occurred:\n");
            e.printStackTrace();
        }
        MessagePrompt p = new MessagePrompt("Operation Complete", "Files in \"" + VTMP_DIR + "\" are ready for archiving.");
        p.show();
    }
