    public void setImage2DCoordinate(LinkedHashMap<String, AttributeHandler> tableAttributeHandler) throws Exception {
        double det;
        aladin = 0;
        AttributeHandler ah;
        if ((ah = tableAttributeHandler.get("_zimage")) != null && ah.getValue().toString().equalsIgnoreCase("true")) {
            xnpix = Integer.parseInt((tableAttributeHandler.get("_znaxis1")).getValue());
            ynpix = Integer.parseInt((tableAttributeHandler.get("_znaxis2")).getValue());
        } else {
            xnpix = Integer.parseInt((tableAttributeHandler.get("_naxis1")).getValue());
            ynpix = Integer.parseInt((tableAttributeHandler.get("_naxis2")).getValue());
        }
        if (tableAttributeHandler.containsKey("_PLTRAS")) {
            alpha = Double.parseDouble((tableAttributeHandler.get("_pltras")).getValue());
            Dss(tableAttributeHandler);
            return;
        } else {
            Xcen = Double.parseDouble((tableAttributeHandler.get("_crpix1")).getValue());
            Ycen = Double.parseDouble((tableAttributeHandler.get("_crpix2")).getValue());
            alphai = Double.parseDouble((tableAttributeHandler.get("_crval1")).getValue());
            deltai = Double.parseDouble((tableAttributeHandler.get("_crval2")).getValue());
            if (tableAttributeHandler.containsKey("_cd1_1") && tableAttributeHandler.containsKey("_cd1_2") && tableAttributeHandler.containsKey("_cd2_1") && tableAttributeHandler.containsKey("_cd2_2")) {
                CD[0][0] = Double.parseDouble((tableAttributeHandler.get("_cd1_1")).getValue());
                CD[0][1] = Double.parseDouble((tableAttributeHandler.get("_cd1_2")).getValue());
                CD[1][0] = Double.parseDouble((tableAttributeHandler.get("_cd2_1")).getValue());
                CD[1][1] = Double.parseDouble((tableAttributeHandler.get("_cd2_2")).getValue());
                incA = Math.sqrt(CD[0][0] * CD[0][0] + CD[0][1] * CD[0][1]);
                incD = Math.sqrt(CD[1][0] * CD[1][0] + CD[1][1] * CD[1][1]);
                rota = Math.acos(CD[0][0] / incA) * (180. / Math.PI);
            } else {
                incA = Double.parseDouble((tableAttributeHandler.get("_cdelt1")).getValue());
                incD = Double.parseDouble((tableAttributeHandler.get("_cdelt2")).getValue());
                if (tableAttributeHandler.containsKey("_crota1") || tableAttributeHandler.containsKey("_crota2")) {
                    double rota1 = 0, rota2 = 0;
                    if (tableAttributeHandler.containsKey("_crota1") && tableAttributeHandler.containsKey("_crota2")) {
                        rota1 = Double.parseDouble((tableAttributeHandler.get("_crota1")).getValue());
                        rota2 = Double.parseDouble((tableAttributeHandler.get("_crota2")).getValue());
                    } else {
                        if (tableAttributeHandler.containsKey("_crota1")) rota1 = Double.parseDouble((tableAttributeHandler.get("_crota1")).getValue());
                        if (tableAttributeHandler.containsKey("_crota2")) rota2 = Double.parseDouble((tableAttributeHandler.get("_crota2")).getValue());
                    }
                    rota = rota1;
                    if (rota1 == 0) {
                        rota = rota2;
                    } else {
                        if (rota2 != 0) {
                            rota = (rota1 + rota2) / 2;
                        }
                    }
                    CD[0][0] = incA * Math.cos((rota / 180.) * Math.PI);
                    CD[0][1] = -incD * Math.sin((rota / 180.) * Math.PI);
                    CD[1][0] = incA * Math.sin((rota / 180.) * Math.PI);
                    CD[1][1] = incD * Math.cos((rota / 180.) * Math.PI);
                } else {
                    if (tableAttributeHandler.containsKey("_pc001001") && tableAttributeHandler.containsKey("_pc001002") && tableAttributeHandler.containsKey("_pc002001") && tableAttributeHandler.containsKey("_pc002002")) {
                        CD[0][0] = Double.parseDouble((tableAttributeHandler.get("_pc001001")).getValue()) * incA;
                        CD[0][1] = Double.parseDouble((tableAttributeHandler.get("_pc001002")).getValue()) * incA;
                        CD[1][0] = Double.parseDouble((tableAttributeHandler.get("_pc002001")).getValue()) * incD;
                        CD[1][1] = Double.parseDouble((tableAttributeHandler.get("_pc002002")).getValue()) * incD;
                        rota = Math.acos(Double.parseDouble((tableAttributeHandler.get("_pc001001")).getValue())) * (180. / Math.PI);
                    } else {
                        rota = 0.0;
                        CD[0][0] = incA;
                        CD[0][1] = 0;
                        CD[1][0] = 0;
                        CD[1][1] = incD;
                        AttributeHandler origin = (tableAttributeHandler.get("_origin"));
                        if (origin != null && origin.getValue().startsWith("'DeNIS")) {
                            if (CD[0][0] > 0) CD[0][0] = -CD[0][0];
                            if (CD[1][1] > 0) CD[1][1] = -CD[1][1];
                        }
                    }
                }
            }
        }
        if (tableAttributeHandler.containsKey("_epoch")) {
            epoch = Double.parseDouble((tableAttributeHandler.get("_epoch")).getValue());
            flagepoc = 1;
        } else {
            epoch = 0.0;
        }
        if (tableAttributeHandler.containsKey("_equinox")) {
            equinox = Double.parseDouble((tableAttributeHandler.get("_equinox")).getValue());
        } else {
            if (tableAttributeHandler.containsKey("_epoch")) {
                equinox = Double.parseDouble((tableAttributeHandler.get("_epoch")).getValue());
                epoch = equinox;
                flagepoc = 1;
            } else {
                equinox = 2000.0;
                epoch = 2000.0;
                flagepoc = 0;
            }
        }
        type1 = (tableAttributeHandler.get("_ctype1")).getValue();
        type2 = (tableAttributeHandler.get("_ctype1")).getValue();
        if (type1.startsWith("'DEC")) {
            double tmp_invert = deltai;
            deltai = alphai;
            alphai = tmp_invert;
            tmp_invert = CD[0][0];
            CD[0][0] = CD[1][0];
            CD[1][0] = tmp_invert;
            tmp_invert = CD[1][1];
            CD[1][1] = CD[0][1];
            CD[0][1] = tmp_invert;
            tmp_invert = incA;
            incA = incD;
            incD = tmp_invert;
        }
        if (type1.startsWith("'ELON")) {
            system = 4;
        }
        if (type1.startsWith("'GLON")) {
            system = 2;
        }
        if (type1.startsWith("'SLON")) {
            system = 3;
        }
        if (equinox == 1950.0) {
            system = 1;
        }
        if (tableAttributeHandler.containsKey("_radecsys")) {
            String Syst = (tableAttributeHandler.get("_radecsys")).getValue();
            if (Syst.indexOf("ICRS") >= 0) system = 6; else if (Syst.indexOf("FK5") >= 0) system = 5; else if (Syst.indexOf("FK4") >= 0) system = 1;
        }
        proj = 0;
        if (type1.indexOf("SIN") >= 0) proj = 1; else if (type1.indexOf("TAN") >= 0) proj = 2; else if (type1.indexOf("COE") >= 0) proj = 2; else if (type1.indexOf("ARC") >= 0) proj = 3; else if (type1.indexOf("AIT") >= 0) proj = 4; else if (type1.indexOf("ZEA") >= 0) proj = 5; else if (type1.indexOf("STG") >= 0) proj = 6; else if (type1.indexOf("CAR") >= 0) proj = 7; else if (type1.indexOf("NCP") >= 0) proj = 8; else if (type1.indexOf("ZPN") >= 0) proj = 9;
        if (proj == 0) {
            Messenger.printMsg(Messenger.WARNING, "WCS CTYPE <" + type1 + "> has no specified projection: Take TAN projection by default");
        }
        widtha = xnpix * Math.abs(incA);
        widthd = ynpix * Math.abs(incD);
        cdelz = Math.cos(deltai * deg_to_rad);
        sdelz = Math.sin(deltai * deg_to_rad);
        det = CD[0][0] * CD[1][1] - CD[0][1] * CD[1][0];
        ID[0][0] = CD[1][1] / det;
        ID[0][1] = -CD[0][1] / det;
        ID[1][0] = -CD[1][0] / det;
        ID[1][1] = CD[0][0] / det;
    }
