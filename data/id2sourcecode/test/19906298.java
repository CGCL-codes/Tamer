    public boolean updateScience(Science science) {
        int updated = 0;
        String provinceId = science.getProvince().getId();
        Calendar update = science.getLastUpdate();
        try {
            queryForObject(EXISTS_SCIENCE, String.class, science.getId());
            updated = update(UPDATE_SCIENCE, update, science.getAlchemy(), science.getTools(), science.getHousing(), science.getFood(), science.getMilitary(), science.getCrime(), science.getChanneling(), science.getId(), update);
        } catch (EmptyResultDataAccessException e) {
            updated = update(INSERT_SCIENCE, science.getId(), provinceId, update, science.getAlchemy(), science.getTools(), science.getHousing(), science.getFood(), science.getMilitary(), science.getCrime(), science.getChanneling());
        }
        return updated > 0;
    }
