    public Link createLink() {
        SeqFeatureI peptideResultHit = result.getHitFeature();
        int pepHitStart = peptideResultHit.getStart(), pepHitEnd = peptideResultHit.getEnd();
        int midPepHit = (pepHitStart + pepHitEnd) / 2;
        int genomicMid = getGenomicPosition(midPepHit);
        SeqFeatureI hitExon = transcript.getFeatureContaining(genomicMid);
        if (hitExon == null) {
            System.out.println("Unable to find transcript " + transcript.getName() + " exon for " + "result " + result.getName() + " hit coords " + pepHitStart + "-" + pepHitEnd + ". Check data.");
            return null;
        }
        int genHitStart = getGenomicPosition(pepHitStart);
        int genHitEnd = getGenomicPosition(pepHitEnd);
        int genHitLow = hitExon.getStrand() == 1 ? genHitStart : genHitEnd;
        int genHitHigh = hitExon.getStrand() == 1 ? genHitEnd : genHitStart;
        SeqFeatureI hitExonFragment = hitExon.cloneFeature();
        if (hitExonFragment.getLow() < genHitLow) hitExonFragment.setLow(genHitLow);
        if (hitExonFragment.getHigh() > genHitHigh) hitExonFragment.setHigh(genHitHigh);
        Link link = null;
        boolean hasSpecFeat = linker.hasSpeciesFeature();
        if (transcriptIsFromTopSpecies) {
            link = new Link(hitExonFragment, result, hasSpecFeat, linker.hasPercentIdentity());
            link.setSpeciesFeature1(hitExon);
            link.setTypeIsFeat1(false);
        } else {
            link = new Link(result, hitExonFragment, hasSpecFeat, linker.hasPercentIdentity());
            link.setSpeciesFeature2(hitExon);
        }
        return link;
    }
