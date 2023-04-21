package net.sf.rottz.tv.client.rottzclient.screens.components.infopanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import net.sf.rottz.tv.client.RottzTVClientException;
import net.sf.rottz.tv.client.rottzclient.screens.GameScreen;
import net.sf.rottz.tv.common.media.AdInfo;

public class AdInfoPanel extends InfoPanel {

    private static final int ADINFO_LINE_HEIGHT = 25;

    private static final int ADINFO_PANEL_WIDTH = 250;

    private static final int AD_VALUE_WIDTH = 100;

    private GameScreen screen = null;

    private AdInfo adInfo;

    @Override
    public void render(Graphics g, Point screenPos) {
        if (screen == null || adInfo == null) throw new RottzTVClientException("Required helpers were not set. Aborting render of panel.");
        Rectangle adRect = new Rectangle(screenPos);
        adRect.width = ADINFO_PANEL_WIDTH;
        Rectangle adCompanyRect = new Rectangle(adRect);
        adCompanyRect.height = ADINFO_LINE_HEIGHT;
        String adDescription = adInfo.getDescription();
        Rectangle adDescRect = new Rectangle(adCompanyRect);
        adDescRect.height = getNeededLines(g, adDescription, ADINFO_PANEL_WIDTH) * ADINFO_LINE_HEIGHT;
        adDescRect.y += adCompanyRect.height;
        Rectangle payDollarSignRect = new Rectangle(adDescRect);
        payDollarSignRect.width = 20;
        payDollarSignRect.height = ADINFO_LINE_HEIGHT;
        payDollarSignRect.y += adDescRect.height;
        Rectangle payAmountRect = new Rectangle(payDollarSignRect);
        payAmountRect.x += payDollarSignRect.width;
        payAmountRect.width = AD_VALUE_WIDTH;
        Rectangle bonusPhraseRect = new Rectangle(payAmountRect);
        bonusPhraseRect.x += payAmountRect.width;
        bonusPhraseRect.width = adRect.width - (bonusPhraseRect.x - adRect.x);
        Rectangle penaltyDollarSignRect = new Rectangle(payDollarSignRect);
        penaltyDollarSignRect.width = 20;
        penaltyDollarSignRect.y += ADINFO_LINE_HEIGHT;
        Rectangle penaltyAmountRect = new Rectangle(penaltyDollarSignRect);
        penaltyAmountRect.x += penaltyDollarSignRect.width;
        penaltyAmountRect.width = AD_VALUE_WIDTH;
        Rectangle penaltyPhraseRect = new Rectangle(penaltyAmountRect);
        penaltyPhraseRect.x += penaltyAmountRect.width;
        penaltyPhraseRect.width = adRect.width - (penaltyPhraseRect.x - adRect.x);
        Rectangle minPhraseRect = new Rectangle(penaltyDollarSignRect);
        minPhraseRect.width = 40;
        minPhraseRect.y += ADINFO_LINE_HEIGHT;
        Rectangle specNeededRect = new Rectangle(minPhraseRect);
        specNeededRect.x += minPhraseRect.width;
        specNeededRect.width = 35;
        Rectangle millionPhraseRect = new Rectangle(specNeededRect);
        millionPhraseRect.x += specNeededRect.width;
        millionPhraseRect.width = 45;
        Rectangle percentPrefixRect = new Rectangle(millionPhraseRect);
        percentPrefixRect.x += millionPhraseRect.width;
        percentPrefixRect.width = 15;
        Rectangle percentValueRect = new Rectangle(percentPrefixRect);
        percentValueRect.x += percentPrefixRect.width;
        percentValueRect.width = 15;
        Rectangle percentSuffixRect = new Rectangle(percentValueRect);
        percentSuffixRect.x += percentValueRect.width;
        percentSuffixRect.width = 20;
        Rectangle adShowsLeftRect = new Rectangle(minPhraseRect);
        adShowsLeftRect.width = ADINFO_PANEL_WIDTH;
        adShowsLeftRect.y += ADINFO_LINE_HEIGHT;
        adRect.height = (adShowsLeftRect.y + adShowsLeftRect.height) - adCompanyRect.y;
        screen.drawRectangle(g, adRect, Color.BLACK, Color.GRAY);
        screen.drawRectangle(g, adCompanyRect, null, Color.RED);
        screen.drawRectangle(g, payDollarSignRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, payAmountRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, bonusPhraseRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, penaltyDollarSignRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, penaltyAmountRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, penaltyPhraseRect, null, Color.LIGHT_GRAY);
        screen.drawRectangle(g, adShowsLeftRect, null, Color.LIGHT_GRAY);
        String company = adInfo.getCompany();
        String payAmount = InfoPanel.applyThousandSeparator(adInfo.getPaidValue().toString());
        String penaltyAmount = InfoPanel.applyThousandSeparator(adInfo.getFailPenalty().toString());
        int neededAudienceKs = 500;
        int maxAudienceKs = 2000;
        String specsNeededPercent;
        if (maxAudienceKs == 0 || neededAudienceKs > maxAudienceKs) specsNeededPercent = "N/A"; else specsNeededPercent = String.valueOf((100 * neededAudienceKs) / maxAudienceKs);
        String specsNeededKs = String.valueOf(neededAudienceKs / 1000.0);
        int neededExhibits = adInfo.getNeededExhibits();
        int exhibitsDone = 2;
        int daysLeft = 1;
        String adShowsLeftPhrase = buildAdsLeftPhrase(exhibitsDone, neededExhibits, daysLeft);
        screen.drawLeftAlignedString(g, adCompanyRect, company, 10, Color.BLACK);
        screen.drawMultiLineString(g, adDescription, adDescRect, 10, ADINFO_LINE_HEIGHT, Color.BLACK);
        screen.drawLeftAlignedString(g, payDollarSignRect, "$", 10, Color.BLACK);
        screen.drawRightAlignedString(g, payAmount, payAmountRect, 10, Color.BLACK);
        screen.drawLeftAlignedString(g, bonusPhraseRect, "Bonus", 10, Color.BLACK);
        screen.drawLeftAlignedString(g, penaltyDollarSignRect, "$", 10, Color.BLACK);
        screen.drawRightAlignedString(g, penaltyAmount, penaltyAmountRect, 10, Color.BLACK);
        screen.drawLeftAlignedString(g, penaltyPhraseRect, "Penalty", 10, Color.BLACK);
        screen.drawLeftAlignedString(g, minPhraseRect, "min.", 10, Color.BLACK);
        screen.drawLeftAlignedString(g, specNeededRect, specsNeededKs, 10, Color.BLACK);
        screen.drawLeftAlignedString(g, millionPhraseRect, "million", 0, Color.BLACK);
        screen.drawLeftAlignedString(g, percentPrefixRect, "(=", 0, Color.BLACK);
        screen.drawRightAlignedString(g, specsNeededPercent, percentValueRect, 0, Color.BLACK);
        screen.drawLeftAlignedString(g, percentSuffixRect, "%)", 0, Color.BLACK);
        screen.drawLeftAlignedString(g, adShowsLeftRect, adShowsLeftPhrase, 10, Color.BLACK);
    }

    public int getNeededLines(Graphics g, String text, int maxWidth) {
        int[] breakPos = screen.getLineBreakPositions(g, text, maxWidth);
        return (1 + breakPos.length);
    }

    private String buildAdsLeftPhrase(int exhibitsDone, int neededExhibits, int daysLeft) {
        String exhibitsLeft = String.valueOf(neededExhibits - exhibitsDone);
        String periodLeft;
        if (daysLeft == 0) periodLeft = " until end of day"; else periodLeft = " within " + (daysLeft + 1) + " days";
        String adsLeftPhrase = exhibitsLeft + " out of " + neededExhibits + periodLeft;
        return adsLeftPhrase;
    }

    public void setHelpers(GameScreen screen, AdInfo adInfo) {
        this.screen = screen;
        this.adInfo = adInfo;
    }
}
