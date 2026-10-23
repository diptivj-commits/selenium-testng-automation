package tests;

import org.testng.annotations.*;

public class ExecutionSequenceTest {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("1. BeforeSuite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("2. BeforeTest");
    }

    @BeforeClass
    public void beforeClass() {
        System.out.println("3. BeforeClass");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("4. BeforeMethod");
    }

    @Test(priority = 1)
    public void firstTest() {
        System.out.println("5. First test");
    }

    @Test(priority = 2)
    public void secondTest() {
        System.out.println("5. Second test");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("6. AfterMethod");
    }

    @AfterClass
    public void afterClass() {
        System.out.println("7. AfterClass");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("8. AfterTest");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("9. AfterSuite");
    }
}