    public Customisation(String location) throws IOException {
        URL url = this.getClass().getResource(location);
        if (url != null) {
            props = new Properties();
            props.load(url.openStream());
            instrument = getProperty("instrument.name", "Instrument");
            try {
                invTypeVisible = new Boolean(getProperty("inv.type.visible", "true")).booleanValue();
            } catch (Exception mre) {
                invTypeVisible = true;
            }
            try {
                invNumberVisible = new Boolean(getProperty("inv.number.visible", "true")).booleanValue();
            } catch (Exception mre) {
                invNumberVisible = true;
            }
            try {
                visitIdVisible = new Boolean(getProperty("visitId.visible", "true")).booleanValue();
            } catch (Exception mre) {
                visitIdVisible = true;
            }
            try {
                shiftVisible = new Boolean(getProperty("shift.visible", "true")).booleanValue();
            } catch (Exception mre) {
                shiftVisible = true;
            }
            try {
                bcatInvStr = new Boolean(getProperty("bcatinvstr.visible", "true")).booleanValue();
            } catch (Exception mre) {
                bcatInvStr = true;
            }
            try {
                facilitySearchPageVisible = new Boolean(getProperty("facility.search.page.visible", "false")).booleanValue();
            } catch (Exception mre) {
                facilitySearchPageVisible = false;
            }
            try {
                cycleVisible = new Boolean(getProperty("cycle.visible", "false")).booleanValue();
            } catch (Exception mre) {
                cycleVisible = false;
            }
            try {
                grantIdVisible = new Boolean(getProperty("grantId.visible", "false")).booleanValue();
            } catch (Exception mre) {
                grantIdVisible = false;
            }
            try {
                abstractPopupVisible = new Boolean(getProperty("abstract.popup.visible", "false")).booleanValue();
            } catch (Exception mre) {
                abstractPopupVisible = false;
            }
            try {
                invParamVisible = new Boolean(getProperty("inv.param.visible", "false")).booleanValue();
            } catch (Exception mre) {
                invParamVisible = false;
            }
            try {
                downloadTypeSRB = new Boolean(getProperty("download.type.srb", "false")).booleanValue();
            } catch (Exception mre) {
                downloadTypeSRB = false;
            }
            try {
                invParamName = getProperty("inv.param.name", "false");
            } catch (Exception mre) {
                invParamName = "Param Name";
            }
            try {
                String instrumentList = getProperty("instrument.list");
                String[] instruments = instrumentList.split(",");
                instrumentsItems.add(new SelectItem("", ""));
                for (String instrument : instruments) {
                    instrumentsItems.add(new SelectItem(instrument, instrument));
                }
            } catch (Exception mre) {
                log.warn("Unable to read in instruments", mre);
                instrumentsItems.add(new SelectItem("error", "error"));
            }
            try {
                String investigationTypeList = getProperty("investigation.type.list");
                String[] types = investigationTypeList.split(",");
                investigationTypeItems.add(new SelectItem("", ""));
                for (String investigationType : types) {
                    int index = investigationType.indexOf("_");
                    if (index != -1) {
                        investigationTypeItems.add(new SelectItem(investigationType, investigationType.substring(0, index)));
                    } else {
                        investigationTypeItems.add(new SelectItem(investigationType, investigationType));
                    }
                }
            } catch (Exception mre) {
                log.warn("Unable to read in investigation type", mre);
                instrumentsItems.add(new SelectItem("error", "error"));
            }
        } else {
            throw new FileNotFoundException(location + " not found");
        }
    }
