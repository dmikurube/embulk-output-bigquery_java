package org.embulk.output.bigquery_java;

import org.embulk.output.bigquery_java.config.BigqueryColumnOption;
import org.embulk.output.bigquery_java.config.BigqueryColumnOptionType;
import org.embulk.output.bigquery_java.config.PluginTask;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampFormatter;
import org.embulk.spi.time.TimestampParser;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BigqueryValueConverter {
    // default datetime format %Y-%m-%d %H:%M:%S.%6N
    // default timestamp format %Y-%m-%d %H:%M:%S.%6N
    // date format
    private static TimestampFormatter timestampFormat;

    private static String pattern;
    private static String timezone;
    private static TimestampParser parser;
    private static Timestamp ts;

    // TODO: refactor later
    public static void convertAndSet(ObjectNode node, String name, String src, BigqueryColumnOption columnOption, PluginTask task) {
        switch (BigqueryColumnOptionType.valueOf(columnOption.getType().get())) {
            case BOOLEAN:
                node.put(name, Boolean.parseBoolean(src));
                break;
            case INTEGER:
                node.put(name, Integer.parseInt(src));
                break;
            case FLOAT:
                node.put(name, Float.parseFloat(src));
                break;
            case STRING:
                node.put(name, src);
                break;
            case TIMESTAMP:
                pattern = columnOption.getTimestampFormat().orElse(task.getDefaultTimestampFormat());
                timezone = columnOption.getTimezone();
                parser = TimestampParser.of(pattern, timezone);
                ts = parser.parse(src);
                timestampFormat = TimestampFormatter.of("%Y-%m-%d %H:%M:%S.%6N", timezone);
                node.put(name, timestampFormat.format(ts));
                break;
            case DATETIME:
                pattern = columnOption.getTimestampFormat().orElse(task.getDefaultTimestampFormat());
                timezone = columnOption.getTimezone();
                parser = TimestampParser.of(pattern, timezone);
                ts = parser.parse(src);
                timestampFormat = TimestampFormatter.of("%Y-%m-%d %H:%M:%S.%6N", timezone);
                node.put(name, timestampFormat.format(ts));
                break;
            case DATE:
                pattern = columnOption.getTimestampFormat().orElse(task.getDefaultTimestampFormat());
                timezone = columnOption.getTimezone();
                parser = TimestampParser.of(pattern, timezone);
                ts = parser.parse(src);
                timestampFormat = TimestampFormatter.of("%Y-%m-%d", timezone);
                node.put(name, timestampFormat.format(ts));
                break;
            case RECORD:
                // TODO:
                node.put(name, src);
                break;
            default:
                throw new RuntimeException("Invalid data convert for String");
        }
    }
}
