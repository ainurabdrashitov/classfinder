package com.aainur.classfinder;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ClassFinderTest {

    @Test
    void test_getSimpleClassName() {
        assertEquals("AaBbCc", ClassFinder.getSimpleClassName("AaBbCc"));
        assertEquals("AaBbCc", ClassFinder.getSimpleClassName("a.b.c.AaBbCc"));
        assertEquals("A", ClassFinder.getSimpleClassName("b.A"));
    }

    @Test
    void test_getNextUpperPosition() {
        assertEquals(-1, ClassFinder.getNextUpperPosition("abc", 0));
        assertEquals(0, ClassFinder.getNextUpperPosition("AaBbCc", 0));
        assertEquals(2, ClassFinder.getNextUpperPosition("AaBbCc", 1));
        assertEquals(2, ClassFinder.getNextUpperPosition("AaBbCc", 2));
        assertEquals(4, ClassFinder.getNextUpperPosition("AaBbCc", 3));
    }

    @Test
    void test_replaseIfPatternInLower() {
        assertEquals("AaBbCc", ClassFinder.replaseIfPatternInLower("AaBbCc"));
        assertEquals("ABC", ClassFinder.replaseIfPatternInLower("abc"));
        assertEquals("ABC", ClassFinder.replaseIfPatternInLower("ABC"));
    }

    @Test
    void test_splitLineByUppers() {
        assertEquals(Arrays.asList("A", "B", "C"), ClassFinder.splitLineByUppers("ABC"));
        assertEquals(Arrays.asList("Aa", "Bb", "Cc"), ClassFinder.splitLineByUppers("AaBbCc"));
        assertEquals(Arrays.asList("a", "Bb", "Cc"), ClassFinder.splitLineByUppers("aBbCc"));
        assertEquals(Collections.singletonList("abc"), ClassFinder.splitLineByUppers("abc"));
    }

    @Test
    void test_matchesSubString() {
        assertTrue(ClassFinder.matchesSubString("Abcd", ""));
        assertTrue(ClassFinder.matchesSubString("Abc", "Abc"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "A"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "Ab*"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "Ab*d"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "A*c*"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "*bcd"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "*bc"));
        assertTrue(ClassFinder.matchesSubString("Abcd", "Abcd*"));

        assertFalse(ClassFinder.matchesSubString("Arc", "Abc"));
        assertFalse(ClassFinder.matchesSubString("Arcd", "Abc"));
        assertFalse(ClassFinder.matchesSubString("Arcd", "Ab*"));
        assertFalse(ClassFinder.matchesSubString("Arcd", "Ab*d"));
        assertFalse(ClassFinder.matchesSubString("Arcd", "*bcd"));
        assertFalse(ClassFinder.matchesSubString("Arcd", "*bc"));
    }

    @Test
    void test_getNextMatchingSubstringIndex() {
        assertEquals(1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "B", 0));
        assertEquals(1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "B", 1));
        assertEquals(-1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "B", 2));
        assertEquals(-1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "Bb", 0));
        assertEquals(1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "B*", 0));
        assertEquals(-1, ClassFinder.getNextMatchingSubstringIndex(Arrays.asList("A", "B", "C"), "Bb*", 0));
    }

    @Test
    void test_matches() {
        assertTrue(ClassFinder.matches("c.d.FooBar", ""));
        assertTrue(ClassFinder.matches("a.b.FooBarBaz", "FooB*"));
        assertTrue(ClassFinder.matches("c.d.FooBar", "FooB*"));

        assertFalse(ClassFinder.matches("", "A"));
        assertFalse(ClassFinder.matches("codeborne.WishMaker", "FooB*"));
    }
}
