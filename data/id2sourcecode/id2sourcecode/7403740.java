    public void addConstraintSet(final ConstraintSet constraintSet, final boolean overwrite) throws ConstraintSetAlreadyDefinedException, IllegalArgumentException {
        Assert.argumentNotNull("constraintSet", constraintSet);
        Assert.argumentNotEmpty("constraintSet.id", constraintSet.getId());
        synchronized (constraintSetsById) {
            if (!overwrite && constraintSetsById.containsKey(constraintSet.getId())) throw new ConstraintSetAlreadyDefinedException(constraintSet.getId());
            constraintSetsById.put(constraintSet.getId(), constraintSet);
        }
    }
