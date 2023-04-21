    @Override
    public String getText(Object object) {
        Object labelValue = ((Port) object).getChannel();
        String label = labelValue == null ? null : labelValue.toString();
        return label == null || label.length() == 0 ? getString("_UI_Port_type") : getString("_UI_Port_type") + " " + label;
    }
