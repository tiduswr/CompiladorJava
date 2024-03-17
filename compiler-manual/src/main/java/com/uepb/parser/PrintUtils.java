package com.uepb.parser;

public class PrintUtils {
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[32m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void printlnGreen(String s){
        System.out.print(ANSI_GREEN_BACKGROUND + s + ANSI_RESET + "\n");
    }

    private static void printMethod(String s){
        System.out.print(ANSI_BLUE_BACKGROUND + s + ANSI_RESET);
    }

    public static void printCurrentMethod(){
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        String methodName = e.getMethodName();
        
        printMethod("🛠️    " + methodName + ": ");
    }

}
