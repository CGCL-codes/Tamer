package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.MXQueryBigDecimal;
import ch.ethz.mxquery.datamodel.MXQueryDate;
import ch.ethz.mxquery.datamodel.MXQueryDateTime;
import ch.ethz.mxquery.datamodel.MXQueryDayTimeDuration;
import ch.ethz.mxquery.datamodel.MXQueryDouble;
import ch.ethz.mxquery.datamodel.MXQueryFloat;
import ch.ethz.mxquery.datamodel.MXQueryNumber;
import ch.ethz.mxquery.datamodel.MXQueryTime;
import ch.ethz.mxquery.datamodel.MXQueryYearMonthDuration;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.datamodel.xdm.BooleanToken;
import ch.ethz.mxquery.datamodel.xdm.DateTimeToken;
import ch.ethz.mxquery.datamodel.xdm.DateToken;
import ch.ethz.mxquery.datamodel.xdm.DayTimeDurToken;
import ch.ethz.mxquery.datamodel.xdm.DecimalToken;
import ch.ethz.mxquery.datamodel.xdm.DoubleToken;
import ch.ethz.mxquery.datamodel.xdm.FloatToken;
import ch.ethz.mxquery.datamodel.xdm.LongToken;
import ch.ethz.mxquery.datamodel.xdm.TextToken;
import ch.ethz.mxquery.datamodel.xdm.TimeToken;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.datamodel.xdm.YearMonthDurToken;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.functions.RequestTypeMulti;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.util.Set;

public class MaxMin extends TokenBasedIterator implements RequestTypeMulti {

    private boolean maxComputation = false;

    protected void init() throws MXQueryException {
        if (subIters[0] == null) {
            throw new IllegalArgumentException();
        }
        XDMIterator iter = subIters[0];
        TokenInterface tok1 = iter.next();
        int type = tok1.getEventType();
        if (tok1.isAttribute()) {
            type = Type.getAttributeValueType(type);
        }
        int workingType = Type.getEventTypeSubstituted(type, Context.getDictionary());
        if (subIters.length > 1) {
            XDMIterator collIter = subIters[1];
            TokenInterface collToken = collIter.next();
            if (collToken == Token.END_SEQUENCE_TOKEN || !Type.isTypeOrSubTypeOf(collToken.getEventType(), Type.STRING, Context.getDictionary())) throw new TypeException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "Wrong type for collation: " + Type.getTypeQName(collToken.getEventType(), Context.getDictionary()), loc);
            if (Type.isTypeOrSubTypeOf(type, Type.STRING, Context.getDictionary())) {
                String collUri = collToken.getText();
                Set collations = context.getCollations();
                if (!collations.contains(collUri)) throw new DynamicException(ErrorCodes.F0010_UNSUPPORTED_COLLATION, "Unsupported Collation", loc);
            }
        }
        int resType = type;
        switch(workingType) {
            case Type.END_SEQUENCE:
                currentToken = Token.END_SEQUENCE_TOKEN;
                break;
            case Type.INTEGER:
                evaluateNum(iter, tok1.getLong(), null, resType);
                break;
            case Type.UNTYPED_ATOMIC:
                evaluateNum(iter, Long.MIN_VALUE, new MXQueryDouble(tok1.getValueAsString()), Type.DOUBLE);
                break;
            case Type.DOUBLE:
            case Type.FLOAT:
            case Type.DECIMAL:
                evaluateNum(iter, Long.MIN_VALUE, tok1.getNumber(), resType);
                break;
            case Type.DATE_TIME:
                evaluateDateTime(iter, tok1.getDateTime());
                break;
            case Type.DATE:
                evaluateDate(iter, tok1.getDate());
                break;
            case Type.TIME:
                evaluateTime(iter, tok1.getTime());
                break;
            case Type.DAY_TIME_DURATION:
                evaluateDayTimeDuration(iter, tok1.getDayTimeDur());
                break;
            case Type.YEAR_MONTH_DURATION:
                evaluateYearMonthDuration(iter, tok1.getYearMonthDur());
                break;
            case Type.STRING:
            case Type.ANY_URI:
                evaluateString(iter, tok1.getText(), type);
                break;
            case Type.BOOLEAN:
                evaluateBoolean(iter, tok1.getBoolean());
                break;
            default:
                {
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type: " + Type.getTypeQName(type, Context.getDictionary()), loc);
                }
        }
    }

    private void evaluateBoolean(XDMIterator iter, boolean startBool) throws MXQueryException {
        boolean dominantVal = startBool;
        TokenInterface tok = iter.next();
        int type;
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            if (type != Type.BOOLEAN) {
                throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type: " + Type.getTypeQName(type, Context.getDictionary()) + " expected: xs:boolean", loc);
            }
            boolean curVal = tok.getBoolean();
            if (dominantVal != curVal) {
                if (maxComputation && curVal) dominantVal = true;
                if (!maxComputation && !curVal) dominantVal = false;
            }
            tok = iter.next();
        }
        if (dominantVal == true) currentToken = BooleanToken.TRUE_TOKEN; else currentToken = BooleanToken.FALSE_TOKEN;
    }

    private void evaluateNum(XDMIterator iter, long pInt, MXQueryNumber pDouble, int resType) throws MXQueryException {
        long maxInt = pInt;
        MXQueryNumber maxNumber = pDouble;
        int maxType = resType;
        long currInt;
        MXQueryNumber currNumber = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            if (tok.isAttribute()) {
                type = Type.getAttributeValueType(type);
            }
            maxType = Type.getNumericalOpResultType(type, maxType);
            int workingType = Type.getEventTypeSubstituted(type, Context.getDictionary());
            switch(workingType) {
                case (Type.INTEGER):
                    {
                        currInt = tok.getLong();
                        if (maxNumber == null || !maxNumber.isNaN()) {
                            if (maxNumber != null) {
                                if (handleMINorMAX(maxNumber.compareTo(currInt) <= 0)) {
                                    maxInt = currInt;
                                    maxNumber = null;
                                }
                            } else {
                                if (handleMINorMAX(currInt > maxInt)) {
                                    maxInt = currInt;
                                }
                            }
                        }
                    }
                    break;
                case Type.DOUBLE:
                case Type.FLOAT:
                case Type.DECIMAL:
                case Type.UNTYPED_ATOMIC:
                case Type.UNTYPED:
                    {
                        int genType;
                        if (type == Type.UNTYPED_ATOMIC || type == Type.UNTYPED) {
                            currNumber = new MXQueryDouble(tok.getValueAsString());
                            genType = Type.DOUBLE;
                            maxType = Type.getNumericalOpResultType(maxType, genType);
                        } else currNumber = tok.getNumber();
                        if (currNumber.isNaN()) {
                            if (maxNumber == null || !maxNumber.isNaN()) {
                                maxNumber = currNumber;
                            }
                        }
                        if (maxNumber != null) {
                            if (!maxNumber.isNaN()) {
                                if (handleMINorMAX(maxNumber.compareTo(currNumber) < 0)) {
                                    maxNumber = currNumber;
                                }
                            }
                        } else if (handleMINorMAX(currNumber.compareTo(maxInt) > 0)) {
                            maxNumber = currNumber;
                            maxInt = Long.MIN_VALUE;
                        }
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        switch(Type.getEventTypeSubstituted(maxType, Context.getDictionary())) {
            case Type.INTEGER:
                currentToken = new LongToken(maxType, null, maxInt);
                break;
            case Type.DOUBLE:
                if (maxNumber == null) currentToken = new DoubleToken(null, new MXQueryDouble(maxInt)); else if (maxNumber instanceof MXQueryDouble) currentToken = new DoubleToken(null, (MXQueryDouble) maxNumber); else currentToken = new DoubleToken(null, (MXQueryDouble) (new MXQueryDouble(0).add(maxNumber)));
                break;
            case Type.FLOAT:
                if (maxNumber == null) currentToken = new FloatToken(null, new MXQueryFloat(maxInt)); else if (maxNumber instanceof MXQueryFloat) currentToken = new FloatToken(null, (MXQueryFloat) maxNumber); else currentToken = new FloatToken(null, (MXQueryFloat) (new MXQueryFloat(0).add(maxNumber)));
                break;
            case Type.DECIMAL:
                if (maxNumber == null) currentToken = new DecimalToken(null, (new MXQueryBigDecimal(maxInt))); else currentToken = new DecimalToken(null, (MXQueryBigDecimal) maxNumber);
                ;
                break;
        }
    }

    private boolean handleMINorMAX(boolean val) {
        if (maxComputation) return val; else return !val;
    }

    private void evaluateDateTime(XDMIterator iter, MXQueryDateTime pDate) throws MXQueryException {
        MXQueryDateTime maxDateTime = pDate;
        MXQueryDateTime currDateTime = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            switch(type) {
                case (Type.DATE_TIME):
                    {
                        currDateTime = tok.getDateTime();
                        if (handleMINorMAX(maxDateTime.compareTo(currDateTime) < 0)) maxDateTime = currDateTime;
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        currentToken = new DateTimeToken(null, maxDateTime);
    }

    private void evaluateDate(XDMIterator iter, MXQueryDate pDate) throws MXQueryException {
        MXQueryDate maxDate = pDate;
        MXQueryDate currDate = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            switch(type) {
                case (Type.DATE):
                    {
                        currDate = tok.getDate();
                        if (handleMINorMAX(maxDate.compareTo(currDate) < 0)) maxDate = currDate;
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        currentToken = new DateToken(null, maxDate);
    }

    private void evaluateTime(XDMIterator iter, MXQueryTime pTime) throws MXQueryException {
        MXQueryTime maxTime = pTime;
        MXQueryTime currTime = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            switch(type) {
                case (Type.TIME):
                    {
                        currTime = tok.getTime();
                        if (handleMINorMAX(maxTime.compareTo(currTime) < 0)) maxTime = currTime;
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        currentToken = new TimeToken(null, maxTime);
    }

    private void evaluateDayTimeDuration(XDMIterator iter, MXQueryDayTimeDuration pDuration) throws MXQueryException {
        MXQueryDayTimeDuration maxDuration = pDuration;
        MXQueryDayTimeDuration currDuration = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            switch(type) {
                case (Type.DAY_TIME_DURATION):
                    {
                        currDuration = tok.getDayTimeDur();
                        if (handleMINorMAX(maxDuration.compareTo(currDuration) < 0)) maxDuration = currDuration;
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        currentToken = new DayTimeDurToken(null, maxDuration);
    }

    private void evaluateYearMonthDuration(XDMIterator iter, MXQueryYearMonthDuration pDuration) throws MXQueryException {
        MXQueryYearMonthDuration maxDuration = pDuration;
        MXQueryYearMonthDuration currDuration = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            switch(type) {
                case (Type.YEAR_MONTH_DURATION):
                    {
                        currDuration = tok.getYearMonthDur();
                        if (handleMINorMAX(maxDuration.compareTo(currDuration) < 0)) maxDuration = currDuration;
                    }
                    break;
                default:
                    throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            tok = iter.next();
        }
        currentToken = new YearMonthDurToken(null, maxDuration);
    }

    private void evaluateString(XDMIterator iter, String string, int resType) throws MXQueryException {
        String maxString = string;
        String currString = null;
        int type;
        TokenInterface tok = iter.next();
        while ((type = tok.getEventType()) != Type.END_SEQUENCE) {
            currString = tok.getText();
            if (type != resType) {
                throw new TypeException(ErrorCodes.F0028_INVALID_ARGUMENT_TYPE, "Invalid argument type", loc);
            }
            if (handleMINorMAX(maxString.compareTo(currString) < 0)) {
                maxString = currString;
            }
            tok = iter.next();
        }
        currentToken = new TextToken(null, maxString);
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(Type.ANY_ATOMIC_TYPE, Type.OCCURRENCE_IND_ZERO_OR_ONE);
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        MaxMin copy = new MaxMin();
        if (maxComputation) copy.setOperation("max");
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }

    public void setOperation(String type) throws MXQueryException {
        if (type.equals("max")) maxComputation = true; else if (type.equals("min")) maxComputation = false; else throw new MXQueryException(ErrorCodes.A0009_EC_EVALUATION_NOT_POSSIBLE, "Unsupported operation for MaxMin: " + type, loc);
    }

    public void setReturnType(int type) throws MXQueryException {
    }
}
