    public boolean handleTarget(EditableParameterModel p, int total, int curr) throws Exception {
        float abs_maxv = (float) p.getParameterDescriptor().getMaxValue().intValue();
        float abs_minv = (float) p.getParameterDescriptor().getMinValue().intValue();
        float abs_val = p.getValue().intValue();
        if (mode == 0) abs_pivot = abs_minv + (abs_maxv - abs_minv) / 2; else if (mode == 1) {
            abs_pivot = localCenter;
        } else {
            numericField.getComponent().setText("");
            if (curr == 0) {
                if (cmdDlg.run(null, 1, 1) != ZCommandDialog.COMPLETED) return false; else pivotStr = numericField.getValue();
            }
            int temp_pivot;
            try {
                temp_pivot = p.getParameterDescriptor().getValueForUnitlessString(pivotStr).intValue();
            } catch (Exception e) {
                throw new CommandFailedException("pivot is invalid");
            }
            if (!p.getParameterDescriptor().isValidValue(IntPool.get(temp_pivot))) throw new CommandFailedException("pivot value is not valid");
            abs_pivot = temp_pivot;
        }
        float nv = abs_pivot + (abs_pivot - abs_val);
        if (nv > abs_maxv) nv = abs_minv + (nv - abs_maxv); else if (nv < abs_minv) nv = nv + abs_maxv - (abs_minv - nv);
        p.setValue(IntPool.get(Math.round(nv)));
        return true;
    }
