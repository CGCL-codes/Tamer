    public void mouseClicked(MouseEvent e) {
        textarea.setText(null);
        labelIllustration.setIcon(null);
        if (e.getSource().equals(this.jRadioButtonChoice)) {
            String desChoice = "Respondents select the item(s) that are correct \nfrom a list.\n\n" + "For example:\n";
            textarea.append(desChoice);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(choiceIcon);
            }
            jRadioButtonChoice.setSelected(true);
        } else if (e.getSource().equals(this.jRadioButtonOrder)) {
            String desOrder = "Respondents place a set of items in a correct \norder.\n\n" + "For example:\n";
            textarea.append(desOrder);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(orderIcon);
            }
            jRadioButtonOrder.setSelected(true);
        } else if (e.getSource().equals(this.jRadioButtonAssociate)) {
            String desAssociate = "Respondents indicate the items in a set that are \nrelated.\n\n" + "For example:\n";
            textarea.append(desAssociate);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(associateIcon);
            }
            jRadioButtonAssociate.setSelected(true);
        } else if (e.getSource().equals(this.jRadioButtonInline)) {
            String desInline = "A choice is embedded in a line of text. Respondents \nselect the option that is correct.\n\n" + "For example:\n";
            textarea.append(desInline);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(inlineIcon);
            }
            jRadioButtonInline.setSelected(true);
        } else if (e.getSource().equals(this.jRadioButtonTextEntry)) {
            String desText = "Respondents generate a word or phrase that is \ncorrect.\n\n" + "For example:\n";
            textarea.append(desText);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(textentryIcon);
            }
            jRadioButtonTextEntry.setSelected(true);
        } else if (e.getSource().equals(this.jRadioButtonHotspot)) {
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(hotspotIcon);
                String desHotspot = "Respondents select the point on an image that is \ncorrect.\n\n" + "For example:\n";
                textarea.append(desHotspot);
                jRadioButtonHotspot.setSelected(true);
            } else {
                String desGraphic = "The Applet cannot handle this type of question because it cannot read or write to disk";
                textarea.append(desGraphic);
            }
        } else if (e.getSource().equals(this.jRadioButtonGraphic)) {
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                String desGraphic = "Respondents arrange points on an image in order.\n\n" + "For example:\n";
                textarea.append(desGraphic);
                labelIllustration.setIcon(graphicIcon);
                jRadioButtonGraphic.setSelected(true);
            } else {
                String desGraphic = "The Applet cannot handle this type of question because it cannot read or write to disk";
                textarea.append(desGraphic);
            }
        } else if (e.getSource().equals(this.jRadioButtonSlider)) {
            String desSlider = "Respondents select the correct value with a \nslider.\n\n" + "For example:\n";
            textarea.append(desSlider);
            if (owner.getClass().getSuperclass().getSimpleName().equals("JFrame")) {
                labelIllustration.setIcon(sliderIcon);
            }
            jRadioButtonSlider.setSelected(true);
        }
    }
