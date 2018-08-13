package com.example.jaminhu.airkeyboard;

public class MyPlayground {

    public static String chooseBestWord(String nGramWindow){
        String bestWord;


        return bestWord;
    }

    public static String getFingerSequence(String originalWord){

        String word = originalWord.toLowerCase();

        StringBuilder fingerSequence = new StringBuilder();

        for (int i=0; i< word.length(); i++){
            String currentChar = Character.toString(word.charAt(i));
            String currentFingerId = getFingerId(currentChar);
            fingerSequence.append(currentFingerId);
        }

        return fingerSequence.toString();
    }

    private static String getFingerId(String character){
        switch (character){
            case "q":
            case "a":
            case "z":
                return "0";
            case "w":
            case "s":
            case "x":
                return "1";
            case "e":
            case "d":
            case "c":
                return "2";
            case "r":
            case "f":
            case "v":
            case "t":
            case "g":
            case "b":
                return "3";
            case " ":
                return "5"; //could also be 5 but 6 is most likely for right handed people.
            case "y":
            case "h":
            case "n":
            case "u":
            case "j":
            case "m":
                return "6";
            case "i":
            case "k":
            case ",":
                return "7";
            case "o":
            case "l":
            case ".":
                return "8";
            case "p":
            case "ö":
            case "-":
            case "å":
            case "ä":
            case "'":
                return "9";
            default:
                return "?";
        }
    };
}

    /*
    probably useless code but took time to type out so here just in case
    private String[] finger1 = {"q", "a", "z"};
    private String[] finger2 = {"w", "s", "x"};
    private String[] finger3 = {"e", "d", "c"};
    private String[] finger4 = {"r", "f", "v", "t", "g", "b"};
    private String[] finger5 = {}; //space bar left thumb
    private String[] finger6 = {}; //space bar right thumb
    private String[] finger7 = {"y", "h", "n", "u", "j", "m"};
    private String[] finger8 = {"i", "k", ","};
    private String[] finger9 = {"o", "l", ""};
     */