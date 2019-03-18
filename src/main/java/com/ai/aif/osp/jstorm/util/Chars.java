package com.ai.aif.osp.jstorm.util;

/**
 * <em>Consider this class private.</em>
 */
public final class Chars {

    /** Carriage Return. */
    public static final char CR = '\r';

    /** Carriage Return. */
    public static final char UDASH = '_';

    /** Double Quote. */
    public static final char DQUOTE = '\"';

    /** Equals '='. */
    public static final char EQ = '=';

    /** Line Feed. */
    public static final char LF = '\n';

    /** Single Quote [']. */
    public static final char QUOTE = '\'';

    /** Space. */
    public static final char SPACE = ' ';

    /** Tab. */
    public static final char TAB = '\t';

    /** Dash. */
    public static final char DASH = '-';

    /** At. */
    public static final char AT = '@';

    /** Forward slash. */
    public static final char SLASH = '/';

    /** Backslash */
    public static final char BSLASH = '\\';

    /** Colon. */
    public static final char COLON = ':';

    /** COMMA. */
    public static final char COMMA = ',';

    /** Left brace */
    public static final char LBRACE = '{';

    /** Right brace */
    public static final char RBRACE = '}';

    /** Left bracket */
    public static final char LBRACKET= '[';

    /** Right bracket */
    public static final char RBRACKET = ']';


    private Chars() {
    }

    public static String asString(char oneChar){
        return String.valueOf(oneChar);
    }
}
