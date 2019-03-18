package com.ai.aif.osp.jstorm.util;

public class Objects {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean nonNull(Object o) {
        return o != null;
    }

    public static <T> T getDefault(T t, T defaultVal) {
        if (t == null) {
            return defaultVal;
        }
        return t;
    }

    /**
     * cast object to Long safety
     * @param o the object
     * @return Long value for <code>o</code> parameter if cast successfully
     */
    public static Long toLong(Object o){
        if(isNull(o)){
            return null;
        }
        if(o instanceof Long) {
            return (Long) o;
        }else{
            return Long.valueOf(o.toString());
        }
    }

    /**
     * cast object to Long safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return Long value for <code>o</code> parameter if cast successfully
     */
    public static Long toLong(Object o, Long defaultValue){
        Long value ;
        try{
            value = toLong(o);
        }catch (Exception e){
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * cast object to String safety
     * @param o the object
     * @return String value for <code>o</code> parameter if cast successfully
     */
    public static String toString(Object o){
        if(isNull(o)){
            return null;
        }
        if(o instanceof String) {
            return (String) o;
        }else{
            return o.toString();
        }
    }

    /**
     * cast object to String safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return String value for <code>o</code> parameter if cast successfully
     */
    public static String toString(Object o, String defaultValue){
        String value ;
        try{
            value = toString(o);
        }catch (Exception e){
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * cast object to Boolean safety
     * @param o the object
     * @return Boolean value for <code>o</code> parameter if cast successfully
     */
    public static Boolean toBoolean(Object o){
        if(isNull(o)){
            return Boolean.FALSE;
        }
        if(o instanceof Boolean) {
            return (Boolean) o;
        }else{
            return Boolean.valueOf(o.toString());
        }
    }

    /**
     * cast object to Boolean safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return Boolean value for <code>o</code> parameter if cast successfully
     */
    public static Boolean toBoolean(Object o, Boolean defaultValue){
        Boolean value ;
        try{
            if(o instanceof String){
                value = Boolean.valueOf(o.toString());
            }else{
                value = (Boolean) o;
            }
        }catch (Exception e){
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * cast object to Integer safety
     * @param o the object
     * @return Integer value for <code>o</code> parameter if cast successfully
     */
    public static Integer toInteger(Object o){
        if(isNull(o)){
            return null;
        }
        if(o instanceof Integer) {
            return (Integer) o;
        }else{
            return Integer.valueOf(o.toString());
        }
    }

    /**
     * cast object to Integer safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return Integer value for <code>o</code> parameter if cast successfully
     */
    public static Integer toInteger(Object o, Integer defaultValue){
        Integer value ;
        try{
            if(o instanceof String){
                value = Integer.valueOf(o.toString());
            }else{
                value = (Integer) o;
            }
        }catch (Exception e){
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link NullPointerException} if it is. This method
     * is designed primarily for doing parameter validation in methods and
     * constructors with multiple parameters, as demonstrated below:
     * <blockquote><pre>
     * public Foo(Bar bar, Baz baz) {
     *     this.bar = Objects.requireNonNull(bar, "bar must not be null");
     *     this.baz = Objects.requireNonNull(baz, "baz must not be null");
     * }
     * </pre></blockquote>
     *
     * @param obj     the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                NullPointerException} is thrown
     * @param <T> the type of the reference
     * @return {@code obj} if not {@code null}
     * @throws NullPointerException if {@code obj} is {@code null}
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

}
