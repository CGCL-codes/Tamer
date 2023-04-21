    public static void convertMetadata(MetadataRetrieve src, MetadataStore dest) {
        Integer ii = null;
        int globalPixCount = 0;
        for (int i = 0; i < src.getImageCount(); i++) {
            ii = new Integer(i);
            dest.setImage(src.getImageName(ii), src.getCreationDate(ii), src.getDescription(ii), ii);
            dest.setDimensions(src.getPixelSizeX(ii), src.getPixelSizeY(ii), src.getPixelSizeZ(ii), src.getPixelSizeC(ii), src.getPixelSizeT(ii), ii);
            for (int j = 0; j < src.getPixelsCount(ii); j++) {
                Integer p = new Integer(j);
                dest.setPixels(src.getSizeX(ii), src.getSizeY(ii), src.getSizeZ(ii), src.getSizeC(ii), src.getSizeT(ii), new Integer(FormatTools.pixelTypeFromString(src.getPixelType(ii))), src.getBigEndian(ii), src.getDimensionOrder(ii), ii, p);
                dest.setDisplayOptions(src.getZoom(ii), src.isRedChannelOn(ii), src.isGreenChannelOn(ii), src.isBlueChannelOn(ii), src.isDisplayRGB(ii), src.getColorMap(ii), src.getZStart(ii), src.getZStop(ii), src.getTStart(ii), src.getTStop(ii), ii, p, new Integer(0), new Integer(1), new Integer(2), new Integer(0));
                Integer globalPix = new Integer(globalPixCount);
                for (int ch = 0; ch < src.getChannelCount(globalPix); ch++) {
                    Integer c = new Integer(ch);
                    dest.setLogicalChannel(ch, src.getChannelName(globalPix, c), null, null, null, null, null, null, null, null, null, null, null, src.getPhotometricInterpretation(globalPix, c), src.getMode(globalPix, c), null, null, null, null, null, src.getEmWave(globalPix, c), src.getExWave(globalPix, c), null, src.getChannelNDFilter(globalPix, c), globalPix);
                    dest.setChannelGlobalMinMax(ch, src.getGlobalMin(globalPix, c), src.getGlobalMax(globalPix, c), globalPix);
                    dest.setDisplayChannel(c, src.getBlackLevel(globalPix, c), src.getWhiteLevel(globalPix, c), src.getGamma(globalPix, c), globalPix);
                }
                globalPixCount++;
            }
            dest.setImagingEnvironment(src.getTemperature(ii), src.getAirPressure(ii), src.getHumidity(ii), src.getCO2Percent(ii), ii);
        }
        for (int i = 0; i < src.getExperimenterCount(); i++) {
            ii = new Integer(i);
            dest.setExperimenter(src.getFirstName(ii), src.getLastName(ii), src.getEmail(ii), src.getInstitution(ii), src.getDataDirectory(ii), src.getGroup(ii), ii);
        }
        for (int i = 0; i < src.getGroupCount(); i++) {
            ii = new Integer(i);
            dest.setGroup(src.getGroupName(ii), src.getLeader(ii), src.getContact(ii), ii);
        }
        for (int i = 0; i < src.getInstrumentCount(); i++) {
            ii = new Integer(i);
            dest.setInstrument(src.getManufacturer(ii), src.getModel(ii), src.getSerialNumber(ii), src.getType(ii), ii);
        }
        for (int i = 0; i < src.getDisplayROICount(); i++) {
            ii = new Integer(i);
            dest.setDisplayROI(src.getX0(ii), src.getY0(ii), src.getZ0(ii), src.getX1(ii), src.getY1(ii), src.getZ1(ii), src.getT0(ii), src.getT1(ii), src.getDisplayOptions(ii), ii);
        }
        for (int i = 0; i < src.getStageLabelCount(); i++) {
            ii = new Integer(i);
            dest.setStageLabel(src.getStageName(ii), src.getStageX(ii), src.getStageY(ii), src.getStageZ(ii), ii);
        }
        ii = null;
        dest.setPlaneInfo(0, 0, 0, src.getTimestamp(ii, ii, ii, ii), src.getExposureTime(ii, ii, ii, ii), ii);
        dest.setLightSource(src.getLightManufacturer(ii), src.getLightModel(ii), src.getLightSerial(ii), ii, ii);
        dest.setLaser(src.getLaserType(ii), src.getLaserMedium(ii), src.getLaserWavelength(ii), src.isFrequencyDoubled(ii), src.isTunable(ii), src.getPulse(ii), src.getPower(ii), ii, ii, ii, ii);
        dest.setFilament(src.getFilamentType(ii), src.getFilamentPower(ii), ii, ii);
        dest.setArc(src.getArcType(ii), src.getArcPower(ii), ii, ii);
        dest.setDetector(src.getDetectorManufacturer(ii), src.getDetectorModel(ii), src.getDetectorSerial(ii), src.getDetectorType(ii), src.getDetectorGain(ii), src.getDetectorVoltage(ii), src.getDetectorOffset(ii), ii, ii);
        dest.setObjective(src.getObjectiveManufacturer(ii), src.getObjectiveModel(ii), src.getObjectiveSerial(ii), src.getLensNA(ii), src.getObjectiveMagnification(ii), ii, ii);
        dest.setExcitationFilter(src.getExcitationManufacturer(ii), src.getExcitationModel(ii), src.getExcitationLotNumber(ii), src.getExcitationType(ii), ii);
        dest.setDichroic(src.getDichroicManufacturer(ii), src.getDichroicModel(ii), src.getDichroicLotNumber(ii), ii);
        dest.setEmissionFilter(src.getEmissionManufacturer(ii), src.getEmissionModel(ii), src.getEmissionLotNumber(ii), src.getEmissionType(ii), ii);
        dest.setFilterSet(src.getFilterSetManufacturer(ii), src.getFilterSetModel(ii), src.getFilterSetLotNumber(ii), ii, ii);
        dest.setOTF(src.getOTFSizeX(ii), src.getOTFSizeY(ii), src.getOTFPixelType(ii), src.getOTFPath(ii), src.getOTFOpticalAxisAverage(ii), ii, ii, ii, ii);
    }
