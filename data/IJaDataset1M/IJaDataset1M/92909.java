package org.cleartk.syntax.feature;

import java.util.List;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.simple.CoveredTextExtractor;
import org.cleartk.classifier.feature.extractor.simple.TypePathExtractor;
import org.cleartk.syntax.SyntaxTestBase;
import org.cleartk.syntax.constituent.type.TreebankNode;
import org.cleartk.syntax.constituent.util.TreebankNodeUtility;
import org.junit.Assert;
import org.junit.Test;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Steven Bethard
 */
public class SyntacticPathExtractorTest extends SyntaxTestBase {

    @Test
    public void test() throws Throwable {
        tokenBuilder.buildTokens(jCas, "I ran home", "I ran home", "PRP VBD NN");
        TreebankNode iNode = TreebankNodeUtility.newNode(jCas, 0, 1, "PRP");
        TreebankNode inpNode = TreebankNodeUtility.newNode(jCas, "NP", iNode);
        TreebankNode ranNode = TreebankNodeUtility.newNode(jCas, 2, 5, "VBD");
        TreebankNode homeNode = TreebankNodeUtility.newNode(jCas, 6, 10, "NN");
        TreebankNode homenpNode = TreebankNodeUtility.newNode(jCas, "NP", homeNode);
        TreebankNode ranvpNode = TreebankNodeUtility.newNode(jCas, "VP", ranNode, homenpNode);
        TreebankNode topNode = TreebankNodeUtility.newNode(jCas, "S", inpNode, ranvpNode);
        CoveredTextExtractor textExtractor = new CoveredTextExtractor();
        TypePathExtractor tagExtractor = new TypePathExtractor(TreebankNode.class, "nodeType");
        SyntacticPathExtractor extractor;
        List<Feature> features;
        extractor = new SyntacticPathExtractor(tagExtractor);
        features = extractor.extract(jCas, iNode, ranNode);
        Assert.assertEquals(2, features.size());
        Assert.assertEquals("SyntacticPath(TypePath(NodeType))", features.get(0).getName());
        Assert.assertEquals("PRP::NP::S;;VP;;VBD", features.get(0).getValue());
        Assert.assertEquals("SyntacticPath_Length", features.get(1).getName());
        Assert.assertEquals(5, ((Long) features.get(1).getValue()).intValue());
        extractor = new SyntacticPathExtractor(tagExtractor);
        features = extractor.extract(jCas, topNode, homenpNode);
        Assert.assertEquals(2, features.size());
        Assert.assertEquals("SyntacticPath(TypePath(NodeType))", features.get(0).getName());
        Assert.assertEquals("S;;VP;;NP", features.get(0).getValue());
        Assert.assertEquals("SyntacticPath_Length", features.get(1).getName());
        Assert.assertEquals(3, ((Long) features.get(1).getValue()).intValue());
        extractor = new SyntacticPathExtractor(textExtractor, true);
        features = extractor.extract(jCas, homeNode, ranNode);
        Assert.assertEquals(2, features.size());
        Assert.assertEquals("PartialSyntacticPath(null)", features.get(0).getName());
        Assert.assertEquals("home::home::ran home", features.get(0).getValue());
        Assert.assertEquals("PartialSyntacticPath_Length", features.get(1).getName());
        Assert.assertEquals(3, ((Long) features.get(1).getValue()).intValue());
    }
}
