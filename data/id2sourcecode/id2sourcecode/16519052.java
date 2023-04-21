    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.jdbcTemplate, "jdbcTemplate cannot be null");
        Assert.notNull(this.readAllTagsSql, "insertTagSql cannot be null");
        Assert.notNull(this.writeTagSql, "readAllTagsFromTableSql cannot be null");
    }
