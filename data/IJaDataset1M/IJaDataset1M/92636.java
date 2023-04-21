package net.simpleframework.content.bbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.propeditor.EComponentType;
import net.simpleframework.web.page.component.ui.propeditor.FieldComponent;
import net.simpleframework.web.page.component.ui.propeditor.PropEditorBean;
import net.simpleframework.web.page.component.ui.propeditor.PropField;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsForumHandle extends DefaultCatalogHandle {

    @Override
    public Class<? extends IIdBeanAware> getEntityBeanClass() {
        return BbsForum.class;
    }

    @Override
    public IBbsApplicationModule getApplicationModule() {
        return BbsUtils.applicationModule;
    }

    @Override
    protected ExpressionValue getBeansSQL(final ComponentParameter compParameter, final Object parentId) {
        final ArrayList<Object> al = new ArrayList<Object>();
        final StringBuilder sql = new StringBuilder();
        if (parentId == null) {
            sql.append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "parentid"));
        } else {
            sql.append("parentid=?");
            al.add(parentId);
        }
        return new ExpressionValue(sql.toString(), al.toArray());
    }

    @Override
    public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
        if (beanClazz.isAssignableFrom(BbsForum.class)) {
            final Object[] values = ev.getValues();
            if (BbsUtils.getTableEntityManager(BbsTopic.class).getCount(new ExpressionValue(DbUtils.getIdsSQLParam("catalogid", values.length), values)) > 0) {
                throw HandleException.wrapException(LocaleI18n.getMessage("BbsForumHandle.0"));
            }
        }
        super.doDelete(compParameter, ev, beanClazz);
    }

    @Override
    public Collection<PropField> getPropFields(final ComponentParameter compParameter, final PropEditorBean formEditor) {
        final ArrayList<PropField> al = new ArrayList<PropField>(formEditor.getFormFields());
        final PropField pf = new PropField();
        final FieldComponent fc = new FieldComponent();
        fc.setName("_showTags");
        fc.setDefaultValue("true");
        fc.setType(EComponentType.checkbox);
        pf.getFieldComponents().add(fc);
        pf.setLabel(LocaleI18n.getMessage("BbsForumHandle.1"));
        al.add(3, pf);
        return al;
    }

    @Override
    public Map<String, Object> doSavePropEditor(final ComponentParameter compParameter) {
        final Map<String, Object> data = super.doSavePropEditor(compParameter);
        data.put("showTags", ConvertUtils.toBoolean(compParameter.getRequestParameter("_showTags"), false));
        return data;
    }

    @Override
    public void doLoadPropEditor(final PageParameter pageParameter, final Catalog catalog, final Map<String, Object> dataBinding) {
        super.doLoadPropEditor(pageParameter, catalog, dataBinding);
        dataBinding.put("_showTags", ((BbsForum) catalog).isShowTags());
    }
}
