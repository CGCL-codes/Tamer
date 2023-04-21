    public static void main(String[] args) {
        PasswordGenerator gen = new PasswordGeneratorImpl();
        Hasher hasher = new HasherImpl();
        HashAlg alg = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        if (args.length < 1 || args.length > 5) {
            showUsage(0);
            return;
        }
        boolean arguments = false, showPsws = true;
        if (args[0].indexOf("-", 0) != -1 && args[0].length() > 1) {
            arguments = true;
            int argsLen = args[0].length();
            String charSet = "", oldCharSet = gen.getCharacterSet();
            for (int i = 1; i < argsLen; i++) {
                char c = args[0].charAt(i);
                if (c == 'a') gen.addRule(new NoAnnoyingCharsRule()); else if (c == 'l') gen.addRule(new HasLowCaseCharsRule()); else if (c == 'u') gen.addRule(new HasHighCaseCharsRule()); else if (c == 'd') gen.addRule(new HasNumbersRule()); else if (c == 'c') gen.addRule(new CheckRowOriginRule()); else if (c == 'r') gen.addRule(new SingleAppearanceCharRule()); else if (c == 'y') charSet += PasswordGeneratorImpl.Zero_Through_Nine_SET; else if (c == 'k') charSet += PasswordGeneratorImpl.a_Through_z_SET; else if (c == 'i') charSet += PasswordGeneratorImpl.A_Through_Z_SET; else if (c == 'e') charSet += PasswordGeneratorImpl.Exotic_Characters_SET; else if (c == 'o') charSet += PasswordGeneratorImpl.Normal_Symbols_SET; else if (c == 's') charSet += PasswordGeneratorImpl.Exotic_Symbols_SET; else if (c == 'n') showPsws = false; else if (c == 'q') alg = HashAlg.MD5; else if (c == 'p') alg = HashAlg.SHA1; else if (c == 'j') alg = HashAlg.SHA256; else if (c == 'b') alg = HashAlg.SHA512;
            }
            if (charSet.equals("")) gen.setCharacterSet(oldCharSet); else gen.setCharacterSet(charSet);
            if (args[0].indexOf("x") != -1) {
                if (args.length >= 4 && args[3].indexOf("o=") == -1) gen.setCharacterSet("");
                if (args.length == 5 && args[4].indexOf("o=") == -1) gen.setCharacterSet("");
                if (args.length < 4 || args.length > 5) {
                    showUsage(3);
                    return;
                }
            }
        }
        if (args.length >= 4 && args[3].indexOf("o=") == -1) gen.setCharacterSet(gen.getCharacterSet() + args[3]); else if (args.length == 5 && args[4].indexOf("o=") == -1) gen.setCharacterSet(gen.getCharacterSet() + args[4]);
        int pswLenArgPos = (arguments ? 1 : 0), pswLen = 0;
        try {
            pswLen = Integer.parseInt(args[pswLenArgPos]);
            if (pswLen <= 0) {
                showUsage(2);
                return;
            }
        } catch (Exception e) {
            showUsage(1);
            return;
        }
        int pswAmountArgPos = (arguments ? 2 : 1), pswAmount = 0;
        try {
            pswAmount = Integer.parseInt(args[pswAmountArgPos]);
            if (pswAmount <= 0) {
                showUsage(5);
                return;
            }
        } catch (Exception e) {
            showUsage(6);
            return;
        }
        ArrayList<String> list = new ArrayList<String>();
        if (showPsws) System.out.print("Generating " + pswAmount + " password(s):\n"); else System.out.print("Generating " + pswAmount + " password(s)");
        for (int k = 0; k < pswAmount; k++) {
            String psw = "";
            while (true) {
                psw = gen.generate(pswLen);
                if (!list.contains(psw)) {
                    list.add(psw);
                    break;
                }
            }
            if (showPsws) {
                System.out.print("  " + psw);
                if (alg != null) System.out.print("  [" + hasher.genHexStringHash(psw, alg) + "]");
                System.out.println("");
            }
        }
        if (!showPsws) System.out.println(" ..done");
        if (args.length > 3) {
            int outFileArgPos = (arguments ? 3 : 2);
            String outfile = args[outFileArgPos];
            if (outfile.indexOf("o=") != -1) {
                if (outfile.length() > 2) {
                    outfile = outfile.substring(2);
                    File file = new File(outfile);
                    if (file.exists()) {
                        System.out.println("'" + outfile + "' already exists!\nDo you want to overwrite it? [y/n]");
                        try {
                            reader = new BufferedReader(new InputStreamReader(System.in));
                            String input = reader.readLine().toLowerCase();
                            if (input.equals("y")) file.delete();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                    try {
                        writer = new BufferedWriter(new FileWriter(file));
                        for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
                            String psw = (String) it.next();
                            writer.write(psw + '\n');
                        }
                        writer.close();
                        System.out.println("Password(s) written file: " + outfile);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                } else {
                    showUsage(4);
                    return;
                }
            }
        }
        int charSetSize = gen.getCharacterSet().length();
        double entropyBits = gen.checkStrength(pswLen, charSetSize);
        System.out.println("Password strength: " + (int) entropyBits + " bits");
        System.out.println("Level of protection: " + gen.strengthDescription(entropyBits));
    }
