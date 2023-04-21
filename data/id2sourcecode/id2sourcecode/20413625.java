        public boolean validateStep() {
            boolean valid_res = false;
            if (wrpChValidation.getChannel() != null) {
                double val_min = minValidationTextField.getValue();
                double val_max = maxValidationTextField.getValue();
                double val = wrpChValidation.getValue();
                if (val >= val_min && val <= val_max && wrpChValidation.valueChanged()) {
                    valid_res = true;
                }
            }
            return valid_res;
        }
