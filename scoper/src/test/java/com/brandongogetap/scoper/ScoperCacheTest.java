package com.brandongogetap.scoper;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ScoperCacheTest {

    private ScoperCacheRobot robot;

    @Before
    public void setUp() {
        robot = new ScoperCacheRobot();
    }

    @Test
    public void throwsIfNullComponent() {
        try {
            robot.initComponent("test", null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("component == null", e.getMessage());
        }
    }

    @Test
    public void throwsIfNoComponent() {
        try {
            robot.getComponent("test");
            fail();
        } catch (NullPointerException e) {
            assertEquals("No Component for: test", e.getMessage());
        }
    }

    @Test
    public void cachedComponentReturnedForTag() {
        Object component = new Object();
        robot.initComponent("test", component)
                .checkComponentEquals("test", component);
    }

    @Test
    public void componentNoLongerCachedAfterDestroyed() {
        Object component = new Object();

        try {
            robot.initComponent("test", component)
                    .destroyComponent("test")
                    .getComponent("test");
            fail();
        } catch (NullPointerException e) {
            assertEquals("No Component for: test", e.getMessage());
        }
    }

    @Test
    public void existingComponentReturnedInsteadOfCached() {
        Object component = new Object();
        Object newComponent = new Object();

        robot.initComponent("test", component)
                .initComponent("test", newComponent)
                .checkComponentEquals("test", component);
    }

    @Test
    public void putComponentStoresCorrectObject() {
        Object component = new Object();

        robot.putComponent("test", component)
                .checkComponentEquals("test", component);
    }

    @Test
    public void cacheCountIsCorrect() {
        Object component = new Object();
        robot.checkCacheCountEquals(0)
                .initComponent("first", component)
                .checkCacheCountEquals(1)
                .initComponent("second", component)
                .checkCacheCountEquals(2)
                .destroyComponent("first")
                .checkCacheCountEquals(1)
                .destroyComponent("second")
                .checkCacheCountEquals(0);
    }

    @Test
    public void contextCheckedForInstanceOfScoperContext() {
        Context context = Mockito.mock(Context.class);
        try {
            robot.getWithContext(context);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(
                    "Context does not have ScoperContext linked: " + context.getClass().getName(),
                    e.getMessage());
        }
    }
}