    public int triangulate() {
        StringBuilder sb;
        OGPOutput output = OpenGeoProver.settings.getOutput();
        FileLogger logger = OpenGeoProver.settings.getLogger();
        if (this.checkAndReOrderTriangularSystem() == true) {
            try {
                output.writePlainText("The system is already triangular.\n\n");
                output.writePolySystem(this);
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
            return OGPConstants.RET_CODE_SUCCESS;
        }
        this.variableList = new Vector<Integer>();
        Vector<XPolynomial> triangularSystem = new Vector<XPolynomial>();
        Vector<XPolynomial> freeSystem = null;
        Vector<XPolynomial> notFreeSystem = null;
        Vector<XPolynomial> auxSystem = this.polynomials;
        Vector<XPolynomial> tempSystemForOutput = null;
        XPolySystem tempPolySystem = null;
        Vector<Integer> originalIndexes = null;
        boolean tempSystemChanged = true;
        try {
            output.writePlainText("The input system is:\n\n");
            output.writePolySystem(this);
        } catch (IOException e) {
            logger.error("Failed to write to output file(s).");
            output.close();
            return OGPConstants.ERR_CODE_GENERAL;
        }
        for (int ii = this.polynomials.size(), istep = 1, isize = this.polynomials.size(); ii > 0; ii--, istep++) {
            try {
                output.openSubSection("Triangulation, step " + istep, true);
                output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
                output.openItemWithDesc("Choosing variable:");
                sb = new StringBuilder();
                sb.append("Trying the variable with index ");
                sb.append(ii);
                sb.append(".\n\n");
                output.closeItemWithDesc(sb.toString());
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
            freeSystem = new Vector<XPolynomial>();
            notFreeSystem = new Vector<XPolynomial>();
            originalIndexes = new Vector<Integer>();
            tempSystemChanged = true;
            for (int jj = 0, kk = auxSystem.size(); jj < kk; jj++) {
                XPolynomial currXPoly = auxSystem.get(jj);
                int varExp = 0;
                ArrayList<Term> termList = currXPoly.getTermsAsDescList();
                boolean allProcessed = false;
                int numOfTerms = termList.size();
                int counter = 0;
                boolean found = false;
                while (counter < numOfTerms && !allProcessed && !found) {
                    XTerm currTerm = (XTerm) termList.get(counter);
                    if (currTerm == null) {
                        logger.error("Found null term");
                        return OGPConstants.ERR_CODE_NULL;
                    }
                    varExp = currTerm.getVariableExponent(ii);
                    if (varExp > 0) found = true; else if (currTerm.getPowers().size() == 0 || currTerm.getPowers().get(0).getIndex() < ii) allProcessed = true;
                    counter++;
                }
                if (found) {
                    notFreeSystem.add((XPolynomial) currXPoly.clone());
                    originalIndexes.add(new Integer(jj));
                } else freeSystem.add((XPolynomial) currXPoly.clone());
            }
            if (notFreeSystem.size() == 0) {
                logger.error("Variable with index " + ii + " not found in polynomial system.");
                return OGPConstants.ERR_CODE_GENERAL;
            }
            try {
                sb = new StringBuilder();
                sb.append("Variable <ind_text><label>x</label><ind>");
                sb.append(ii);
                sb.append("</ind></ind_text> selected:");
                output.openItemWithDesc(sb.toString());
                sb = new StringBuilder();
                sb.append("The number of polynomials with this variable, with indexes from 1 to ");
                sb.append(isize - istep + 1);
                sb.append(", is ");
                sb.append(notFreeSystem.size());
                sb.append(".\n\n");
                output.closeItemWithDesc(sb.toString());
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
            if (notFreeSystem.size() == 1) {
                triangularSystem.add(0, notFreeSystem.get(0));
                this.variableList.add(0, new Integer(ii));
                auxSystem = freeSystem;
                tempSystemChanged = false;
                try {
                    output.openItemWithDesc("Single polynomial with chosen variable:");
                    sb = new StringBuilder();
                    sb.append("Chosen polynomial is <ind_text><label>p</label><ind>");
                    sb.append(originalIndexes.get(0).intValue() + 1);
                    sb.append("</ind></ind_text>. No reduction needed.\n\n");
                    output.closeItemWithDesc(sb.toString());
                    output.writeEnumItem("The triangular system has not been changed.\n\n");
                } catch (IOException e) {
                    logger.error("Failed to write to output file(s).");
                    output.close();
                    return OGPConstants.ERR_CODE_GENERAL;
                }
            } else {
                boolean end = false;
                do {
                    int first = 0, second = 1;
                    int exp1 = notFreeSystem.get(first).getLeadingExp(ii), exp2 = notFreeSystem.get(second).getLeadingExp(ii);
                    int min1, min2, count1 = 1, count2 = 1;
                    if (exp1 == 0 || exp2 == 0) {
                        logger.error("Variable not found when expected to be found.");
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                    if (exp1 <= exp2) {
                        min1 = exp1;
                        min2 = exp2;
                    } else {
                        first = 1;
                        second = 0;
                        min1 = exp2;
                        min2 = exp1;
                    }
                    for (int ll = 2, mm = notFreeSystem.size(); ll < mm; ll++) {
                        int currExp = notFreeSystem.get(ll).getLeadingExp(ii);
                        if (currExp == 0) {
                            logger.error("Variable not found when expected to be found.");
                            return OGPConstants.ERR_CODE_GENERAL;
                        }
                        if (currExp < min1) {
                            first = ll;
                            min1 = currExp;
                            count1 = 1;
                        } else if (currExp == min1) {
                            count1++;
                        } else if (currExp < min2) {
                            second = ll;
                            min2 = currExp;
                            count2 = 1;
                        } else if (currExp == min2) {
                            count2++;
                        }
                    }
                    try {
                        output.openItemWithDesc("Minimal degrees:");
                        sb = new StringBuilder();
                        if (min1 < min2) {
                            sb.append(count1);
                            sb.append(" polynomial(s) with degree ");
                            sb.append(min1);
                            sb.append(" and ");
                            sb.append(count2);
                            sb.append(" polynomial(s) with degree ");
                            sb.append(min2);
                        } else if (min1 == min2) {
                            sb.append(count1 + count2);
                            sb.append(" polynomial(s) with degree ");
                            sb.append(min1);
                        }
                        sb.append(".\n\n");
                        output.closeItemWithDesc(sb.toString());
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                    if (min1 == 1) {
                        try {
                            output.openItemWithDesc("Polynomial with linear degree:");
                            sb = new StringBuilder();
                            sb.append("Removing variable <ind_text><label>x</label><ind>");
                            sb.append(ii);
                            sb.append("</ind></ind_text> from all other polynomials by reducing them with polynomial <ind_text><label>p</label><ind>");
                            sb.append(originalIndexes.get(first).intValue() + 1);
                            sb.append("</ind></ind_text> from previous step.\n\n");
                            output.closeItemWithDesc(sb.toString());
                        } catch (IOException e) {
                            logger.error("Failed to write to output file(s).");
                            output.close();
                            return OGPConstants.ERR_CODE_GENERAL;
                        }
                        XPolynomial currPoly = notFreeSystem.get(first);
                        triangularSystem.add(0, currPoly);
                        this.variableList.add(0, new Integer(ii));
                        notFreeSystem.remove(first);
                        for (int ll = 0, mm = notFreeSystem.size(); ll < mm; ll++) {
                            XPolynomial tempXP = notFreeSystem.get(ll).pseudoReminder(currPoly, ii);
                            if (tempXP == null) return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
                            int numOfTerms = tempXP.getTerms().size();
                            if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
                                logger.error("Polynomial exceeds maximal allowed number of terms.");
                                return OGPConstants.ERR_CODE_SPACE;
                            }
                            if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
                                OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
                            }
                            if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
                                logger.error("Prover execution time has been expired.");
                                return OGPConstants.ERR_CODE_TIME;
                            }
                            freeSystem.add(tempXP);
                        }
                        auxSystem = freeSystem;
                        end = true;
                    } else {
                        XPolynomial r2 = notFreeSystem.get(second);
                        XPolynomial r1 = notFreeSystem.get(first);
                        int leadExp = 0;
                        try {
                            output.openItemWithDesc("No linear degree polynomials:");
                            sb = new StringBuilder();
                            sb.append("Reducing polynomial <ind_text><label>p</label><ind>");
                            sb.append(second + 1);
                            sb.append("</ind></ind_text> (of degree ");
                            sb.append(count2);
                            sb.append(") with <ind_text><label>p</label><ind>");
                            sb.append(first + 1);
                            sb.append("</ind></ind_text> (of degree ");
                            sb.append(count1);
                            sb.append(").\n\n");
                            output.closeItemWithDesc(sb.toString());
                        } catch (IOException e) {
                            logger.error("Failed to write to output file(s).");
                            output.close();
                            return OGPConstants.ERR_CODE_GENERAL;
                        }
                        do {
                            XPolynomial temp = r2.pseudoReminder(r1, ii);
                            if (temp == null) return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
                            int numOfTerms = temp.getTerms().size();
                            if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
                                logger.error("Polynomial exceeds maximal allowed number of terms.");
                                return OGPConstants.ERR_CODE_SPACE;
                            }
                            if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
                                OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
                            }
                            if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
                                logger.error("Prover execution time has been expired.");
                                return OGPConstants.ERR_CODE_TIME;
                            }
                            r2 = r1;
                            r1 = temp;
                            if (r1.isZero()) {
                                logger.error("Two polynomials have common factor.");
                                return OGPConstants.ERR_CODE_GENERAL;
                            }
                            leadExp = r1.getLeadingExp(ii);
                        } while (leadExp > 1);
                        notFreeSystem.set(first, r1);
                        notFreeSystem.set(second, r2);
                        if (leadExp == 0) {
                            freeSystem.add(r1);
                            notFreeSystem.remove(first);
                            if (notFreeSystem.size() == 1) {
                                triangularSystem.add(0, r2);
                                this.variableList.add(0, new Integer(ii));
                                auxSystem = freeSystem;
                                end = true;
                            }
                        } else {
                            triangularSystem.add(0, r1);
                            this.variableList.add(0, new Integer(ii));
                            notFreeSystem.remove(first);
                            for (int ll = 0, mm = notFreeSystem.size(); ll < mm; ll++) {
                                XPolynomial tempXP = notFreeSystem.get(ll).pseudoReminder(r1, ii);
                                if (tempXP == null) return OpenGeoProver.settings.getRetCodeOfPseudoDivision();
                                int numOfTerms = tempXP.getTerms().size();
                                if (numOfTerms > OpenGeoProver.settings.getParameters().getSpaceLimit()) {
                                    logger.error("Polynomial exceeds maximal allowed number of terms.");
                                    return OGPConstants.ERR_CODE_SPACE;
                                }
                                if (numOfTerms > OpenGeoProver.settings.getMaxNumOfTerms()) {
                                    OpenGeoProver.settings.setMaxNumOfTerms(numOfTerms);
                                }
                                if (OpenGeoProver.settings.getTimer().isTimeIsUp()) {
                                    logger.error("Prover execution time has been expired.");
                                    return OGPConstants.ERR_CODE_TIME;
                                }
                                freeSystem.add(tempXP);
                            }
                            auxSystem = freeSystem;
                            end = true;
                        }
                    }
                } while (!end);
            }
            tempSystemForOutput = new Vector<XPolynomial>();
            for (XPolynomial xp : auxSystem) tempSystemForOutput.add(xp);
            for (XPolynomial xp : triangularSystem) tempSystemForOutput.add(xp);
            tempPolySystem = new XPolySystem();
            tempPolySystem.setPolynomials(tempSystemForOutput);
            try {
                output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
                if (tempSystemChanged) {
                    output.writePlainText("Finished a triangulation step, the current system is:\n\n");
                    output.writePolySystem(tempPolySystem);
                }
                output.closeSubSection();
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
        }
        this.polynomials = triangularSystem;
        try {
            output.writePlainText("\n\nThe triangular system is:\n\n");
            output.writePolySystem(this);
        } catch (IOException e) {
            logger.error("Failed to write to output file(s).");
            output.close();
            return OGPConstants.ERR_CODE_GENERAL;
        }
        return OGPConstants.RET_CODE_SUCCESS;
    }
