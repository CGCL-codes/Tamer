    @SuppressWarnings("unchecked")
    public MascotParameters(String serverName) throws MalformedURLException, IOException {
        super(new Parameter[0]);
        para.add(peakLists);
        URL url = new URL(getSearchMaskUrlString());
        URLConnection lConn = url.openConnection();
        InputStream ins = lConn.getInputStream();
        iForm = HTTPForm.parseHTMLForm(ins);
        Vector<InputInterface> lvInputs = iForm.getInputs();
        for (int i = 0; i < lvInputs.size(); i++) {
            InputInterface lInput = (InputInterface) lvInputs.elementAt(i);
            if (lInput.getType() == InputInterface.SELECTINPUT) {
                if (lInput.getName().equals("FORMAT")) {
                    lInput.setValue("Mascot generic");
                } else if (lInput.getName().equals("REPORT")) {
                    lInput.setValue(((SelectInput) lInput).getElements()[0]);
                } else {
                    SelectInput input = (SelectInput) lInput;
                    String[] elements = ((SelectInput) lInput).getElements();
                    if (input.getMultiple()) {
                        UserParameter p = new MultiChoiceParameter<String>(lInput.getComment(), lInput.getName(), elements);
                        para.add(p);
                    } else {
                        UserParameter p = new ComboParameter<String>(lInput.getComment(), lInput.getName(), elements);
                        para.add(p);
                    }
                }
            }
            if (lInput.getType() == InputInterface.CHECKBOX) {
                if (lInput.getName().equals("OVERVIEW")) {
                    lInput.setValue("0");
                } else para.add(new BooleanParameter(lInput.getComment(), lInput.getName()));
            }
            if (lInput.getType() == InputInterface.RADIOINPUT) {
                RadioInput input = (RadioInput) lInput;
                String[] elements = input.getChoices();
                para.add(new ComboParameter<String>(lInput.getComment(), lInput.getName(), elements));
            }
            if (lInput.getType() == InputInterface.TEXTFIELDINPUT && lInput instanceof TextFieldInput) {
                TextFieldInput textFiled = (TextFieldInput) lInput;
                if (textFiled.isHidden()) {
                } else if (textFiled.getName().equals("FILE")) {
                    textFiled.setValue("");
                } else if (textFiled.getName().equals("PRECURSOR")) {
                    textFiled.setValue("");
                } else if (textFiled.getName().equals("USERNAME")) {
                    textFiled.setValue("");
                } else if (textFiled.getName().equals("USEREMAIL")) {
                    textFiled.setValue("");
                } else if (textFiled.getName().equals("COM")) {
                    textFiled.setValue("Mzmine " + MZmineCore.getMZmineVersion());
                } else if (textFiled.getName().equals("SEG")) {
                    textFiled.setValue("");
                } else {
                    para.add(new StringParameter(lInput.getComment(), lInput.getName()));
                }
            }
        }
    }
