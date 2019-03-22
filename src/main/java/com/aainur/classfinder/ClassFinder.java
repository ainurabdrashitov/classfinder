package com.aainur.classfinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

public class ClassFinder {

    public static void main(String[] args) throws IOException {
        final String fileName = args[0];
        final String pattern = args[1];
        final List<String> lines;
        try {
            lines = new ArrayList<>(readAllLines(get(fileName)));
        } catch (IOException e) {
            // ToDo: replace by logging
            out.println(String.format("Exception while reading file %s: %s", fileName, e.getClass()));
            throw e;
        }
        lines.stream()
                .filter(l -> matches(l.trim(), pattern.trim()))
                .map(String::trim)
                .forEach(out::println);
    }

    public static boolean matches(String line, String pattern) {
        if (line.contains(pattern)) {
            return true;
        }
        if (line.isEmpty()) {
            return false;
        }
        List<String> subLines = splitLineByUppers(getSimpleClassName(line));
        List<String> subPatterns = splitLineByUppers(replaseIfPatternInLower(pattern));
        int matchingSubstringIndex = 0;
        for (String subPattern : subPatterns) {
            final int prevMatchingSubstringIndex = matchingSubstringIndex;
            matchingSubstringIndex =
                    getNextMatchingSubstringIndex(subLines, subPattern, prevMatchingSubstringIndex);
            if (matchingSubstringIndex == -1) {
                return false;
            } else {
                matchingSubstringIndex++;
            }
        }
        return true;
    }

    static int getNextMatchingSubstringIndex(List<String> subLines, String subPattern, int from) {
        for (int i = from; i < subLines.size(); i++) {
            if (matchesSubString(subLines.get(i), subPattern)) {
                return i;
            }
        }
        return -1;
    }

    // subPattern can have wildcard '*' which match missing letters
    static boolean matchesSubString(String subLine, String subPattern) {
        if (subLine.contains(subPattern)) {
            return true;
        }
        if (!subPattern.contains("*")) {
            return false;
        }
        int subLinePosition = 0;
        String[] patternParts = subPattern.split("\\*");
        for (String patternPart : patternParts) {
            int prevSubLinePosition = subLinePosition;
            subLinePosition = subLine.indexOf(patternPart, prevSubLinePosition);
            if (subLinePosition == -1) {
                return false;
            } else {
                subLinePosition += patternPart.length();
            }
        }
        return true;
    }

    static List<String> splitLineByUppers(String line) {
        int currentPosition = 0;
        List<String> subLines = new ArrayList<>();
        while(currentPosition < line.length()) {
            final int prevPosition = currentPosition;
            currentPosition = getNextUpperPosition(line, prevPosition + 1);
            String subLine = line.substring(prevPosition, currentPosition);
            subLines.add(subLine);
        }
        return subLines;
    }

    static String replaseIfPatternInLower(String pattern) {
        if (getNextUpperPosition(pattern, 0) == -1) {
            return pattern.toUpperCase();
        } else {
            return pattern;
        }
    }

    static int getNextUpperPosition(String line, int from) {
        int i;
        for (i = from; i < line.length(); i++) {
            if (Character.isUpperCase(line.charAt(i))) {
                return i;
            }
        }
        if (from != 0) {
            return i;
        } else {
            return -1;
        }
    }

    static String getSimpleClassName(String className) {
        String[] domains = className.split("\\.");
        return domains[domains.length - 1];
    }
}
