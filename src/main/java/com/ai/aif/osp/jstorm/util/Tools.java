package com.ai.aif.osp.jstorm.util;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Doubles;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.*;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;


/**
 * Utility class for various tool/helper functions.
 */
public final class Tools {

    public static final String ES_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String ES_DATE_FORMAT_NO_MS = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter ES_DATE_FORMAT_FORMATTER = DateTimeFormat.forPattern(Tools.ES_DATE_FORMAT).withZoneUTC();
    public static final DateTimeFormatter ISO_DATE_FORMAT_FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();

    private Tools() {
    }

    /**
     * Get the own PID of this process.
     *
     * @return PID of the running process
     */
    public static String getPID() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    /**
     * Converts integer syslog loglevel to human readable string
     *
     * @param level The level to convert
     * @return The human readable level
     */
    public static String syslogLevelToReadable(int level) {
        switch (level) {
            case 0:
                return "Emergency";
            case 1:
                return "Alert";
            case 2:
                return "Critical";
            case 3:
                return "Error";
            case 4:
                return "Warning";
            case 5:
                return "Notice";
            case 6:
                return "Informational";
            case 7:
                return "Debug";
        }

        return "Invalid";
    }

    /**
     * Converts integer syslog facility to human readable string
     *
     * @param facility The facility to convert
     * @return The human readable facility
     */
    public static String syslogFacilityToReadable(int facility) {
        switch (facility) {
            case 0:
                return "kernel";
            case 1:
                return "user-level";
            case 2:
                return "mail";
            case 3:
                return "system daemon";
            case 4:
            case 10:
                return "security/authorization";
            case 5:
                return "syslogd";
            case 6:
                return "line printer";
            case 7:
                return "network news";
            case 8:
                return "UUCP";
            case 9:
            case 15:
                return "clock";
            case 11:
                return "FTP";
            case 12:
                return "NTP";
            case 13:
                return "LOG audit";
            case 14:
                return "LOG alert";

            // TODO: Make user definable?
            case 16:
                return "local0";
            case 17:
                return "local1";
            case 18:
                return "local2";
            case 19:
                return "local3";
            case 20:
                return "local4";
            case 21:
                return "local5";
            case 22:
                return "local6";
            case 23:
                return "local7";
        }

        return "Unknown";
    }

    /**
     * Get a String containing version information of JRE, OS, ...
     *
     * @return Descriptive string of JRE and OS
     */
    public static String getSystemInformation() {
        String ret = System.getProperty("java.vendor");
        ret += " " + System.getProperty("java.version");
        ret += " on " + System.getProperty("os.name");
        ret += " " + System.getProperty("os.version");
        return ret;
    }

    /**
     * Decompress ZLIB (RFC 1950) compressed data
     *
     * @param compressedData A byte array containing the ZLIB-compressed data.
     * @return A string containing the decompressed data
     */
    public static String decompressZlib(byte[] compressedData) throws IOException {
        return decompressZlib(compressedData, Long.MAX_VALUE);
    }

    /**
     * Decompress ZLIB (RFC 1950) compressed data
     *
     * @param compressedData A byte array containing the ZLIB-compressed data.
     * @param maxBytes       The maximum number of uncompressed bytes to read.
     * @return A string containing the decompressed data
     */
    public static String decompressZlib(byte[] compressedData, long maxBytes) throws IOException {
        try (final ByteArrayInputStream dataStream = new ByteArrayInputStream(compressedData);
             final InflaterInputStream in = new InflaterInputStream(dataStream);
             final InputStream limited = ByteStreams.limit(in, maxBytes)) {
            return new String(ByteStreams.toByteArray(limited), StandardCharsets.UTF_8);
        }
    }

    /**
     * Decompress GZIP (RFC 1952) compressed data
     *
     * @param compressedData A byte array containing the GZIP-compressed data.
     * @return A string containing the decompressed data
     */
    public static String decompressGzip(byte[] compressedData) throws IOException {
        return decompressGzip(compressedData, Long.MAX_VALUE);
    }

    /**
     * Decompress GZIP (RFC 1952) compressed data
     *
     * @param compressedData A byte array containing the GZIP-compressed data.
     * @param maxBytes       The maximum number of uncompressed bytes to read.
     * @return A string containing the decompressed data
     */
    public static String decompressGzip(byte[] compressedData, long maxBytes) throws IOException {
        try (final ByteArrayInputStream dataStream = new ByteArrayInputStream(compressedData);
             final GZIPInputStream in = new GZIPInputStream(dataStream);
             final InputStream limited = ByteStreams.limit(in, maxBytes)) {
            return new String(ByteStreams.toByteArray(limited), StandardCharsets.UTF_8);
        }
    }

    /**
     * @return The current UTC UNIX timestamp.
     */
    public static int getUTCTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Get the current UNIX epoch with milliseconds of the system
     *
     * @return The current UTC UNIX timestamp with milliseconds.
     */
    public static double getUTCTimestampWithMilliseconds() {
        return getUTCTimestampWithMilliseconds(System.currentTimeMillis());
    }

    /**
     * Get the UNIX epoch with milliseconds of the provided millisecond timestamp
     *
     * @param timestamp a millisecond timestamp (milliseconds since UNIX epoch)
     * @return The current UTC UNIX timestamp with milliseconds.
     */
    public static double getUTCTimestampWithMilliseconds(long timestamp) {
        return timestamp / 1000.0;
    }

    public static String getLocalHostname() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            return "Unknown";
        }

        return addr.getHostName();
    }

    public static String getLocalCanonicalHostname() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            return "Unknown";
        }

        return addr.getCanonicalHostName();
    }

    public static int getTimestampDaysAgo(int ts, int days) {
        return (ts - (days * 86400));
    }

    public static String encodeBase64(final String what) {
        return BaseEncoding.base64().encode(what.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(final String what) {
        return new String(BaseEncoding.base64().decode(what), StandardCharsets.UTF_8);
    }

    public static String rdnsLookup(InetAddress socketAddress) throws UnknownHostException {
        return socketAddress.getCanonicalHostName();
    }

    public static String generateServerId() {
        return UUID.randomUUID().toString();
    }

    public static <T extends Comparable<? super T>> SortedSet<T> asSortedSet(Collection<T> c) {
        return ImmutableSortedSet.copyOf(c);
    }

    public static String buildElasticSearchTimeFormat(DateTime timestamp) {
        return timestamp.toString(ES_DATE_FORMAT_FORMATTER);
    }

    /**
     * The double representation of a UNIX timestamp with milliseconds is a strange, human readable format.
     * <p/>
     * This sucks and no format should use the double representation. Change GELF to use long. (zomg)
     */
    public static DateTime dateTimeFromDouble(double x) {
        return new DateTime(Math.round(x * 1000), DateTimeZone.UTC);
    }

    /**
     * Parse the string representation of an ISO 8601 date/timestamp with milliseconds and timezone.
     */
    public static DateTime dateTimeFromString(String s) {
        return ISO_DATE_FORMAT_FORMATTER.parseDateTime(s);
    }

    /**
     * Accepts our ElasticSearch time formats without milliseconds.
     *
     * @return A DateTimeFormatter suitable to parse an ES_DATE_FORMAT formatted string to a
     * DateTime Object even if it contains no milliseconds.
     */
    public static DateTimeFormatter timeFormatterWithOptionalMilliseconds() {
        // This is the .SSS part
        DateTimeParser ms = new DateTimeFormatterBuilder()
                .appendLiteral(".")
                .appendFractionOfSecond(1, 3)
                .toParser();

        return new DateTimeFormatterBuilder()
                .append(DateTimeFormat.forPattern(ES_DATE_FORMAT_NO_MS).withZoneUTC())
                .appendOptional(ms)
                .toFormatter();
    }

    public static DateTime nowUTC() {
        return new DateTime(DateTimeZone.UTC);
    }

    /**
     * @return The current date with timezone UTC.
     * @deprecated Use {@link #nowUTC()} instead.
     */
    @Deprecated
    public static DateTime iso8601() {
        return nowUTC();
    }

    public static String getISO8601String(DateTime time) {
        return ISODateTimeFormat.dateTime().print(time);
    }

    /**
     * Try to parse a date in ES_DATE_FORMAT format considering it is in UTC and convert it to an ISO8601 date.
     * If an error is encountered in the process, it will return the original string.
     */
    public static String elasticSearchTimeFormatToISO8601(String time) {
        try {
            DateTime dt = DateTime.parse(time, ES_DATE_FORMAT_FORMATTER);
            return getISO8601String(dt);
        } catch (IllegalArgumentException e) {
            return time;
        }
    }

    /**
     * @param target String to cut.
     * @param start  Character position to start cutting at. Inclusive.
     * @param end    Character position to stop cutting at. Exclusive!
     * @return Extracted/cut part of the string or null when invalid positions where provided.
     */
    public static String safeSubstring(String target, int start, int end) {
        if (target == null) {
            return null;
        }

        int slen = target.length();
        if (start < 0 || end <= 0 || end <= start || slen < start || slen < end) {
            return null;
        }

        return target.substring(start, end);
    }

    /**
     * Convert something to a double in a fast way having a good guess
     * that it is a double. This is perfect for MongoDB data that *should*
     * have been stored as doubles already so there is a high probability
     * of easy converting.
     *
     * @param x The object to convert to a double
     * @return Converted object, 0 if empty or something went wrong.
     */
    public static Double getDouble(Object x) {
        if (x == null) {
            return null;
        }

        if (x instanceof Double) {
            return (Double) x;
        }

        if (x instanceof String) {
            String s = x.toString();
            if (s == null || s.isEmpty()) {
                return null;
            }
        }

        /*
         * This is the last and probably expensive fallback. This should be avoided by
         * only passing in Doubles, Integers, Longs or stuff that can be parsed from it's String
         * representation. You might have to build cached objects that did a safe conversion
         * once for example. There is no way around for the actual values we compare if the
         * user sent them in as non-numerical type.
         */
        return Doubles.tryParse(x.toString());
    }

    public static Number getNumber(Object o, Number defaultValue) {
        if (o instanceof Number) {
            return (Number)o;
        }

        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Try to get the primary {@link InetAddress} of the primary network interface with
     * fallback to the local loopback address (usually {@code 127.0.0.1} or {@code ::1}.
     *
     * @return The primary {@link InetAddress} of the primary network interface
     * or the loopback address as fallback.
     * @throws SocketException if the list of network interfaces couldn't be retrieved
     */
    public static InetAddress guessPrimaryNetworkAddress() throws SocketException {
        final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        if (interfaces != null) {
            for (NetworkInterface interf : Collections.list(interfaces)) {
                if (!interf.isLoopback() && interf.isUp()) {
                    // Interface is not loopback and up. Try to get the first address.
                    for (InetAddress addr : Collections.list(interf.getInetAddresses())) {
                        if (addr instanceof Inet4Address) {
                            return addr;
                        }
                    }
                }
            }
        }

        return InetAddress.getLoopbackAddress();
    }

    public static String bytesToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format(Locale.ENGLISH, "%02x", b & 0xff)).append(' ');
        return sb.toString().trim();
    }

    /**
     * The default uncaught exception handler will print to STDERR, which we don't always want for threads.
     * Using this utility method you can avoid writing to STDERR on a per-thread basis
     */
    public static void silenceUncaughtExceptionsInThisThread() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread ignored, Throwable ignored1) {
            }
        });
    }

    public static class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private final Logger log;

        public LogUncaughtExceptionHandler(Logger log) {
            this.log = log;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error("Thread {} failed by not catching exception: {}.", t.getName(), e);
        }
    }
}
