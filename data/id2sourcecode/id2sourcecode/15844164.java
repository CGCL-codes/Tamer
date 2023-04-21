    protected VariableValue processCompositeVal(Element child, String name, String comment, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int CV, String mask, String item) {
        VariableValue v;
        @SuppressWarnings("unchecked") List<Element> lChoice = child.getChildren("compositeChoice");
        CompositeVariableValue v1 = new CompositeVariableValue(name, comment, "", readOnly, infoOnly, writeOnly, opsOnly, CV, mask, 0, lChoice.size() - 1, _cvModel.allCvVector(), _status, item);
        v = v1;
        for (int k = 0; k < lChoice.size(); k++) {
            Element choiceElement = lChoice.get(k);
            String choice = LocaleSelector.getAttribute(choiceElement, "choice");
            v1.addChoice(choice);
            @SuppressWarnings("unchecked") List<Element> lSetting = choiceElement.getChildren("compositeSetting");
            for (int n = 0; n < lSetting.size(); n++) {
                Element settingElement = lSetting.get(n);
                String varName = LocaleSelector.getAttribute(settingElement, "label");
                String value = settingElement.getAttribute("value").getValue();
                v1.addSetting(choice, varName, findVar(varName), value);
            }
        }
        v1.lastItem();
        return v;
    }
