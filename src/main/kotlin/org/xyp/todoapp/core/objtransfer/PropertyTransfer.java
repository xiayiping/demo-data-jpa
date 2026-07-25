package org.xyp.todoapp.core.objtransfer;


import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PropertyTransfer {
    private static final Logger logger = LoggerFactory.getLogger(PropertyTransfer.class);

    private static final String EL_PREFIX = "#{";
    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String EXP_SUFFIX = "}";
    private static final String PRESERVE_PREFIX = "\\";

    private static final SpelExpressionParser elParser = new SpelExpressionParser();

    private PropertyTransfer() {
    }

    public static Object parseExpression(Object context, String input) {
        if (null == context) {
            return null;
        }
        final PositionHolder pos = new PositionHolder(0);
        return parseExpressionString(context, input, pos);
    }

    private static Object parseExpressionString(@NonNull Object context, String input, PositionHolder pos) {
        if (!StringUtils.hasText(input) && 0 == pos.position) {
            return input;
        }
        if (null == input || input.isEmpty()) {
            return "";
        }

        List<Object> results = new ArrayList<>();
        while (pos.position < input.length()) {
            if (isPlaceholderBegin(input, pos.position)) {
                final var res = parsePlaceholder(context, input, pos);
                if (null != res) {
                    results.add(res);
                }
            } else if (isElBegin(input, pos.position)) {
                final var res = parseElExpression(context, input, pos);
                if (null != res) {
                    results.add(res);
                }
            } else if (isPreservedBegin(input, pos.position)) {
                final var res = parsePreserved(input, pos);
                results.add(res);
            } else if (input.startsWith(EXP_SUFFIX, pos.position) && pos.level > 0) {
                pos.position++;
                pos.level--;
                break;
            } else {
                final var res = input.substring(pos.position, pos.position + 1);
                results.add(res);
                pos.position++;
            }
        }

        if (results.isEmpty()) {
            return null;
        } else if (1 == results.size()) {
            return results.getFirst();
        } else {
            return results.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.joining());
        }
    }

    private static Object parsePreserved(String input, PositionHolder pos) {
        final int bg = pos.position + 1;
        pos.position = bg + 1;
        return input.substring(bg, pos.position);
    }

    private static Object parseElExpression(@NonNull Object context, String input, PositionHolder pos) {
        pos.position += 2;
        pos.level += 1;

        final var o = parseExpressionString(context, input, pos);
        if (null == o) {
            return null;
        }
        final String expressionString = o.toString();
        final Expression expression = elParser.parseExpression(expressionString);

        try {
            return expression.getValue(context, Object.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private static Object parsePlaceholder(@NonNull Object context, String input, PositionHolder pos) {
        pos.level += 1;
        pos.position += 2;

        final Object o = parseExpressionString(context, input, pos);
        if (null == o) {
            return null;
        }
        return PropertyUtil.getProperty(context, o.toString().trim());
    }

    private static boolean isPreservedBegin(String input, int position) {
        final int inputLen = input.length();
        return position < inputLen - 1
                && input.startsWith(PRESERVE_PREFIX, position);
    }

    private static boolean isElBegin(String input, int position) {
        final int inputLen = input.length();
        return position < inputLen - 2
                && input.startsWith(EL_PREFIX, position);
    }

    private static boolean isPlaceholderBegin(String input, int position) {
        final int inputLen = input.length();
        return position < inputLen - 2
                && input.startsWith(PLACEHOLDER_PREFIX, position);
    }

    private static class PositionHolder {
        int position;
        int level = 0;

        public PositionHolder(int position) {
            this.position = position;
        }
    }
}
