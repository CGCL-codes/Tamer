package org.identifylife.key.engine.core.repository.jdbc;

import org.identifylife.key.engine.context.Context;
import org.identifylife.key.engine.core.model.Taxon;
import org.identifylife.key.engine.core.model.TaxonSet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author dbarnier
 *
 */
@ContextConfiguration
public class JdbcContextRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JdbcContextRepository repository;

    @Test
    public void testCreate() {
        Context context = new Context("testContext");
        repository.create(context);
        Assert.assertNotNull(context.getId());
        Context created = repository.getByContextId(context.getContextId());
        Assert.assertNotNull(created);
        Assert.assertEquals(context.getContextId(), created.getContextId());
    }

    @Test
    public void testCreateOrUpdate() {
        Context context = new Context("testContext");
        repository.create(context);
        Assert.assertNotNull(context.getId());
        Long pk = context.getId();
        Context created = repository.getByContextId(context.getContextId());
        Assert.assertNotNull(created);
        Assert.assertEquals(pk, created.getId());
        Assert.assertEquals(context.getContextId(), created.getContextId());
        TaxonSet ts = new TaxonSet(new Taxon("taxon:1", "parent"));
        ts.addTaxon(new Taxon("taxon:1.1", "childA"));
        ts.addTaxon(new Taxon("taxon:1.2", "childB"));
        ts.addTaxon(new Taxon("taxon:1.3", "childC"));
        created.addTaxonSet(ts);
        repository.createOrUpdate(created);
        Context updated = repository.getByContextId(context.getContextId());
        Assert.assertNotNull(updated);
        Assert.assertEquals(pk, created.getId());
        Assert.assertEquals(context.getContextId(), updated.getContextId());
        Assert.assertNotNull(updated.getTaxonSets());
        TaxonSet uts = updated.getTaxonSet(new Taxon("taxon:1"));
        Assert.assertNotNull(uts);
        Assert.assertNotNull(uts.getTaxa());
        Assert.assertTrue(uts.getTaxa().size() == 3);
        Assert.assertTrue(uts.hasTaxon(new Taxon("taxon:1.2")));
    }

    @Test
    public void testDelete() {
        Context context = new Context("testContext");
        repository.create(context);
        Assert.assertNotNull(context.getId());
        repository.delete(context);
        Context created = repository.getByContextId(context.getContextId());
        Assert.assertNull(created);
    }
}
