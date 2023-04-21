    public int printProverResults(int proverRetCode) {
        OGPParameters parameters = OpenGeoProver.settings.getParameters();
        OGPOutput output = OpenGeoProver.settings.getOutput();
        FileLogger logger = OpenGeoProver.settings.getLogger();
        Stopwatch stopwatch = OpenGeoProver.settings.getStopwacth();
        int retCode = OGPConstants.RET_CODE_SUCCESS;
        if (parameters.createReport()) {
            try {
                output.openSection("Prover results");
                output.openEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
                output.openItemWithDesc("Status:");
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
        }
        String statusText;
        switch(proverRetCode) {
            case TheoremProver.THEO_PROVE_RET_CODE_FALSE:
                statusText = "Theorem has been disproved.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case TheoremProver.THEO_PROVE_RET_CODE_TRUE:
                statusText = "Theorem has been proved.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case TheoremProver.THEO_PROVE_RET_CODE_UNKNOWN:
                statusText = "Theorem can't be neither proved nor disproved.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case OGPConstants.ERR_CODE_GENERAL:
                statusText = "Proving failed - general error occurred.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    boolean exceptionCaught = false;
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        exceptionCaught = true;
                    } finally {
                        output.close();
                        if (exceptionCaught) return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case OGPConstants.ERR_CODE_NULL:
                statusText = "Proving failed - Found null object when expected non-null.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    boolean exceptionCaught = false;
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        exceptionCaught = true;
                    } finally {
                        output.close();
                        if (exceptionCaught) return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case OGPConstants.ERR_CODE_SPACE:
                statusText = "Proving failed - Space limit has been reached.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
            case OGPConstants.ERR_CODE_TIME:
                statusText = "Proving failed - Time for prover execution has been expired.";
                System.out.println(statusText);
                if (parameters.createReport()) {
                    try {
                        output.closeItemWithDesc(statusText);
                    } catch (IOException e) {
                        logger.error("Failed to write to output file(s).");
                        output.close();
                        return OGPConstants.ERR_CODE_GENERAL;
                    }
                }
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Time spent by the prover is ");
        sb.append(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()));
        sb.append(" seconds.");
        String timeReportSec = sb.toString();
        sb = new StringBuilder();
        sb.append("Time spent by the prover is ");
        sb.append(stopwatch.getTimeIntMillisec());
        sb.append(" milliseconds.");
        String timeReportMiliSec = sb.toString();
        sb = new StringBuilder();
        sb.append("The biggest polynomial obtained during prover execution contains ");
        sb.append(OpenGeoProver.settings.getMaxNumOfTerms());
        sb.append(" terms.");
        String spaceReport = sb.toString();
        System.out.println(timeReportMiliSec);
        System.out.println();
        System.out.println();
        System.out.println(spaceReport);
        System.out.println();
        System.out.println();
        if (parameters.createReport()) {
            try {
                output.openItemWithDesc("Space Complexity:");
                output.closeItemWithDesc(spaceReport);
                output.openItemWithDesc("Time Complexity:");
                output.closeItemWithDesc(timeReportSec);
                output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_DESCRIPTION);
                output.closeSection();
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
        }
        if (proverRetCode != TheoremProver.THEO_PROVE_RET_CODE_FALSE && proverRetCode != TheoremProver.THEO_PROVE_RET_CODE_TRUE) {
            try {
                output.closeDocument();
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
            return retCode;
        }
        stopwatch.startMeasureTime();
        if (parameters.createReport()) {
            try {
                output.openSection("NDG Conditions");
                output.openSubSection("NDG Conditions in readable form", false);
                output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
        }
        retCode = this.consProtocol.translateNDGConditionsToUserReadableForm();
        if (retCode != OGPConstants.RET_CODE_SUCCESS) {
            boolean exceptionCaught = false;
            try {
                output.openItem();
                output.writePlainText("Failed to translate NDG Conditions to readable form");
                output.closeItem();
                output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
                output.closeSubSection();
                output.closeSection();
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                exceptionCaught = true;
            } finally {
                output.close();
                if (exceptionCaught) retCode = OGPConstants.ERR_CODE_GENERAL;
            }
            return retCode;
        }
        if (this.consProtocol.getNdgConditions() == null) {
            boolean exceptionCaught = false;
            try {
                output.openItem();
                output.writePlainText("There are no NDG conditions for this theorem");
                output.closeItem();
                output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
                output.closeSubSection();
                output.closeSection();
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                exceptionCaught = true;
            } finally {
                output.close();
                if (exceptionCaught) retCode = OGPConstants.ERR_CODE_GENERAL;
            }
            return retCode;
        }
        for (NDGCondition ndgc : this.consProtocol.getNdgConditions()) {
            String ndgcText = ndgc.getBestDescription();
            try {
                if (ndgcText == null || ndgcText.length() == 0) {
                    output.openItem();
                    output.writePolynomial(ndgc.getPolynomial());
                    output.closeItem();
                } else {
                    output.openItem();
                    output.writePlainText(ndgcText);
                    output.closeItem();
                }
            } catch (IOException e) {
                logger.error("Failed to write to output file(s).");
                output.close();
                return OGPConstants.ERR_CODE_GENERAL;
            }
        }
        stopwatch.endMeasureTime();
        try {
            output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
            output.closeSubSection();
            output.openSubSection("Time spent for processing NDG Conditions", false);
            output.openEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
            output.openItem();
            output.writePlainText(OGPUtilities.roundUpToPrecision(stopwatch.getTimeIntSec()) + " seconds");
            output.closeItem();
            output.closeEnum(SpecialFileFormatting.ENUM_COMMAND_ITEMIZE);
            output.closeSubSection();
            output.closeSection();
            output.closeDocument();
        } catch (IOException e) {
            logger.error("Failed to write to output file(s).");
            output.close();
            return OGPConstants.ERR_CODE_GENERAL;
        }
        return retCode;
    }
