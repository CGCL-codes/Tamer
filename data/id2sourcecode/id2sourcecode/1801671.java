    public String generateValidValue(Map<ConstraintType, Constraint> constraints) throws UnsupportedConstraintException, MissingPropertyException {
        for (ConstraintType constraintType : constraints.keySet()) {
            if (!supportedConstraintTypes.contains(constraintType)) {
                throw new UnsupportedConstraintException(this, constraintType);
            }
        }
        String value = null;
        if (constraints.containsKey(ConstraintType.LENGTH)) {
            Integer max = (Integer) constraints.get(ConstraintType.LENGTH).getProperty("max");
            Integer min = (Integer) constraints.get(ConstraintType.LENGTH).getProperty("min");
            if (max == null && min == null) {
                throw new MissingPropertyException(constraints.get(ConstraintType.LENGTH), "min", "Expected at least one of properties [min, max] for Constraint of type LENGTH.");
            }
            min = (min == null ? 0 : min);
            max = (max == null ? Integer.MAX_VALUE : max);
            int length = min + random.nextInt(max - min);
            value = RandomStringUtils.random(length);
            return value;
        } else if (constraints.containsKey(ConstraintType.NOTNULL)) {
            return "notnull";
        }
        return value;
    }
