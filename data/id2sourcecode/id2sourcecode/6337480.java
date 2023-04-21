    @Override
    public void onApplicationStart() {
        Logger.info("Jqvalidation startup plugin Started");
        if (Play.mode == Mode.DEV) {
            MapSingleton.setClassFieldValidation(null);
        }
        if (MapSingleton.getClassFieldValidation() != null) {
            return;
        }
        Map<String, Map<String, String>> classFieldValidation = new HashMap<String, Map<String, String>>();
        try {
            RandomAccessFile validationRulesFile = prepareValidationEngineRules();
            @SuppressWarnings("rawtypes") List<Class> classes = Play.classloader.getAnnotatedClasses(Entity.class);
            classes.addAll(getSienaModels());
            Logger.info("Siena Classes are %s", getSienaModels());
            for (Class<?> c : classes) {
                Map<String, String> fieldsValidation = getClassFields(c, validationRulesFile);
                if (!fieldsValidation.isEmpty()) {
                    classFieldValidation.put(c.getSimpleName(), fieldsValidation);
                }
            }
            String jsEnd = "\n};}};$.validationEngineLanguage.newLang();})(jQuery);";
            validationRulesFile.writeBytes(jsEnd);
            validationRulesFile.getChannel().truncate(validationRulesFile.getChannel().position());
            validationRulesFile.close();
        } catch (ClassNotFoundException e) {
            Logger.error(e, "");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error(e, "");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Logger.error(e, "");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e, "");
        }
        MapSingleton.setClassFieldValidation(classFieldValidation);
        Logger.info("Jqvalidation startup plugin Finished\n%s", MapSingleton.getClassFieldValidation());
    }
