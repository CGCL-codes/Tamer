package cat.jm.languages.facade;

import cat.jm.languages.model.*;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test.xml" })
@TransactionConfiguration(transactionManager = "englishTxManager", defaultRollback = true)
@Transactional
public class VocabularyFacadeTest {

    @Autowired
    private VocabularyFacade vocabularyFacade;

    @Test
    public void create() {
        Vocabulary vocabulary = new Vocabulary();
        Integer id = new Integer(Integer.parseInt("1733"));
        vocabulary.setId(id);
        String english = "ILncWKkpCoTQiIqkQmMHHDmDgUfgQUWsmIaUZiLFyVGzMCyaTN";
        vocabulary.setEnglish(english);
        String catalan = "GNdoCfYSEAPYJHZJnzSvKFaUEIqImqTPtprJeNzLkROdEQhHbq";
        vocabulary.setCatalan(catalan);
        String spanish = "cCljdxPnNEAfzsKpzEBKWnhqiZniSMRVvhAjuAnzZklASkYbnV";
        vocabulary.setSpanish(spanish);
        vocabularyFacade.create(vocabulary);
        Vocabulary vocabulary2 = vocabularyFacade.find(vocabulary.getId());
        assertNotNull(vocabulary2);
        assertEquals(vocabulary, vocabulary2);
    }

    @Test
    public void remove() {
        Vocabulary vocabularyForRemove = vocabularyFacade.get().get(0);
        Integer id = vocabularyForRemove.getId();
        Vocabulary vocabulary = vocabularyFacade.find(id);
        assertNotNull(vocabulary);
        vocabularyFacade.remove(vocabulary);
        Vocabulary vocabulary2 = vocabularyFacade.find(id);
        assertNull(vocabulary2);
    }

    @Test
    public void removeById() {
        Vocabulary vocabularyForRemove = vocabularyFacade.get().get(0);
        Integer id = vocabularyForRemove.getId();
        Vocabulary vocabulary = vocabularyFacade.find(id);
        assertNotNull(vocabulary);
        vocabularyFacade.remove(id);
        Vocabulary vocabulary2 = vocabularyFacade.find(id);
        assertNull(vocabulary2);
    }

    @Test
    public void update() {
        Vocabulary vocabulary = vocabularyFacade.get().get(0);
        Integer id = vocabulary.getId();
        assertNotNull(vocabulary);
        String vocabulary_english = vocabulary.getEnglish();
        vocabulary.setEnglish("qtksJJOdwUzNlNSMegLcbGhvawEeSTTRaEnWJIPnoBvapHCoxI");
        String vocabulary_catalan = vocabulary.getCatalan();
        vocabulary.setCatalan("UjtAKyeJLUmnNJUrtUHnOXXCXSFBjviGBJzFXiXGcgPZaXsJvn");
        String vocabulary_spanish = vocabulary.getSpanish();
        vocabulary.setSpanish("GHMScOjhSdtcLPxWgcuUctEzxAlpsnCsLPFCQkooAVpGMZQWIg");
        vocabularyFacade.update(vocabulary);
        Vocabulary vocabulary2 = vocabularyFacade.get().get(0);
        assertNotNull(vocabulary2);
        assertNotSame(vocabulary_english, vocabulary2.getEnglish());
        assertNotSame(vocabulary_catalan, vocabulary2.getCatalan());
        assertNotSame(vocabulary_spanish, vocabulary2.getSpanish());
    }

    @Test
    public void get() {
        List<Vocabulary> vocabularyList = vocabularyFacade.get();
        assertFalse(vocabularyList.isEmpty());
    }

    @Test
    public void getPaginated() {
        Integer index = 0, maxResults = 2;
        List<Vocabulary> vocabularyList = vocabularyFacade.get(index, maxResults);
        assertFalse(vocabularyList.isEmpty());
        assertEquals(vocabularyList.size(), Long.parseLong(Integer.toString(maxResults)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmptyObject() {
        Vocabulary vocabulary = new Vocabulary();
        vocabularyFacade.create(vocabulary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullObject() {
        Vocabulary vocabulary = null;
        vocabularyFacade.create(vocabulary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullObject() {
        Vocabulary vocabulary = null;
        vocabularyFacade.remove(vocabulary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullIdObject() {
        Integer vocabularyId = null;
        vocabularyFacade.remove(vocabularyId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findNullObject() {
        Integer vocabularyId = null;
        vocabularyFacade.find(vocabularyId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateNullObject() {
        Vocabulary vocabulary = null;
        vocabularyFacade.update(vocabulary);
    }
}
