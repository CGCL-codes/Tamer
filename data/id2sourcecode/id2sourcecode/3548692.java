        public Object getValueAt(final int row, final int column) {
            final KnobElement element = _knob.getElements().get(row);
            if (element == null) return null;
            switch(column) {
                case PV_COLUMN:
                    return element.getChannelString();
                case COEFFICIENT_COLUMN:
                    return new FormattedNumber(element.getCoefficient());
                case CUSTOM_LIMITS_COLUMN:
                    return element.isUsingCustomLimits();
                case LOWER_LIMIT_COLUMN:
                    return new FormattedNumber(element.getLowerLimit());
                case UPPER_LIMIT_COLUMN:
                    return new FormattedNumber(element.getUpperLimit());
                default:
                    return "?";
            }
        }
