package com.may1635.steam_crawl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UserCrawlerTest extends TestCase {
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public UserCrawlerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Another Rigourous Test ;)
     */
    public void testApp() {
        assertTrue(true);
    }
}
