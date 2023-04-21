    public Element buildPermissionsField(String form, String entity, List<Option> includedOptions, List<Option> excludedOptions) throws RepositoryException {
        final int listSize = 8;
        final String listWidth = "19em";
        final String buttonWidth = "95px";
        final String[] roles = new String[] { "manager", "writer", "reader" };
        final String field = "permission-" + entity;
        Element excludedSelect = new Element("select", xhtml).setAttribute("id", form + "-form" + "-" + field + "-all" + "-field").setAttribute("multiple", "multiple").setAttribute("size", String.valueOf(listSize)).setAttribute("style", "width: " + listWidth + ";");
        for (Option option : excludedOptions) {
            excludedSelect.addContent(new Element("option", xhtml).setAttribute("value", option.getValue()).setText(option.getText()));
        }
        Element includedSelect = new Element("select", xhtml).setAttribute("id", form + "-form" + "-" + field + "-field").setAttribute("multiple", "multiple").setAttribute("size", String.valueOf(listSize)).setAttribute("style", "width: " + listWidth + ";");
        for (Option option : includedOptions) {
            includedSelect.addContent(new Element("option", xhtml).setAttribute("value", option.getValue()).setText(option.getText()));
        }
        Element fieldTable = new Element("table", xhtml);
        Element buttonCell = new Element("td", xhtml).setAttribute("style", "vertical-align: middle");
        for (String role : roles) {
            buttonCell.addContent(new Element("input", xhtml).setAttribute("type", "button").setAttribute("style", "width: " + buttonWidth + "; text-align: right;").setAttribute("value", StringUtils.capitalize(role) + " >").setAttribute("onclick", "moveOptions(" + "'#" + form + "-form" + "-" + field + "-all" + "-field'" + ", " + "'#" + form + "-form" + "-" + field + "-field'" + ", " + "function(option) { " + "  option.text += ' (' + '" + role + "' + ')'; " + "  copyOption('#project-form-" + role + "-" + entity + "-field', option); " + "}" + ");"));
            buttonCell.addContent(new Element("br", xhtml));
        }
        {
            buttonCell.addContent(new Element("input", xhtml).setAttribute("type", "button").setAttribute("style", "width: " + buttonWidth + "; text-align: left;").setAttribute("value", "< Delete").setAttribute("onclick", "moveOptions(" + "'#" + form + "-form" + "-" + field + "-field'" + ", " + "'#" + form + "-form" + "-" + field + "-all" + "-field'" + ", " + "function(option) { " + "  var t = option.text; " + "  var role = t.substring(t.lastIndexOf(' (') + 2, t.lastIndexOf(')'));" + "  deleteOption('#project-form-' + role + '-" + entity + "-field', option); " + "  option.text = t.substring(0, t.lastIndexOf(' (')); " + "}" + ");"));
        }
        fieldTable.addContent(new Element("tr", xhtml).addContent(new Element("td", xhtml).addContent(excludedSelect)).addContent(buttonCell).addContent(new Element("td", xhtml).addContent(includedSelect)));
        Element hiddenRow = new Element("tr", xhtml).setAttribute("style", "display: none;");
        for (String role : roles) {
            Element includedSelectRole = new Element("select", xhtml).setAttribute("id", "project-form-" + role + "-" + entity + "-field").setAttribute("multiple", "multiple").setAttribute("size", String.valueOf(listSize)).setAttribute("style", "width: " + listWidth + ";");
            for (Option option : includedOptions) {
                if (!option.getText().endsWith(" (" + role + ")")) continue;
                includedSelectRole.addContent(new Element("option", xhtml).setAttribute("value", option.getValue()).setText(option.getText()));
            }
            hiddenRow.addContent(new Element("td", xhtml).addContent(includedSelectRole));
        }
        fieldTable.addContent(hiddenRow);
        return fieldTable;
    }
