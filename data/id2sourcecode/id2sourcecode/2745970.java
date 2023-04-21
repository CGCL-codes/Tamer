    private static void nalezenaShodnost(final String fileName, final String ShodneS, final String directory) {
        try {
            final GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(System.currentTimeMillis());
            final String path = String.format("%s%tF%s%s", Ppa1Cviceni.SHODNE + File.separator, cal, directory.substring(Ppa1Cviceni.VALIDOVANE.length()), fileName);
            FileUtils.copyFile(new File(directory + fileName), new File(path));
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        final StudentType student = findStudent(fileName);
        if (student != null) {
            final DomaciUlohyType dut = student.getDomaciUlohy();
            final NalezenaShodnostType nst = dut.getNalezenaShodnost();
            nst.setVysledek(true);
            final String[] nazevSouboru = fileName.split("_");
            CviceniType cviceni = null;
            for (final CviceniType cv : dut.getCviceni()) {
                if (cv.getCislo().intValue() == Integer.parseInt(nazevSouboru[1].substring(2))) {
                    cviceni = cv;
                    break;
                }
            }
            for (final UlohaType ut : cviceni.getUloha()) {
                if (ut.getCislo().intValue() == Integer.parseInt(nazevSouboru[2])) {
                    final List<OdevzdanoType> odevzdane = ut.getOdevzdano();
                    int last = 0;
                    OdevzdanoType ot = null;
                    do {
                        ot = odevzdane.get(last);
                        last++;
                    } while ((!odevzdane.get(last - 1).getValidator().isVysledek()) && last < odevzdane.size());
                    if (!ot.getValidator().isVysledek()) {
                        continue;
                    }
                    HashSet<String> mnozinaOsobnichCiselOpisujicich = new HashSet<String>();
                    List<ShodneSType> shodneSTypeList = ot.getShodneS();
                    for (ShodneSType sst : shodneSTypeList) {
                        if (!mnozinaOsobnichCiselOpisujicich.contains(sst.getOsCislo())) {
                            mnozinaOsobnichCiselOpisujicich.add(sst.getOsCislo());
                        }
                    }
                    ObjectFactory of = new ObjectFactory();
                    final ShodneSType shodny = of.createShodneSType();
                    shodny.setOsCislo(ShodneS.split("[._]")[3]);
                    if (!mnozinaOsobnichCiselOpisujicich.contains(shodny.getOsCislo())) {
                        ot.getShodneS().add(shodny);
                    }
                    break;
                }
            }
        }
    }
