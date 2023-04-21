    private void fetchFromHiski() {
        book.setText("");
        srk.setText("");
        srkNo.setText("");
        eventId = null;
        eventFirstType.setText("");
        eventFirstDate.setText("");
        eventLastType.setText("");
        eventLastDate.setText("");
        bookName = null;
        pvm1Name = null;
        pvm2Name = null;
        personCount = 0;
        eventFrom.setText("");
        eventTo.setText("");
        eventVillage.setText("");
        eventFarm.setText("");
        eventReason.setText("");
        eventUserComment.setText("");
        eventOrigComment.setText("");
        eventNote.setText("");
        for (int i = 0; i < pNumero.length; i++) {
            remove(pSukuPid[i]);
            remove(pSukuName[i]);
            remove(pNumero[i]);
            remove(pType[i]);
            remove(pSex[i]);
            remove(rOccu[i]);
            remove(rGivenname[i]);
            remove(rPatronym[i]);
            remove(rSurname[i]);
            remove(pOccu[i]);
            remove(pGivenname[i]);
            remove(pPatronym[i]);
            remove(pSurname[i]);
            remove(pAgeVillage[i]);
            remove(pReasonFarm[i]);
        }
        pNumero = new JLabel[0];
        pSukuPid = new JLabel[0];
        pSukuName = new JLabel[0];
        pTypeName = new String[0];
        pType = new JLabel[0];
        pSex = new JComboBox[0];
        pOccu = new JTextField[0];
        pGivenname = new JTextField[0];
        pPatronym = new JTextField[0];
        pSurname = new JTextField[0];
        pAgeVillage = new JTextField[0];
        pReasonFarm = new JTextField[0];
        StringBuilder sb = new StringBuilder();
        sb.append("http://hiski.genealogia.fi/");
        String requri;
        int resu;
        SukuPopupMenu pop = SukuPopupMenu.getInstance();
        for (int i = 0; i < 3; i++) {
            pop.enableHiskiPerson(i, false, null);
        }
        sb.append("hiski?fi+t");
        String hiskiNumStr = this.hiskiNumber.getText().trim();
        int hiskiNum = 0;
        Document doc = null;
        try {
            hiskiNum = Integer.parseInt(hiskiNumStr);
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(this, "'" + hiskiNumStr + "'" + Resurses.getString("NOT_NUMBER"), Resurses.getString("HISKI_NUMBER"), JOptionPane.WARNING_MESSAGE);
            return;
        }
        sb.append(hiskiNum);
        hiskiBrowserUrl = sb.toString();
        sb.append("+xml");
        requri = sb.toString();
        try {
            URL url = new URL(requri);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            resu = uc.getResponseCode();
            if (resu == 200) {
                InputStream in = uc.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(in);
                try {
                    doc = this.bld.parse(bis);
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SukuException(e);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString());
            logger.log(Level.WARNING, "hiski", e);
            return;
        }
        Element docEle = doc.getDocumentElement();
        Element ele;
        NodeList nl = docEle.getElementsByTagName("tapahtuma");
        if (nl.getLength() != 1) {
            logger.warning("tapahtuma count = " + nl.getLength());
        }
        if (nl.getLength() > 0) {
            Element tap = (Element) nl.item(0);
            try {
                bookName = tap.getAttribute("kirja");
                book.setText(Resurses.getString(bookName));
            } catch (MissingResourceException mre) {
                book.setText(bookName);
            }
            eventVillage.setEnabled(false);
            eventFarm.setEnabled(false);
            eventFrom.setEnabled(false);
            eventTo.setEnabled(false);
            eventReason.setEnabled(false);
            if (bookName.equals("kastetut")) {
                eventVillage.setEnabled(true);
                eventFarm.setEnabled(true);
                eventReason.setEnabled(true);
            } else if (bookName.equals("vihityt")) {
                eventReason.setEnabled(true);
            } else if (bookName.equals("haudatut")) {
                eventVillage.setEnabled(true);
                eventFarm.setEnabled(true);
            } else {
                eventVillage.setEnabled(true);
                eventFarm.setEnabled(true);
                eventFrom.setEnabled(true);
                eventTo.setEnabled(true);
                eventReason.setEnabled(true);
            }
            NodeList henkNodes = docEle.getElementsByTagName("henkilo");
            personCount = henkNodes.getLength();
            initHiskiPersons(personCount);
            String vv = null;
            String kk = null;
            String vk = null;
            String pv = null;
            for (int pidx = 0; pidx < personCount; pidx++) {
                ele = (Element) henkNodes.item(pidx);
                pNumero[pidx].setText("" + pidx);
                String theType = ele.getAttribute("tyyppi");
                pType[pidx].setText(theType);
                boolean showMenu = true;
                if ("isa".equals(theType)) {
                    pSex[pidx].setSelectedIndex(1);
                    pSex[pidx].setEnabled(false);
                } else if ("aiti".equals(theType)) {
                    pSex[pidx].setSelectedIndex(2);
                    pSex[pidx].setEnabled(false);
                } else if ("mies".equals(theType)) {
                    pSex[pidx].setSelectedIndex(1);
                    pSex[pidx].setEnabled(false);
                } else if ("vaimo".equals(theType)) {
                    pSex[pidx].setSelectedIndex(2);
                    pSex[pidx].setEnabled(false);
                } else if ("omainen".equals(theType)) {
                    pSex[pidx].setVisible(false);
                    showMenu = false;
                }
                NodeList nlh = ele.getChildNodes();
                Element elp;
                StringBuilder muut = new StringBuilder();
                for (int j = 0; j < nlh.getLength(); j++) {
                    if (nlh.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        elp = (Element) nlh.item(j);
                        String elpNam = elp.getNodeName();
                        if (elpNam == null) {
                        } else if (elpNam.equals("kyla")) {
                            pAgeVillage[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("talo")) {
                            pReasonFarm[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("lisat")) {
                        } else if (elpNam.equals("ammatti")) {
                            rOccu[pidx].setText(elp.getTextContent());
                            pOccu[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("etunimi")) {
                            rGivenname[pidx].setText(elp.getTextContent());
                            pGivenname[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("patronyymi")) {
                            rPatronym[pidx].setText(elp.getTextContent());
                            pPatronym[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("sukunimi")) {
                            rSurname[pidx].setText(elp.getTextContent());
                            pSurname[pidx].setText(elp.getTextContent());
                        } else if (elpNam.equals("ika")) {
                            StringBuilder age = new StringBuilder();
                            vv = elp.getAttribute("vv");
                            if (!vv.isEmpty()) {
                                age.append("vv=" + vv + ";");
                            }
                            kk = elp.getAttribute("kk");
                            if (!kk.isEmpty()) {
                                age.append("kk=" + kk + ";");
                            }
                            vk = elp.getAttribute("vk");
                            if (!vk.isEmpty()) {
                                age.append("vk=" + vk + ";");
                            }
                            pv = elp.getAttribute("pv");
                            if (!pv.isEmpty()) {
                                age.append("pv=" + pv);
                            }
                            if (age.length() > 0) {
                                pAgeVillage[pidx].setText(age.toString());
                            }
                        } else {
                            if (muut.length() > 0) {
                                muut.append(";");
                            }
                            muut.append(elp.getTextContent());
                        }
                    }
                }
                if (showMenu) {
                    pop.enableHiskiPerson(pidx, true, theType);
                }
            }
            NodeList taplist = tap.getChildNodes();
            int pvmno = 0;
            StringBuilder remark = new StringBuilder();
            for (int i = 0; i < taplist.getLength(); i++) {
                if (taplist.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    ele = (Element) taplist.item(i);
                    String eleName = ele.getNodeName();
                    if (eleName == null) {
                    } else if (eleName.equals("srk")) {
                        srkNo.setText(ele.getAttribute("nro"));
                        String ll = Resurses.getLanguage();
                        String tmp = ele.getTextContent();
                        int llidx = tmp.indexOf("-");
                        if (llidx > 0) {
                            if (ll.equals("sv") && llidx < tmp.length() + 1) {
                                tmp = tmp.substring(llidx + 1).trim();
                            } else {
                                tmp = tmp.substring(0, llidx).trim();
                            }
                        }
                        srk.setText(tmp);
                    } else if (eleName.equals("tapahtumatunniste")) {
                        eventId = ele.getAttribute("id");
                    } else if (eleName.equals("pvm")) {
                        pvmno++;
                        String datex = null;
                        if (pvmno == 1) {
                            try {
                                pvm1Name = ele.getAttribute("tyyppi");
                                eventFirstType.setText(Resurses.getString(pvm1Name));
                            } catch (MissingResourceException mre) {
                                eventFirstType.setText(pvm1Name);
                            }
                            datex = ele.getTextContent();
                            eventFirstDate.setText(datex);
                        } else {
                            try {
                                pvm2Name = ele.getAttribute("tyyppi");
                                eventLastType.setText(Resurses.getString(pvm2Name));
                            } catch (MissingResourceException mre) {
                                eventLastType.setText(pvm2Name);
                            }
                            datex = ele.getTextContent();
                            eventLastDate.setText(datex);
                        }
                        if ("haudatut".equals(bookName) && ((vv + kk + vk + pv).length() > 0)) {
                            String aux = toBirthDate(datex, vv, kk, vk, pv);
                            if (aux != null && !aux.isEmpty()) {
                                eventExtraType.setVisible(true);
                                eventExtraDate.setVisible(true);
                                eventExtraDate.setDate("CAL", aux, "");
                            }
                        } else {
                            eventExtraType.setVisible(false);
                            eventExtraDate.setVisible(false);
                        }
                    } else if (eleName.equals("kyla")) {
                        eventVillage.setText(ele.getTextContent());
                    } else if (eleName.equals("talo")) {
                        eventFarm.setText(ele.getTextContent());
                    } else if (eleName.equals("mista")) {
                        eventFrom.setText(ele.getTextContent());
                    } else if (eleName.equals("minne")) {
                        eventTo.setText(ele.getTextContent());
                    } else if (eleName.equals("henkilo")) {
                    } else if (eleName.equals("kuolinsyy")) {
                        eventReason.setText(ele.getTextContent());
                    } else if (eleName.equals("oma_kommentti")) {
                        eventUserComment.setText(ele.getTextContent());
                    } else if (eleName.equals("alkup_kommentti")) {
                        eventOrigComment.setText(ele.getTextContent());
                    } else {
                        remark.append(ele.getTextContent());
                        eventNote.setText(remark.toString());
                    }
                }
            }
        }
    }
