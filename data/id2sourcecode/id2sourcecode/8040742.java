    }

    /**
   * Adds a blueprint
   */
    public Blueprint addBlueprint(Blueprint blueprint) throws IOException {
        if (!blueprint.isReadOnly()) getBlueprintFile(blueprint);
        List<Blueprint> blueprints = getBlueprintsInternal(blueprint.getTag());
        for (ListIterator<Blueprint> it = blueprints.listIterator(); it.hasNext(); ) {
            Blueprint other = (Blueprint) it.next();
            if (other.getName().equalsIgnoreCase(blueprint.getName())) {
                if (other.isReadOnly()) throw new IOException("Can't overwrite read-only blueprint");
                it.remove();
                break;
            }
