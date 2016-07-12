package com.bssys.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Locale;

@Ignore
@RunWith(Arquillian.class)
public class I18nTest extends Assert {
    @Inject
    private I18n i18n;

    @Deployment
    public static JavaArchive createTestArchive() {
      return ShrinkWrap.create(JavaArchive.class)
              .addPackage(I18n.class.getPackage())
              .addAsResource("persistence-test.xml", "META-INF/persistence.xml");
    }

    @Test
    public void testGetLocale() throws Exception {
        assertNotNull(i18n.getLocale("russian"));
        assertNotNull(i18n.getLocale("english"));
        assertNotNull(i18n.getLocale(""));
        assertNotNull(i18n.getLocale(null));
    }

    @Test
    public void testGetLocRes() throws Exception {
        assertEquals("Агент", i18n.getLocRes("umtpaytransfer", "TRANSFER_AGENT", null));
        assertEquals("Агент", i18n.getLocRes("umtpaytransfer", "TRANSFER_AGENT", null));
        assertEquals("Агент", i18n.getLocRes("umtpaytransfer", "TRANSFER_AGENT", new Locale("ru", "RU")));
        assertEquals("Agent", i18n.getLocRes("umtpaytransfer", "TRANSFER_AGENT", new Locale("en", "US")));
    }
}
