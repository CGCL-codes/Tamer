    public void configureFromXML(Tag tag) throws ConfigurationException {
        Pattern namePattern = Pattern.compile("(\\D+?)(-?\\d+)");
        infoText = name = tag.getAttributeAsString("name");
        Matcher m = namePattern.matcher(name);
        if (!m.matches()) {
            throw new ConfigurationException("Invalid name format: " + name);
        }
        String letters = m.group(1);
        if (letters.length() == 1) {
            letter = letters.charAt(0);
        } else {
            letter = 26 + letters.charAt(1);
        }
        try {
            number = Integer.parseInt(m.group(2));
            if (number > 90) number -= 100;
        } catch (NumberFormatException e) {
        }
        if (lettersGoHorizontal()) {
            row = number;
            column = letter - '@';
            if (getTileOrientation() == TileOrientation.EW) {
                x = column;
                y = row / 2;
            } else {
                x = column;
                y = (row + 1) / 2;
            }
        } else {
            row = letter - '@';
            column = number;
            if (getTileOrientation() == TileOrientation.EW) {
                x = (column + 8 + (letterAHasEvenNumbers() ? 1 : 0)) / 2 - 4;
                y = row;
            } else {
                x = column;
                y = (row + 1) / 2;
            }
        }
        preprintedTileId = tag.getAttributeAsInteger("tile", -999);
        preprintedPictureId = tag.getAttributeAsInteger("pic", 0);
        preprintedTileRotation = tag.getAttributeAsInteger("orientation", 0);
        currentTileRotation = preprintedTileRotation;
        impassable = tag.getAttributeAsString("impassable");
        tileCost = tag.getAttributeAsIntegerArray("cost", new int[0]);
        valuesPerPhase = tag.getAttributeAsIntegerArray("value", null);
        cityName = tag.getAttributeAsString("city", "");
        if (Util.hasValue(cityName)) {
            infoText += " " + cityName;
        }
        if (tag.getAttributeAsString("unlaidHomeBlocksTokens") != null) {
            setBlockedForTokenLays(tag.getAttributeAsBoolean("unlaidHomeBlocksTokens", false));
        }
        reservedForCompany = tag.getAttributeAsString("reserved");
        List<Tag> bonusTags = tag.getChildren("RevenueBonus");
        if (bonusTags != null) {
            revenueBonuses = new ArrayList<RevenueBonusTemplate>();
            for (Tag bonusTag : bonusTags) {
                RevenueBonusTemplate bonus = new RevenueBonusTemplate();
                bonus.configureFromXML(bonusTag);
                revenueBonuses.add(bonus);
            }
        }
        for (int side : tag.getAttributeAsIntegerArray("open", new int[0])) {
            if (openHexSides == null) openHexSides = new boolean[6];
            openHexSides[side % 6] = true;
        }
        Tag accessTag = tag.getChild("Access");
        if (accessTag != null) {
            String runThroughString = accessTag.getAttributeAsString("runThrough");
            if (Util.hasValue(runThroughString)) {
                try {
                    runThroughAllowed = RunThrough.valueOf(runThroughString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("Illegal value for MapHex" + name + " runThrough property: " + runThroughString, e);
                }
            }
            String runToString = accessTag.getAttributeAsString("runTo");
            if (Util.hasValue(runToString)) {
                try {
                    runToAllowed = RunTo.valueOf(runToString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("Illegal value for MapHex " + name + " runTo property: " + runToString, e);
                }
            }
            String loopString = accessTag.getAttributeAsString("loop");
            if (Util.hasValue(loopString)) {
                try {
                    loopAllowed = Loop.valueOf(loopString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("Illegal value for MapHex " + name + " loop property: " + loopString, e);
                }
            }
            String typeString = accessTag.getAttributeAsString("type");
            if (Util.hasValue(typeString)) {
                try {
                    stopType = Type.valueOf(typeString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("Illegal value for MapHex " + name + " stop type property: " + typeString, e);
                }
            }
            String scoreTypeString = accessTag.getAttributeAsString("score");
            if (Util.hasValue(scoreTypeString)) {
                try {
                    scoreType = Score.valueOf(scoreTypeString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConfigurationException("Illegal value for MapHex " + name + " score type property: " + scoreTypeString, e);
                }
            }
        }
    }
