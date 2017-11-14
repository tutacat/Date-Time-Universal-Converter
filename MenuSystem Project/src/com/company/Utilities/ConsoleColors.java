package com.company.Utilities;

import static com.company.Main.EMPTY_STRING;

public class ConsoleColors {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    public static class AnsiColor
    {
        enum ColorCode {
            BLACK(0),
            RED(1),
            GREEN(2),
            YELLOW(3),
            BLUE(4),
            PURPLE(5),
            CYAN(6),
            WHITE(7);

            public int Code;
            ColorCode(int c){
                Code = c;
            }
        }

        public enum Modifier {
            Regular (1),
            Bold (2),
            Underline (3),
            Background (4);

            private static final String RegularCode = "\033[0;3_m";
            private static final String BoldCode = "\033[1;3_m";
            private static final String UnderLineCode = "\033[4;3_m";
            private static final String BackgroundCode = "\033[4_m";

            private int mc;

            Modifier(int code){
                mc = code;
            }

            public String ColoredText(ColorCode code){
                switch (mc){
                    case 1:
                        return SetTextColor(code, Modifier.RegularCode);
                    case 2:
                        return SetTextColor(code, Modifier.BoldCode);
                    case 3:
                        return SetTextColor(code, Modifier.UnderLineCode);
                    case 4:
                        return SetTextColor(code, Modifier.BackgroundCode);
                }
                return EMPTY_STRING;
            }


            private String SetTextColor(ColorCode colorNumber, String modifier){
                String c = modifier;
                return c.replace("_", String.valueOf(colorNumber.Code));
            }
        }


        public String Color;
        private AnsiColor(String ansiCode){
            Color = ansiCode;
        }

        public static AnsiColor White(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.WHITE));
        }

        public static AnsiColor Red(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.RED));
        }

        public static AnsiColor Green(Modifier modifier) {
            return new AnsiColor(modifier.ColoredText(ColorCode.GREEN));
        }

        public static AnsiColor Yellow(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.YELLOW));
        }

        public static AnsiColor Blue(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.BLUE));
        }

        public static AnsiColor Purple(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.PURPLE));
        }

        public static AnsiColor Cyan(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.CYAN));
        }

        public static AnsiColor Black(Modifier modifier){
            return new AnsiColor(modifier.ColoredText(ColorCode.BLACK));
        }
    }
}
