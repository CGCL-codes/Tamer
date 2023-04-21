    @Override
    public PsiClass build() {
        JavaPsiFacade psiFacade = getInstance(project);
        GlobalSearchScope globalSearchScope = allScope(project);
        PsiClass existingBuilder = psiFacade.findClass(qualifiedClassName, globalSearchScope);
        if (existingBuilder != null) {
            String classExistsMessage = "Builder already exists for '" + classToBuild.getName() + "', overwrite the existing builder?";
            int overwriteClass = showYesNoCancelDialog(project, classExistsMessage, "Warning", getQuestionIcon());
            switch(overwriteClass) {
                case 0:
                    existingBuilder.delete();
                    break;
                case 1:
                    return existingBuilder;
                default:
                    return null;
            }
        }
        return buildNewClass(psiFacade, globalSearchScope);
    }
