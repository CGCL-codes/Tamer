    private void globalSelectionMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            Debug.println(6, "mouse clicked: unselect");
            layer.getChannel(startChannelIndex).setEmptySelection();
        } else if (e.getClickCount() == 2) {
            Debug.println(6, "mouse double-clicked: create marked selection");
            layer.getChannel(startChannelIndex).setMarkedSelection(startSample);
        } else if (e.getClickCount() == 3) {
            Debug.println(6, "mouse tripple-clicked: create channel selection");
            layer.getChannel(startChannelIndex).setFullSelection();
        }
        wideSelectionCopy(e, layer.getChannel(startChannelIndex).getSelection());
        updateHistory(GLanguage.translate(getName()) + " " + GLanguage.translate("range"));
        repaintFocussedClipEditor();
    }
