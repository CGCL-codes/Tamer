        public void makeStep() {
            if (wrpChValidation.getChannel() != null) {
                wrpChValidation.setValueChanged(false);
            }
            QuadMeasure quadMeasure = (QuadMeasure) quadMeasuresAllV.get(current_step);
            quadMeasure.setCurrentToEPICS();
            scanRunner.setMessage("Quad: " + quadMeasure.getQuad_Element().getName());
        }
