package com.ai.aif.osp.jstorm.util;

import com.google.gson.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 取包下面的CLASS名称，XML/JSON转换工具
 */
public class ConvertUtil {
    private static Logger log = Logger.getLogger(ConvertUtil.class);
    public static String PACKAGE_PRJ_NAME = "com.asiainfo";
    private static String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    public static Gson buildGson() {
        GsonBuilder gb = new GsonBuilder();
        //gb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASELOWER_CASE_WITH_DASHES);
        return gb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static String toJson(Object obj) {
        return buildGson().toJson(obj);
    }

    public static JsonElement toJsonTree(Object obj) {
        return buildGson().toJsonTree(obj);
    }

    /**
     * 知道内容是个数组，把json转成对象数组
     *
     * @param data
     * @param clazz
     * @return
     */
    public static <T> List<T> fromJsonArray(String data, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        JsonParser s = new JsonParser();
        JsonElement jsonE = s.parse(data);
        JsonArray array = jsonE.getAsJsonArray();
        Iterator<JsonElement> it = array.iterator();
        for (; it.hasNext(); ) {
            T o = buildGson().fromJson(it.next(), clazz);
            list.add(o);
        }
        return list;
    }

    /**
     * 知道内容是个数组，把json转成对象数组
     *
     * @param data
     * @param typeOfT
     * @return
     */
    public static <T> List<T> fromJsonArray(String data, Type typeOfT) {
        List<T> list = new ArrayList<>();
        if(data.startsWith("[")) {
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            Iterator<JsonElement> it = array.iterator();
            for (; it.hasNext(); ) {
                T o = buildGson().fromJson(it.next(), typeOfT);
                list.add(o);
            }
        }else{
            JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
            list.add(buildGson().fromJson(jsonObject, typeOfT));
        }
        return list;
    }

    /**
     * 取数组中第一个对象的属性值
     *
     * @param data
     * @param field_name
     * @return
     */
    public static String getJsonArrayFirstRowFieldValue(String data, String field_name) {
        JsonParser s = new JsonParser();
        JsonElement jsonE = s.parse(data);
        JsonArray array = jsonE.getAsJsonArray();
        if (array.size() > 0) {
            JsonElement item = array.get(0);
            JsonElement value = item.getAsJsonObject().get(field_name);
            return value.getAsString();
        }
        return null;
    }


    public static Object fromJson(String json, Class<?> clazz) {
        return buildGson().fromJson(json, clazz);
    }

    public static String toXML(Object obj) {
        return getXStreamConvert().toXML(obj).replaceAll("__", "_");
    }

    /**
     * 转为xml格式，包括xml头
     *
     * @param obj
     * @return
     */
    public static String toXMLFull(Object obj) {
        return xmlHeader + toXML(obj);
    }

    public static Object fromXML(String xml, Object root) {
        return getXStreamConvert().fromXML(xml, root);
    }

    public static Object fromXML(String xml) {
        return getXStreamConvert().fromXML(xml);
    }

    /**
     * XML转换工具,可能会有更多别名，不做单实例
     * 使用方法：
     * 1.换类名：stream.alias("blog", Blog.class);
     * 2.换标签：stream.aliasField("author", Blog.class, "writer");
     * 3.忽略标签:stream.addImplicitCollection(Blog.class, "entries");
     * 4.作为一个属性：stream.useAttributeFor(Blog.class, "writer");
     * 5.修改包名：stream.aliasPackage(name, pkgName);
     * 6.加转换类：stream.registerConverter(new AuthorConverter());
     * xstream是不知道怎么把author转化为字符串作为xml的一个属性的。
     * 为了解决这个问题，必须重写SingleValueConverter 方法。
     * Java代码
     * import com.thoughtworks.xstream.converters.SingleValueConverter;
     * public class AuthorConverter implements SingleValueConverter{
     * public Object fromString(String name) {
     * return new Author(name);
     * }
     * public String toString(Object obj) {
     * return ((Author)obj).getName();
     * }
     * public boolean canConvert(Class clazz) {
     * return clazz.equals(Author.class);
     * }
     * }
     *
     * @return
     */
    public static XStream getXStreamConvert() {
        XStream xs = new XStream(new DomDriver());
        Package[] ps = Package.getPackages();
        for (int i = 0; i < ps.length; i++) {
            if (ps[i].getName().indexOf(PACKAGE_PRJ_NAME) >= 0) {
                Set<Class<?>> clazz = ConvertUtil.getClasses(ps[i]);
                Iterator<Class<?>> it = clazz.iterator();
                while (it.hasNext()) {
                    Class<?> cl = it.next();
                    xs.alias(cl.getSimpleName(), cl);
                }
            }
        }
        return xs;
    }

    /**
     * 取得某一类所在包的所有类名 不含迭代
     *
     * @param classLocation
     * @param packageName
     * @return
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        //将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        int packageLength = packagePathSplit.length;
        for (int i = 0; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }


    public static Set<Class<?>> getClasses(Package pack) {

        //第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        if (pack == null)
            return classes;
        //获取包的名字 并进行替换
        String packageName = pack.getName();
        String packageDirName = packageName.replace('.', '/');

        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            System.out.println("getResources(" + packageDirName + ")==" + dirs.hasMoreElements());
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                System.out.println("url:====" + url.getPath());
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            log.trace("添加用户自定义视图类错误 找不到此类的.class文件", e);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.trace("在扫描用户定义视图时从jar包获取文件出错", e);
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e);
        }

        return classes;
    }

    /**
     * 转化字符串为十六进制编码
     *
     * @param s
     * @return
     */
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * @return
     */
    public static String toFixString(int i) {
        String str16 = Integer.toHexString(i);
        String FIX16 = "0x00000000";
        String out16 = FIX16.substring(0, 10 - str16.length()) + str16;
        return out16;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
                                                        final boolean recursive, Set<Class<?>> classes) {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            log.trace("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.trace("添加用户自定义视图类错误 找不到此类的.class文件", e);
                }
            }
        }
    }

    /**
     * JSON字符串特殊字符处理，比如：“\A1;1300”
     *
     * @param s
     * @return String
     */
    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 数字对象转换为Long值
     *
     * @param obj 数值对象
     * @return long值
     */
    public static long castLong(Object obj) {
        if (obj instanceof Number)
            return ((Number) obj).longValue();
        else if (obj != null)
            return Long.valueOf(Strings.castToString(obj));
        else
            return 0;

    }
}
