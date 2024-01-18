package com.uepb.interpreter;

public class GenericCalculator {

    public static String sum(Number n1, Number n2){
        final float n1Parsed = n1 instanceof Integer ? n1.intValue() : n1.floatValue();
        final float n2Parsed = n2 instanceof Integer ? n2.intValue() : n2.floatValue();
        final float result = n1Parsed + n2Parsed;

        return itsAPureInteger(result) ? Integer.toString((int) result) : Float.toString(result);
    }

    public static String sub(Number n1, Number n2){
        final float n1Parsed = n1 instanceof Integer ? n1.intValue() : n1.floatValue();
        final float n2Parsed = n2 instanceof Integer ? n2.intValue() : n2.floatValue();
        final float result = n1Parsed - n2Parsed;

        return itsAPureInteger(result) ? Integer.toString((int) result) : Float.toString(result);
    }

    public static String mult(Number n1, Number n2){
        final float n1Parsed = n1 instanceof Integer ? n1.intValue() : n1.floatValue();
        final float n2Parsed = n2 instanceof Integer ? n2.intValue() : n2.floatValue();
        final float result = n1Parsed * n2Parsed;

        return itsAPureInteger(result) ? Integer.toString((int) result) : Float.toString(result);
    }

    public static String div(Number n1, Number n2){
        final float n1Parsed = n1 instanceof Integer ? n1.intValue() : n1.floatValue();
        final float n2Parsed = n2 instanceof Integer ? n2.intValue() : n2.floatValue();
        final float result = n1Parsed / n2Parsed;

        return itsAPureInteger(result) ? Integer.toString((int) result) : Float.toString(result);
    }

    private static boolean itsAPureInteger(float n){
        int nInt = (int) n;
        float casasDecimais = n - nInt;
        return !(casasDecimais > 0f);
    }

    public static Number parseNumber(String number){
        try{
            return Integer.parseInt(number);
        }catch(NumberFormatException ex){
            try{
                return Float.parseFloat(number);
            }catch(NumberFormatException ex2){
                throw new RuntimeException("Operações aritiméticas só são feitas em (int<>int, float<>float)");
            }
        }
    }

}
