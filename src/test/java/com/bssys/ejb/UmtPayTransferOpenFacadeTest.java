package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.entity.UmtPayTransferOpen;
import com.bssys.test.DataTools;
import com.bssys.umt.TransferDir;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@Ignore
@RunWith(Arquillian.class)
public class UmtPayTransferOpenFacadeTest extends Assert {
    @BeforeClass
    public static void setUpClass() throws Exception {
        DataTools.createDBStructures("UMTPayTransferOpen.xml");

        DataTools.populateData("UMTPayTransferOpen-data.xml");
    }

    @Inject
    private UmtPayTransferOpenFacade facade;

  @Test
    public void testGetUniversalDirectionForTransfer() throws Exception {
        UmtPayTransferOpen transfer = new UmtPayTransferOpen();
        transfer.setSendertransfersys(0);
        transfer.setReceivertransfersys(0);
        int dir = facade.getUniversalDirectionForTransfer(transfer);
        assertEquals(TransferDir.DIR_INTERNAL, dir);

        transfer.setSendertransfersys(1);
        transfer.setReceivertransfersys(0);
        dir = facade.getUniversalDirectionForTransfer(transfer);
        assertEquals(TransferDir.DIR_INCOMING, dir);

        transfer.setSendertransfersys(0);
        transfer.setReceivertransfersys(1);
        dir = facade.getUniversalDirectionForTransfer(transfer);
        assertEquals(TransferDir.DIR_OUTGOING, dir);

        transfer.setSendertransfersys(1);
        transfer.setReceivertransfersys(1);
        dir = facade.getUniversalDirectionForTransfer(transfer);
        assertEquals(TransferDir.DIR_TRANSITIONAL, dir);
    }

    @Test
    public void testGetUniversalDirectionName() throws Exception {
        UmtPayTransferOpen transfer = new UmtPayTransferOpen();

        transfer.setSendertransfersys(0);
        transfer.setReceivertransfersys(0);
        String dirName = facade.getUniversalDirectionName(transfer, new Locale("ru", "RU"));
        assertEquals("внутренний", dirName);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("en", "US"));
        assertEquals("inner", dirName);

        transfer.setSendertransfersys(1);
        transfer.setReceivertransfersys(0);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("ru", "RU"));
        assertEquals("сторонний", dirName);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("en", "US"));
        assertEquals("external", dirName);

        transfer.setSendertransfersys(0);
        transfer.setReceivertransfersys(1);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("ru", "RU"));
        assertEquals("внешний", dirName);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("en", "US"));
        assertEquals("outer", dirName);

        transfer.setSendertransfersys(1);
        transfer.setReceivertransfersys(1);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("ru", "RU"));
        assertEquals("транзитный", dirName);
        dirName = facade.getUniversalDirectionName(transfer, new Locale("en", "US"));
        assertEquals("through", dirName);
    }

  @Test
    public void testGetDocumentNumberByIDRForUser() throws Exception {
        DboDocPK pk = new DboDocPK("11|735325|608453640");
        String docNumber = facade.getDocumentNumberByIDRForUser(pk);
        assertEquals("11010", docNumber);
        pk = new DboDocPK("11|735319|392113770");
        docNumber = facade.getDocumentNumberByIDRForUser(pk);
        assertEquals("11001", docNumber);
        pk = new DboDocPK("11|735319|392113770");
        docNumber = facade.getDocumentNumberByIDRForUser(pk);
        assertNull(docNumber);
        pk = new DboDocPK("12|735319|392113770");
        docNumber = facade.getDocumentNumberByIDRForUser(pk);
        assertNull(docNumber);
    }

  @Test
    public void testGetLastTransfers() throws Exception {
        List<Object[]> list = facade.getLastSendTransfers(0, 11);
        assertEquals(10, list.size());
        list = facade.getLastSendTransfers(1, 11);
        assertEquals(1, list.size());
        list = facade.getLastSendTransfers(10, 11);
        assertEquals(10, list.size());
        list = facade.getLastSendTransfers(100, 11);
        assertEquals(10, list.size());
    }

    @Test
    public void testGetSenderNameAndSurnameByIDR() throws Exception {
        DboDocPK pk = new DboDocPK("11|735325|608453640");
        String name = facade.getSenderNameAndSurnameByIDR(pk);
        assertEquals("sadf asdf", name);
        pk = new DboDocPK("11|735319|392113770");
        name = facade.getSenderNameAndSurnameByIDR(pk);
        assertEquals("sadf asdf", name);
        pk = new DboDocPK("1|7|3");
        name = facade.getSenderNameAndSurnameByIDR(pk);
        assertEquals("", name);
        name = facade.getSenderNameAndSurnameByIDR(null);
        assertEquals("", name);
    }

    @Test
    public void testGetReceiverNameAndSurnameByIDR() throws Exception {
        DboDocPK pk = new DboDocPK("11|735325|608453640");
        String name = facade.getReceiverNameAndSurnameByIDR(pk);
        assertEquals("Дуров Алексей", name);
        pk = new DboDocPK("11|735319|392113770");
        name = facade.getReceiverNameAndSurnameByIDR(pk);
        assertEquals("Дуров Алексей", name);
        pk = new DboDocPK("1|7|3");
        name = facade.getReceiverNameAndSurnameByIDR(pk);
        assertEquals("", name);
        name = facade.getReceiverNameAndSurnameByIDR(null);
        assertEquals("", name);
    }
}
