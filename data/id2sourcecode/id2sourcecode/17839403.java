        @Override
        protected Object getValue(MergeInput input, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    return selectedInputs.contains(input) ? Boolean.TRUE : Boolean.FALSE;
                case 1:
                    return input.getDevice();
                case 2:
                    return new Integer(input.getChannel() + 1);
            }
            return null;
        }
