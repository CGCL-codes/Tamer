package net.sf.mpxj.mpx;

import java.util.HashMap;
import java.util.ListResourceBundle;
import net.sf.mpxj.CodePage;
import net.sf.mpxj.CurrencySymbolPosition;
import net.sf.mpxj.DateOrder;
import net.sf.mpxj.ProjectDateFormat;
import net.sf.mpxj.ProjectTimeFormat;

/**
 * This class defines the Chinese translation of resource required by MPX files.
 */
public final class LocaleData_zh extends ListResourceBundle {

    /**
    * {@inheritDoc}
    */
    @Override
    public Object[][] getContents() {
        return (RESOURCE_DATA);
    }

    private static final String[] TIME_UNITS_ARRAY_DATA = { "m", "h", "d", "w", "mon", "y", "%", "em", "eh", "ed", "ew", "emon", "ey", "e%" };

    private static final HashMap<String, Integer> TIME_UNITS_MAP_DATA = new HashMap<String, Integer>();

    static {
        for (int loop = 0; loop < TIME_UNITS_ARRAY_DATA.length; loop++) {
            TIME_UNITS_MAP_DATA.put(TIME_UNITS_ARRAY_DATA[loop], Integer.valueOf(loop));
        }
    }

    private static final String[] ACCRUE_TYPES_DATA = { "��ʼ", "����", "������" };

    private static final String[] RELATION_TYPES_DATA = { "FF", "FS", "SF", "SS" };

    private static final String[] PRIORITY_TYPES_DATA = { "Lowest", "Very Low", "Lower", "Low", "Medium", "High", "Higher", "Very High", "Highest", "Do Not Level" };

    private static final String[] CONSTRAINT_TYPES_DATA = { "Խ��Խ��", "Խ��Խ��", "���뿪ʼ��", "���������", "��������...��ʼ", "��������...��ʼ", "��������...���", "��������...���" };

    private static final String[] TASK_NAMES_DATA = { null, "���", "WBS", "��ټ���", "�ı�1", "�ı�2", "�ı�3", "�ı�4", "�ı�5", "�ı�6", "�ı�7", "�ı�8", "�ı�9", "�ı�10", "��ע", "��ϵ��", "��Դ��", null, null, null, "��ʱ", "�Ƚϻ�׼��ʱ", "ʵ�ʹ�ʱ", "ʣ�๤ʱ", "��ʱ����", "��ʱ��ɰٷֱ�", null, null, null, null, "�ɱ�", "�Ƚϻ�׼�ɱ�", "ʵ�ʳɱ�", "ʣ��ɱ�", "�ɱ�����", "�̶��ɱ�", "�ɱ�1", "�ɱ�2", "�ɱ�3", null, "����", "�Ƚϻ�׼����", "ʵ�ʹ���", "ʣ�๤��", "��ɰٷֱ�", "���ڲ���", "����1", "����2", "����3", null, "��ʼʱ��", "���ʱ��", "���翪ʼʱ��", "�������ʱ��", "���?ʼʱ��", "�������ʱ��", "�Ƚϻ�׼��ʼʱ��", "�Ƚϻ�׼���ʱ��", "ʵ�ʿ�ʼʱ��", "ʵ�����ʱ��", "��ʼʱ��1", "���ʱ��1", "��ʼʱ��2", "���ʱ��2", "��ʼʱ��3", "���ʱ��3", "��ʱ�����", "���ʱ�����", "��������", null, "ǰ������", "��������", "��Դ���", "��Դ��д", "Ψһ��ʶ��ǰ������", "Ψһ��ʶ�ź�������", null, null, null, null, "�̶�", "��̱�", "�ؼ�", "�ѱ��", "�ܳ�������", "BCWS", "BCWP", "SV", "CV", null, "��ʶ��", "��������", "�ӳ�", "����ʱ��", "��ʱ��", "���ȼ�'", "����Ŀ�ļ�", "��Ŀ", "Ψһ��ʶ��", "�������", null, null, null, null, null, null, null, null, null, null, "��־1", "��־2", "��־3", "��־4", "��־5", "��־6", "��־7", "��־8", "��־9", "��־10", "ժҪ", "������Ŀ", "������", "��������ͼ", null, "��������", "��ʼʱ��4", "���ʱ��4", "��ʼʱ��5", "���ʱ��5", null, null, null, null, null, "��ȷ��", "��Ҫ����", null, null, null, "����1", "����2", "����3", "����4", "����5", null, null, null, null, null, "ֹͣ", "������...���¿�ʼ", "���¿�ʼ" };

    private static final String[] RESOURCE_NAMES_DATA = { null, "���", "��д", "��", "����", "�ı�1", "�ı�2", "�ı�3", "�ı�4", "�ı�5", "��ע", "�����ʼ���ַ", null, null, null, null, null, null, null, null, "��ʱ", "�Ƚϻ�׼��ʱ", "ʣ�๤ʱ", "�Ӱ๤ʱ", "��ʱ��ɰٷֱ�", "��ʱ����", "��ʱ��ɰٷֱ�", null, null, null, "�ɱ�", "�Ƚϻ�׼�ɱ�", "ʵ�ʳɱ�", "ʣ��ɱ�", "�ɱ�����", null, null, null, null, null, "��ʶ��", "���λ", "��׼����", "�Ӱ����", "ÿ��ʹ�óɱ�", "�ɱ�����", "��ȷ���", "���ʹ����", "��׼����", "Ψһ��ʶ��", "������Ŀ", "������" };

    private static final Object[][] RESOURCE_DATA = { { LocaleData.FILE_DELIMITER, ";" }, { LocaleData.PROGRAM_NAME, "Microsoft Project for Windows" }, { LocaleData.FILE_VERSION, "4.0" }, { LocaleData.CODE_PAGE, CodePage.ZH }, { LocaleData.CURRENCY_SYMBOL, "" }, { LocaleData.CURRENCY_SYMBOL_POSITION, CurrencySymbolPosition.BEFORE }, { LocaleData.CURRENCY_DIGITS, Integer.valueOf(2) }, { LocaleData.CURRENCY_THOUSANDS_SEPARATOR, "." }, { LocaleData.CURRENCY_DECIMAL_SEPARATOR, "," }, { LocaleData.DATE_ORDER, DateOrder.DMY }, { LocaleData.TIME_FORMAT, ProjectTimeFormat.TWENTY_FOUR_HOUR }, { LocaleData.DATE_SEPARATOR, "/" }, { LocaleData.TIME_SEPARATOR, ":" }, { LocaleData.AM_TEXT, "" }, { LocaleData.PM_TEXT, "" }, { LocaleData.DATE_FORMAT, ProjectDateFormat.DD_MM_YYYY }, { LocaleData.BAR_TEXT_DATE_FORMAT, Integer.valueOf(0) }, { LocaleData.NA, "NA" }, { LocaleData.YES, "��" }, { LocaleData.NO, "��" }, { LocaleData.TIME_UNITS_ARRAY, TIME_UNITS_ARRAY_DATA }, { LocaleData.TIME_UNITS_MAP, TIME_UNITS_MAP_DATA }, { LocaleData.ACCRUE_TYPES, ACCRUE_TYPES_DATA }, { LocaleData.RELATION_TYPES, RELATION_TYPES_DATA }, { LocaleData.PRIORITY_TYPES, PRIORITY_TYPES_DATA }, { LocaleData.CONSTRAINT_TYPES, CONSTRAINT_TYPES_DATA }, { LocaleData.TASK_NAMES, TASK_NAMES_DATA }, { LocaleData.RESOURCE_NAMES, RESOURCE_NAMES_DATA } };
}
