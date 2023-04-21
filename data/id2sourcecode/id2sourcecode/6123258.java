    private RiskAssessment convertRiskAssessmentDtos(RiskAssessmentDto communityRiskAssessmentDto, RiskAssessmentDto localRiskAssessmentDto, AonsFormat aonsFormat) {
        RiskAssessment riskAssessment = new RiskAssessment();
        Float communityRiskAssessment = communityRiskAssessmentDto.getAssessment();
        Float localRiskAssessment = localRiskAssessmentDto.getAssessment();
        riskAssessment.setCommunityAssessment(communityRiskAssessment);
        List<QuestionAnswer> communityQuestionAnswers = createQuestionAnswerList(communityRiskAssessmentDto);
        riskAssessment.setCommunityQuestionAnswers(communityQuestionAnswers);
        riskAssessment.setCommunityText(communityRiskAssessmentDto.getFreeText());
        riskAssessment.setCommunityRules(obsolescenceDao.retrieveRules(communityRiskAssessmentDto.getRulesId()));
        riskAssessment.setLocalAssessment(localRiskAssessment);
        List<QuestionAnswer> localQuestionAnswers = createQuestionAnswerList(localRiskAssessmentDto);
        riskAssessment.setLocalQuestionAnswers(localQuestionAnswers);
        riskAssessment.setLocalText(localRiskAssessmentDto.getFreeText());
        riskAssessment.setCommunityRules(obsolescenceDao.retrieveRules(communityRiskAssessmentDto.getRulesId()));
        Float finalRiskAssessment = (communityRiskAssessment + localRiskAssessment) / 2;
        riskAssessment.setAonsFormat(aonsFormat);
        riskAssessment.setFinalAssessment(finalRiskAssessment);
        return riskAssessment;
    }
