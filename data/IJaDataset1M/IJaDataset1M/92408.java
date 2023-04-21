package twoadw.website.answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelibra.type.EasyDate;
import twoadw.TwoadwTest;
import twoadw.website.Website;
import twoadw.website.product.Product;
import twoadw.website.product.Products;
import twoadw.website.question.Question;
import twoadw.website.question.Questions;

/**
 * JUnit tests for Answers.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class AnswersTest {

    private static Product sampleProduct;

    private static Products products;

    private static Question sampleProductQuestion;

    private static Questions questions;

    private static Answers sampleProductQuestionAnswers;

    @BeforeClass
    public static void beforeTests() throws Exception {
        Website website = TwoadwTest.getSingleton().getTwoadw().getWebsite();
        products = website.getProducts();
        sampleProduct = products.createProduct("productNumber", "name");
        questions = website.getQuestions();
        sampleProductQuestion = questions.createQuestion(sampleProduct, "questionText");
        sampleProductQuestionAnswers = sampleProductQuestion.getAnswers();
    }

    @Before
    public void beforeTest() throws Exception {
        sampleProductQuestionAnswers.getErrors().empty();
    }

    @Test
    public void answerRequired() throws Exception {
        Answer ans1 = sampleProductQuestionAnswers.createAnswer(sampleProductQuestion, "answerText");
        Answer ans2 = sampleProductQuestionAnswers.createAnswer(sampleProductQuestion, "answerText");
        assertTrue(sampleProductQuestionAnswers.contain(ans1));
        assertTrue(sampleProductQuestionAnswers.contain(ans2));
        assertTrue(sampleProductQuestionAnswers.getErrors().isEmpty());
    }

    @Test
    public void answerTextRequired() throws Exception {
        Answer ans1 = sampleProductQuestionAnswers.createAnswer(sampleProductQuestion, null);
        assertFalse(sampleProductQuestionAnswers.contain(ans1));
        assertFalse(sampleProductQuestionAnswers.getErrors().isEmpty());
        assertNotNull(sampleProductQuestionAnswers.getErrors().getError("Answer.answerText.required"));
    }

    @Test
    public void answerTextlength() throws Exception {
        String answerText = "";
        while (answerText.length() <= 1020) {
            answerText += "1";
        }
        Answer ans1 = sampleProductQuestionAnswers.createAnswer(sampleProductQuestion, answerText);
        assertFalse(sampleProductQuestionAnswers.contain(ans1));
        assertFalse(sampleProductQuestionAnswers.getErrors().isEmpty());
        assertNotNull(sampleProductQuestionAnswers.getErrors().getError("Answer.answerText.length"));
    }

    @Test
    public void answerPublishedDefaultValue() throws Exception {
        Boolean bool = true;
        Answer ans1 = sampleProductQuestionAnswers.createAnswer(sampleProductQuestion, "answerText");
        assertEquals(bool, (Boolean) ans1.getPublished());
    }

    @After
    public void afterTest() throws Exception {
        for (Answer answer : sampleProductQuestionAnswers.getList()) {
            sampleProductQuestionAnswers.remove(answer);
        }
    }

    @AfterClass
    public static void afterTests() throws Exception {
        products.remove(sampleProduct);
        questions.remove(sampleProductQuestion);
        TwoadwTest.getSingleton().close();
    }
}
