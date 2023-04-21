    public static VelocityContext getContext(HttpSession session, boolean calculated, boolean measured, StringBuffer messages, Object servconorrundata, HttpServletRequest request, boolean print) throws IOException, FileNotFoundException, Exception {
        DecimalFormat myFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
        VelocityContext context = new VelocityContext();
        ServletConfig servcon;
        if (servconorrundata instanceof RunData) {
            servcon = ((RunData) servconorrundata).getServletConfig();
        } else {
            servcon = (ServletConfig) servconorrundata;
        }
        String input = ((StringBuffer) session.getAttribute(NmrshiftdbConstants.INPUT)).toString();
        context.put(NmrshiftdbConstants.INPUT, input);
        if (session.getAttribute("twospheres") == null) {
            session.setAttribute("twospheres", "false");
        }
        if (request.getParameter("twospheres") != null) {
            session.setAttribute("twospheres", request.getParameter("twospheres"));
        }
        if (session.getAttribute("hosecode") == null) {
            session.setAttribute("hosecode", "false");
        }
        if (request.getParameter("hosecode") != null) {
            session.setAttribute("hosecode", request.getParameter("hosecode"));
        }
        context.put("hosecode", session.getAttribute("hosecode"));
        context.put("twospheres", session.getAttribute("twospheres"));
        int numberOfAtoms = 0;
        Vector<ValuesForVelocityBean> elementsOfChoosenType = new Vector<ValuesForVelocityBean>();
        Vector<ValueTriple> predictions = new Vector<ValueTriple>();
        Map predictionValuesForAppletMap = new HashMap();
        context.put("choosennumber", session.getAttribute("choosennumber"));
        if (print) {
            context.put("print", "1");
        } else {
            context.put("print", "0");
        }
        Vector<StringAndInt> numbers = new Vector<StringAndInt>();
        for (int i = 0; i <= Integer.parseInt(GeneralUtils.getNmrshiftdbProperty("hosecode.spheres")); i++) {
            if (i == 0) {
                numbers.add(new StringAndInt("Cdk", i));
            } else {
                numbers.add(new StringAndInt(i + "", i));
            }
        }
        context.put("numbers", numbers);
        StringBuffer spectrumForApplet = new StringBuffer();
        StringBuffer tooltips = new StringBuffer();
        String predictAtomType = "";
        IMolecule mol = (IMolecule) session.getAttribute("mol");
        IMolecule molh = (IMolecule) session.getAttribute("molh");
        if (session.getAttribute("trueonly") == null) session.setAttribute("trueonly", new Boolean(true));
        if (input.equals("0")) {
            mol = (IMolecule) mol.clone();
            HueckelAromaticityDetector.detectAromaticity(mol, false);
            HOSECodeGenerator hcg = new HOSECodeGenerator();
            if (request.getParameter("spectrumType") != null) {
                session.setAttribute("spectype", request.getParameter("spectrumType"));
            }
            if (session.getAttribute("spectype") == null) {
                predictAtomType = "C";
            } else if (((String) session.getAttribute("spectype")).equals("100")) {
                predictAtomType = "Hsvm";
            } else if (((String) session.getAttribute("spectype")).equals("101")) {
                predictAtomType = "All";
            } else {
                predictAtomType = DBSpectrumTypePeer.retrieveByPK(new NumberKey((String) session.getAttribute("spectype"))).getDBIsotope(1).getElementSymbol();
            }
            if (!predictAtomType.equals("Hsvm")) {
                for (int i = 0; i < mol.getAtomCount(); i++) {
                    if (mol.getAtom(i).getSymbol().equals(predictAtomType) || predictAtomType.equals("H") || predictAtomType.equals("All")) {
                        numberOfAtoms++;
                        boolean added = false;
                        try {
                            IAtom hatom = null;
                            StringBuffer comment = new StringBuffer();
                            StringBuffer hoseCode = new StringBuffer();
                            StringBuffer hoseCode2 = new StringBuffer();
                            double[] statistics = null;
                            if (predictAtomType.equals("H")) {
                                List connatoms = molh.getConnectedAtomsList(molh.getAtom(i));
                                for (int k = 0; k < connatoms.size(); k++) {
                                    if (((IAtom) connatoms.get(k)).getSymbol().equals("H")) {
                                        hatom = (IAtom) connatoms.get(k);
                                        hoseCode = new StringBuffer();
                                        statistics = AtomUtils.predictRange(molh, hatom, calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, ((Integer) session.getAttribute("choosennumber")).intValue(), true, hoseCode, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                                    }
                                }
                            } else {
                                statistics = AtomUtils.predictRange(mol, mol.getAtom(i), calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, ((Integer) session.getAttribute("choosennumber")).intValue(), true, hoseCode, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                            }
                            if (statistics != null) {
                                double[] statistics2 = null;
                                elementsOfChoosenType.add(new ValuesForVelocityBean(new Float(statistics[1]), i + "", 0, 1000, "", new String("" + (i + 1))));
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).statistics = statistics;
                                added = true;
                                ValueTriple vt = new ValueTriple();
                                vt.value1 = (float) statistics[1];
                                vt.atoms.add(new Float(i));
                                if (mol.getAtom(i).getHydrogenCount() == 0) {
                                    vt.multiplicityString = "s";
                                } else if (mol.getAtom(i).getHydrogenCount() == 1) {
                                    vt.multiplicityString = "d";
                                } else if (mol.getAtom(i).getHydrogenCount() == 2) {
                                    vt.multiplicityString = "t";
                                } else if (mol.getAtom(i).getHydrogenCount() == 3) {
                                    vt.multiplicityString = "q";
                                }
                                vt.sortPosition = mol.getAtom(i).getHydrogenCount();
                                predictions.add(vt);
                                if (statistics[4] > -1) {
                                    if (predictAtomType.equals("H")) {
                                        statistics2 = AtomUtils.predictRange(molh, hatom, calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, (int) statistics[4] - 1, true, hoseCode2, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                                    } else {
                                        statistics2 = AtomUtils.predictRange(mol, mol.getAtom(i), calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, (int) statistics[4] - 1, true, hoseCode2, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                                    }
                                }
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).statistics2 = statistics2;
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setDelta(hoseCode.toString());
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setIdentifier(hoseCode2.toString());
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setIntensity(hoseCode.toString());
                            }
                        } catch (CDKException ex) {
                            if (!added) {
                                elementsOfChoosenType.add(new ValuesForVelocityBean(new Float(0), i + "", 0, 1000, new String("No data available"), new String("" + (i + 1))));
                            }
                        } catch (Exception ex) {
                            if (!added) {
                                elementsOfChoosenType.add(new ValuesForVelocityBean(new Float(0), i + "", 0, 1000, new String("No data available"), new String("" + (i + 1))));
                            }
                            GeneralUtils.logError(ex, "prediction", null, true);
                        }
                    }
                }
                context.put("coords", "2d");
            } else {
                boolean predictbyhose = ((String) session.getAttribute("hosecode")).equals("true");
                HashMap<IAtom, String> hcmap = new HashMap<IAtom, String>();
                StringBuffer[] comments = new StringBuffer[molh.getAtomCount()];
                String smiles = new SmilesGenerator(DefaultChemObjectBuilder.getInstance()).createSMILES(molh);
                SmilesParser sp = new SmilesParser();
                IMolecule mol2 = sp.parseSmiles(smiles);
                List list = UniversalIsomorphismTester.getSubgraphAtomsMap(mol2, mol);
                IAtom[] newatoms = new IAtom[mol2.getAtomCount()];
                for (int i = 0; i < list.size(); i++) {
                    newatoms[((RMap) list.get(i)).getId2()] = mol2.getAtom(((RMap) list.get(i)).getId1());
                }
                int free = list.size();
                for (int i = 0; i < mol2.getAtomCount(); i++) {
                    if (mol2.getAtom(i).getSymbol().equals("H")) {
                        newatoms[free] = mol2.getAtom(i);
                        free++;
                    }
                }
                mol2.setAtoms(newatoms);
                AllRingsFinder arf = new AllRingsFinder();
                IRingSet ringSet = arf.findAllRings(mol);
                Criteria crit = new Criteria();
                crit.add(DBConditionPeer.CONDITION_TYPE_ID, "1");
                crit.add(DBConditionPeer.VALUE, "298");
                DBCondition temp = (DBCondition) DBConditionPeer.doSelect(crit).get(0);
                crit = new Criteria();
                crit.add(DBConditionPeer.CONDITION_TYPE_ID, "3");
                crit.add(DBConditionPeer.VALUE, "125");
                DBCondition field = (DBCondition) DBConditionPeer.doSelect(crit).get(0);
                crit = new Criteria();
                crit.add(DBConditionPeer.CONDITION_TYPE_ID, "2");
                crit.add(DBConditionPeer.VALUE, "Chloroform-D1 (CDCl3)");
                DBCondition solvent = (DBCondition) DBConditionPeer.doSelect(crit).get(0);
                try {
                    GeneralUtils.getModelBuilder("mm2").setMolecule(mol2, false);
                    GeneralUtils.getModelBuilder("mm2").generate3DCoordinates();
                    mol = GeneralUtils.getModelBuilder("mm2").getMolecule();
                } catch (Exception ex) {
                    messages.append("We could not generate 3d coordinates, will use 2d coordianates. Probably the prediction is not as reliable as it could be!<br>");
                    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
                    sdg.setMolecule(mol2);
                    sdg.generateCoordinates();
                    mol = sdg.getMolecule();
                    for (int i = 0; i < mol.getAtomCount(); i++) {
                        mol.getAtom(i).setPoint3d(new Point3d(mol.getAtom(i).getPoint2d().x, mol.getAtom(i).getPoint2d().y, 0));
                    }
                }
                CharArrayWriter[] writer = new CharArrayWriter[5];
                int numberofentries[] = new int[5];
                ResultSetMetaData[] meta = new ResultSetMetaData[5];
                DBConnection dbconn = TurbineDB.getConnection();
                Statement stmt = dbconn.createStatement();
                for (int i = 1; i < 5; i++) {
                    writer[i] = new CharArrayWriter();
                    ResultSet rs = TurbineDB.getConnection().createStatement().executeQuery("select * from DESCRIPTORS_" + i + " limit 1");
                    meta[i] = rs.getMetaData();
                }
                OneToManyTable<String, Float> otmt = new OneToManyTable<String, Float>();
                float[][] divisor = new float[4][];
                float[][] minvalue = new float[4][];
                for (int i = 0; i < 4; i++) {
                    divisor[i] = new float[600];
                    minvalue[i] = new float[600];
                }
                String sql = "SELECT MIN_VALUE, DIVISOR, DESCRIPTOR, PROTONCLASS from DESCRIPTOR_FACTORS";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    minvalue[rs.getInt(4) - 1][Integer.parseInt(rs.getString(3).substring(3))] = rs.getFloat(1);
                    divisor[rs.getInt(4) - 1][Integer.parseInt(rs.getString(3).substring(3))] = rs.getFloat(2);
                }
                int[] mn = null;
                if (!predictbyhose) mn = MorganNumbersTools.getMorganNumbers(mol);
                for (int l = 0; l < mol.getAtomCount(); l++) {
                    if (mol.getAtom(l).getSymbol().equals("H")) {
                        int protonclass = AtomUtils.getProtonClass(mol, l, ringSet);
                        StringBuffer hoseCode = new StringBuffer();
                        if (predictbyhose) {
                            hoseCode.append(hcg.getHOSECode(mol, (IAtom) mol.getConnectedAtomsList(mol.getAtom(l)).get(0), 10, true));
                        } else {
                            hoseCode.append(mn[l] + "");
                        }
                        IAtom heavyatom = (IAtom) mol.getConnectedAtomsList(mol.getAtom(l)).get(0);
                        int protoncount = 0;
                        for (int i = 0; i < mol.getConnectedAtomsList(heavyatom).size(); i++) {
                            if (((IAtom) mol.getConnectedAtomsList(heavyatom).get(i)).getSymbol().equals("H")) protoncount++;
                        }
                        boolean second = false;
                        if (protoncount == 2) {
                            for (int i = 0; i < mol.getConnectedAtomsList(heavyatom).size(); i++) {
                                if (((IAtom) mol.getConnectedAtomsList(heavyatom).get(i)).getSymbol().equals("H")) {
                                    if (((IAtom) mol.getConnectedAtomsList(heavyatom).get(i)) == mol.getAtom(l)) {
                                        break;
                                    } else {
                                        second = true;
                                        break;
                                    }
                                }
                            }
                            hoseCode.append(second ? "2" : "1");
                        }
                        hcmap.put(mol.getAtom(l), hoseCode.toString());
                        float[] descriptors = AtomUtils.getDescriptorSet(temp, field, solvent, mol, mol.getAtom(l), AtomUtils.getAllRings(), protonclass);
                        mol.getAtom(l).setProperty("protonclass", new Integer(protonclass));
                        StringBuffer line = new StringBuffer();
                        line.append("0 ");
                        int colsnumber = meta[protonclass].getColumnCount();
                        int currentnumber = 1;
                        for (int k = 3; k <= colsnumber; k++) {
                            int desnumber = Integer.parseInt(meta[protonclass].getColumnLabel(k).substring(3));
                            line.append((currentnumber) + ":" + ((descriptors[desnumber] - minvalue[protonclass - 1][desnumber]) / divisor[protonclass - 1][desnumber]) + " ");
                            currentnumber++;
                        }
                        line.append("\n");
                        writer[protonclass].write(line.toString());
                        numberofentries[protonclass]++;
                        comments[l] = new StringBuffer("");
                    }
                }
                BufferedReader[] in = new BufferedReader[5];
                for (int i = 1; i < 5; i++) {
                    if (numberofentries[i] != 0) {
                        writer[i].close();
                        BufferedReader inputreader = new BufferedReader(new CharArrayReader(writer[i].toCharArray()));
                        if (model[i] == null) model[i] = svm.svm_load_model(ServletUtils.expandRelative(servcon, "/WEB-INF/tmp/descsvm.model" + i));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        svm_predict.predict(inputreader, new DataOutputStream(baos), model[i], 0);
                        in[i] = new BufferedReader(new StringReader(baos.toString()));
                    }
                }
                for (int l = 0; l < mol.getAtomCount(); l++) {
                    if (mol.getAtom(l).getSymbol().equals("H")) {
                        int protonclass = ((Integer) mol.getAtom(l).getProperty("protonclass")).intValue();
                        String hoseCode = (String) hcmap.get(mol.getAtom(l));
                        Float value = new Float(in[protonclass].readLine());
                        otmt.put(hoseCode, value);
                        if (predictbyhose) comments[l].append("SVM: " + (value.floatValue()));
                    }
                }
                boolean[] alreadyDone = new boolean[mol.getAtomCount()];
                for (int l = 0; l < mol.getAtomCount(); l++) {
                    if (mol.getAtom(l).getSymbol().equals("H")) {
                        String hoseCode = (String) hcmap.get(mol.getAtom(l));
                        IAtom heavyatom = (IAtom) mol.getConnectedAtomsList(mol.getAtom(l)).get(0);
                        int protoncount = 0;
                        for (int i = 0; i < mol.getConnectedAtomsList(heavyatom).size(); i++) {
                            if (((IAtom) mol.getConnectedAtomsList(heavyatom).get(i)).getSymbol().equals("H")) protoncount++;
                        }
                        int mothernumber = mol.getAtomNumber((IAtom) mol.getConnectedAtomsList(mol.getAtom(l)).get(0));
                        if (!alreadyDone[mothernumber] || protoncount == 2) {
                            if (protoncount != 2) {
                                Vector<Float> ev = new Vector<Float>();
                                for (Iterator<Float> e = otmt.get(hoseCode).iterator(); e.hasNext(); ) {
                                    ev.add(e.next());
                                }
                                double[] values = new double[ev.size()];
                                for (int m = 0; m < ev.size(); m++) {
                                    values[m] = ev.get(m).doubleValue();
                                }
                                double value = Statistics.average(values);
                                String valuestr = myFormatter.format(value);
                                elementsOfChoosenType.add(new ValuesForVelocityBean(new Float(value), mothernumber + "", 0, 1000, "", new String("" + (mothernumber + 1) + "-H")));
                                double[] statistics = { 0, value, 0, 0, 0, 0, 0, 0, 0, 0 };
                                StringBuffer comment = new StringBuffer();
                                StringBuffer hoseCodePrediction = new StringBuffer();
                                if (predictbyhose) {
                                    double[] statisticshose = AtomUtils.predictRange(mol, mol.getAtom(l), calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, ((Integer) session.getAttribute("choosennumber")).intValue(), true, hoseCodePrediction, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                                    comments[l].append("; HOSE code: " + myFormatter.format(statisticshose[1]) + " with code " + hoseCodePrediction);
                                } else {
                                    alreadyDone[mothernumber] = true;
                                }
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).statistics = statistics;
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setCurrentSelectedNumber(new Integer(mothernumber + 1));
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setDelta(comments[l].toString());
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setIntensity(hoseCodePrediction.toString());
                                if (session.getAttribute("applet") != null && session.getAttribute("applet").equals("applet")) {
                                    spectrumForApplet.append("S(0," + protoncount + "," + (mol.getAtomNumber(heavyatom) + 1) + ")=" + valuestr + "|");
                                } else {
                                    spectrumForApplet.append((valuestr) + ";" + 0 + ";" + l + "|");
                                }
                                tooltips.append((mol.getAtomNumber(heavyatom) + 1) + "|" + valuestr + "|");
                            } else {
                                Vector<Float> ev = new Vector<Float>();
                                for (Iterator<Float> e = otmt.get(hoseCode).iterator(); e.hasNext(); ) {
                                    ev.add(e.next());
                                }
                                double[] values = new double[ev.size()];
                                for (int m = 0; m < ev.size(); m++) {
                                    values[m] = ev.get(m).doubleValue();
                                }
                                double value = Statistics.average(values);
                                String valuestr = myFormatter.format(value);
                                elementsOfChoosenType.add(new ValuesForVelocityBean(new Float(value), mothernumber + "", 0, 1000, "", new String("" + (mothernumber + 1) + "-H (H " + (l + 1) + ")")));
                                double[] statistics = { 0, value, 0, 0, 0, 0, 0, 0, 0, 0 };
                                StringBuffer comment = new StringBuffer();
                                StringBuffer hoseCodePrediction = new StringBuffer();
                                if (predictbyhose) {
                                    double[] statisticshose = AtomUtils.predictRange(mol, mol.getAtom(l), calculated, measured, null, comment, true, false, null, predictionValuesForAppletMap, ((Integer) session.getAttribute("choosennumber")).intValue(), true, hoseCodePrediction, ((Boolean) session.getAttribute("trueonly")).booleanValue());
                                    comments[l].append("; HOSE code: " + myFormatter.format(statisticshose[1]) + " with code " + hoseCodePrediction);
                                }
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).statistics = statistics;
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setCurrentSelectedNumber(new Integer(mothernumber + 1));
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setDelta(comments[l].toString());
                                ((ValuesForVelocityBean) elementsOfChoosenType.get(elementsOfChoosenType.size() - 1)).setIntensity(hoseCodePrediction.toString());
                                if (session.getAttribute("applet") != null && session.getAttribute("applet").equals("applet")) {
                                    spectrumForApplet.append("S(0," + protoncount + "," + (mol.getAtomNumber(heavyatom) + 1) + ")=" + valuestr + "|");
                                } else {
                                    spectrumForApplet.append((valuestr) + ";" + 0 + ";" + l + "|");
                                }
                                tooltips.append((mol.getAtomNumber(heavyatom) + 1) + "|" + valuestr + "|");
                            }
                        }
                    }
                }
                context.put("coords", "3d");
            }
            context.put("spectrumtype", predictAtomType.equals("Hsvm") ? "1H" : (predictAtomType.equals("All") ? "All" : DBSpectrumTypePeer.retrieveByPK(new NumberKey((String) session.getAttribute("spectype"))).getName()));
            try {
                String smilesNonChiral = new SmilesGenerator(DefaultChemObjectBuilder.getInstance()).createSMILES(mol);
                Criteria crit = new Criteria();
                crit.add(DBMoleculePeer.SMILES_STRING, smilesNonChiral);
                Vector v = DBMoleculePeer.doSelect(crit);
                if (v.size() > 0) {
                    context.put("linkmessage", ((DBMolecule) v.get(0)).getEasylink(request));
                } else {
                    context.put("linkmessage", "none");
                }
            } catch (Exception ex) {
                context.put("linkmessage", "none");
            }
            context.put("spectype", ((String) session.getAttribute("spectype")).equals("101") ? "1" : session.getAttribute("spectype"));
            if (!predictAtomType.equals("Hsvm")) {
                context.put("method", "hose");
            } else {
                context.put("method", "nn");
            }
        }
        Collections.sort(predictions, new SignalComparator());
        for (int i = 0; i < predictions.size(); i++) {
            ValueTriple it = (ValueTriple) predictions.get(i);
            for (int k = 0; k < it.atoms.size(); k++) {
                if (session.getAttribute("applet") != null && session.getAttribute("applet").equals("applet")) {
                    spectrumForApplet.append("S(0," + it.sortPosition + "," + (((Float) it.atoms.get(k)).intValue() + 1) + (predictAtomType.equals("C") ? "," + it.multiplicityString : "") + ")=" + myFormatter.format(it.value1) + "|");
                } else {
                    spectrumForApplet.append(myFormatter.format(it.value1) + (predictAtomType.equals("C") ? it.multiplicityString : "") + ";" + it.value2 + ";" + ((Float) it.atoms.get(k)).intValue() + "|");
                }
                tooltips.append((((Float) it.atoms.get(k)).intValue() + 1) + "|" + myFormatter.format(it.value1) + "|");
            }
        }
        if (predictAtomType.equals("Hsvm")) {
            Collections.sort(elementsOfChoosenType, new VfvbComparator());
        }
        context.put("spectrumForApplet", spectrumForApplet.toString());
        context.put("tooltips", tooltips.toString());
        StringBuffer predictionValuesForApplet = new StringBuffer();
        Iterator iterator = predictionValuesForAppletMap.keySet().iterator();
        while (iterator.hasNext()) {
            Double thisPrediction = (Double) iterator.next();
            double[] values = (double[]) predictionValuesForAppletMap.get(thisPrediction);
            if (values != null) {
                predictionValuesForApplet.append(myFormatter.format(thisPrediction.doubleValue()) + "|");
                for (int k = 0; k < values.length; k++) {
                    predictionValuesForApplet.append(myFormatter.format(values[k]) + ";");
                }
                predictionValuesForApplet.append("|");
            }
        }
        context.put("predictionValuesForApplet", predictionValuesForApplet);
        context.put("messages", messages);
        context.put("elements", elementsOfChoosenType);
        context.put("horiorverti", session.getAttribute("horiorverti"));
        context.put("typeid", request.getParameter("spectrumType"));
        if (session.getAttribute("spectype") == null) session.setAttribute("spectype", "13C");
        context.put("selectedType", session.getAttribute("spectype"));
        Criteria crit = new Criteria();
        Vector v = DBSpectrumTypePeer.doSelect(crit);
        for (int i = 0; i < v.size(); i++) {
            if (((DBSpectrumType) v.get(i)).getName().equals("1H")) {
                ((DBSpectrumType) v.get(i)).setName("1H (using HOSE code)");
                break;
            }
        }
        v.insertElementAt(new DBSpectrumType(101, "All heavy atoms", 1), 0);
        v.add(new DBSpectrumType(100, "1H (using support vector machine)", 1));
        context.put("types", v);
        if (session.getAttribute("mol") != null) {
            StringWriter sw = new StringWriter();
            StringWriter swh = new StringWriter();
            for (int f = 0; f < mol.getAtomCount(); f++) {
                mol.getAtom(f).setFlag(CDKConstants.ISAROMATIC, false);
            }
            for (int f = 0; f < mol.getElectronContainerCount(); f++) {
                IElectronContainer electronContainer = mol.getElectronContainer(f);
                if (electronContainer instanceof IBond) {
                    electronContainer.setFlag(CDKConstants.ISAROMATIC, false);
                }
            }
            new MDLWriter(sw).writeMolecule((IMolecule) session.getAttribute("mol"));
            new MDLWriter(swh).writeMolecule(mol);
            if (session.getAttribute("applet") != null && session.getAttribute("applet").equals("applet") && !print) {
                File outputFile = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/" + System.currentTimeMillis() + "predict.mol"));
                File outputFileh = new File(ServletUtils.expandRelative(servcon, "/nmrshiftdbhtml/" + System.currentTimeMillis() + "predicth.mol"));
                FileWriter out = new FileWriter(outputFile);
                FileWriter outh = new FileWriter(outputFileh);
                out.write(sw.toString());
                out.close();
                outh.write(swh.toString());
                outh.close();
                context.put(NmrshiftdbConstants.MOLFILE, "/nmrshiftdbhtml/" + outputFile.getName());
                context.put(NmrshiftdbConstants.MOLFILEH, "/nmrshiftdbhtml/" + outputFileh.getName());
            } else {
                String filename = "/nmrshiftdbhtml/" + System.currentTimeMillis() + "predict.jpg";
                String filenameh = "/nmrshiftdbhtml/" + System.currentTimeMillis() + "predicth.jpg";
                GeneralUtils.makeJpg(sw.toString(), filename, servcon, 300, 250, true);
                GeneralUtils.makeJpg(swh.toString(), filenameh, servcon, 300, 250, true);
                context.put("imagefile", filename);
                context.put("imagefileh", filenameh);
                if (input.equals("0")) {
                    filename = "/nmrshiftdbhtml/" + System.currentTimeMillis() + "submitspec.jpg";
                    String spectrum = Export.getSpecSvg(spectrumForApplet.toString(), DBSpectrumTypePeer.retrieveByPK(new NumberKey((String) session.getAttribute("spectype"))).getName(), "", 300, 250, false);
                    TranscoderInput transinput = new TranscoderInput(new StringReader(spectrum));
                    File imagefile = new File(ServletUtils.expandRelative(servcon, filename));
                    OutputStream ostream = new FileOutputStream(imagefile);
                    TranscoderOutput output = new TranscoderOutput(ostream);
                    ImageTranscoder it = new JPEGTranscoder();
                    it.transcode(transinput, output);
                    ostream.flush();
                    ostream.close();
                    context.put("specfile", filename);
                }
            }
            context.put("showoldinput", "true");
        }
        context.put("constants", new NmrshiftdbConstants());
        context.put("session", session);
        context.put("servcon", servcon);
        context.put("request", request);
        return context;
    }
