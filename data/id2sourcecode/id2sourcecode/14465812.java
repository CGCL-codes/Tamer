    public TBuilding readBuilding() {
        FileChannel fin;
        try {
            try {
                fin = new FileInputStream("build.sch").getChannel();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                return null;
            }
            ByteBuffer bb = ByteBuffer.allocate((int) fin.size());
            String currstr = new String();
            double currf, currf2;
            int currn, currn2;
            int i;
            TBuilding rezB = new TBuilding();
            String[] str = new String[] { "" };
            String encoding = "";
            String newrow = "((\n\r)+|(\n)+|(\r)+)";
            fin.read(bb);
            bb.flip();
            byte h = 0;
            while (true) {
                try {
                    encoding = mas_encode[h];
                    str = Charset.forName(encoding).decode(bb).toString().split("(( )*" + newrow + "( )*)+|: *| +|(( )*" + newrow + "( )*)+");
                } catch (IllegalArgumentException e) {
                    if (++h >= mas_encode.length) {
                        System.out.println("Encoding fatal Error");
                        return null;
                    }
                    System.out.println("Illegal argument encoding, try with " + mas_encode[h] + "...");
                    continue;
                }
                break;
            }
            bb.clear();
            i = 0;
            try {
                if (str.length > 1) {
                    i++;
                }
                while (i < str.length) {
                    currn = Integer.parseInt(str[i++]);
                    currstr = str[i++];
                    if (currstr.compareTo(Const.PREM) == 0) {
                        currf = Double.parseDouble(str[i++]);
                        currf2 = Double.parseDouble(str[i++]);
                        currn2 = Integer.parseInt(str[i++]);
                        if (!rezB.containsPrem(currn)) {
                            rezB.addPrem(currn, currf, currf2, currn2, Const.PREM);
                        } else {
                            Const.error(50);
                        }
                    } else if (currstr.compareTo(Const.EXIT) == 0) {
                        if (!rezB.containsPrem(currn)) {
                            rezB.addPrem(currn, TPremises.lexit, TPremises.bexit, 0, Const.EXIT);
                        } else {
                            Const.error(50);
                        }
                    } else if (currstr.compareTo(Const.PASS) == 0) {
                        currf = Double.parseDouble(str[i++]);
                        if (!rezB.containsPass(currn)) {
                            rezB.addPass(currn, currf);
                        } else {
                            Const.error(51);
                        }
                    } else {
                        Const.error(60);
                        System.out.println(currstr);
                    }
                }
            } catch (NumberFormatException e) {
            }
            int[] nbr;
            currn = Integer.parseInt(str[i++]);
            while (i < str.length) {
                nbr = new int[4];
                nbr[0] = Integer.parseInt(str[i++]);
                nbr[1] = Integer.parseInt(str[i++]);
                nbr[2] = Integer.parseInt(str[i++]);
                nbr[3] = Integer.parseInt(str[i++]);
                rezB.link(currn, nbr);
                if (i < str.length) currn = Integer.parseInt(str[i++]);
            }
            fin.close();
            rezB.Main_HEAD = rezB.Exits.get(0).Exit;
            rezB.computeXY();
            return rezB;
        } catch (IOException e) {
            System.out.println("Input Error");
            return null;
        }
    }
