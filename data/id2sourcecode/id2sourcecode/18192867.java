    private void properties2sql(File f) throws Exception {
        Properties p = new Properties();
        FileInputStream fis = new FileInputStream(f);
        try {
            p.load(fis);
        } catch (Exception e) {
            System.err.println("Failed to process " + f.getPath());
            throw e;
        } finally {
            IOUtils.closeQuietly(fis);
        }
        SqlExecutor executor = new SimpleSqlExecutor(connection);
        cntFile++;
        try {
            for (Iterator<Entry<Object, Object>> it = p.entrySet().iterator(); it.hasNext(); ) {
                Entry<Object, Object> en = it.next();
                String k = en.getKey().toString();
                String v = en.getValue().toString();
                Map<String, String> param = new HashMap<String, String>();
                String clazz = getClazz(f);
                if (clazz.startsWith(prefix)) {
                    clazz = clazz.substring(prefix.length());
                }
                param.put("clazz", clazz);
                Locale locale = LocaleUtils.localeFromFileName(f, defaultLocale);
                if (StringUtils.isNotEmpty(locale.toString()) && !LocaleUtils.isAvailableLocale(locale)) {
                    System.out.println("Warning: " + locale + " is not a valid Locale [" + f.getName() + "]");
                }
                param.put("language", getLocaleValue(locale.getLanguage()));
                param.put("country", getLocaleValue(locale.getCountry()));
                param.put("variant", getLocaleValue(locale.getVariant()));
                param.put("name", k);
                param.put("value", v);
                cntIns += executor.executeUpdate(insertSql, param);
            }
            connection.commit();
        } catch (Exception e) {
            rollback();
            System.err.println("Failed to process " + f.getPath());
            throw e;
        }
    }
