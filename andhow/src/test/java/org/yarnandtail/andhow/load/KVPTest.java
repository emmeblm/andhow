package org.yarnandtail.andhow.load;

import static org.junit.Assert.*;
import org.junit.Test;
import org.yarnandtail.andhow.api.ParsingException;

/**
 *
 * @author eeverman
 */
public class KVPTest {
	
	@Test
	public void splitKVPGoodEmptyArgs() throws Exception {
		assertEquals(KVP.NULL_KVP, KVP.splitKVP("", KeyValuePairLoader.KVP_DELIMITER));
		assertEquals(KVP.NULL_KVP, KVP.splitKVP("    ", KeyValuePairLoader.KVP_DELIMITER));
		assertEquals(KVP.NULL_KVP, KVP.splitKVP("   \t ", KeyValuePairLoader.KVP_DELIMITER));
		assertEquals(KVP.NULL_KVP, KVP.splitKVP("   \t\n\r\f ", KeyValuePairLoader.KVP_DELIMITER));
		assertEquals(KVP.NULL_KVP, KVP.splitKVP("   \b ", KeyValuePairLoader.KVP_DELIMITER));
	}
	
	@Test
	public void splitKVPGoodFlagNames() throws Exception {
		
		KVP kvp = null;
		
		kvp = KVP.splitKVP("flag", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("1", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("1", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("1a1", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("1a1", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("  1a1   ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("1a1", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("\"1\"", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("\"1\"", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("\'1\'", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("\'1\'", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP(" \t!@#$%^&*()_+-{var}[OPTION]\\~`<br>,.?/|  \t\n\r\f ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("!@#$%^&*()_+-{var}[OPTION]\\~`<br>,.?/|", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("  1\t1   ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("1\t1", kvp.getName());
		assertNull(kvp.getValue());
	}
	
	@Test
	public void splitKVPGoodNameAndValues() throws Exception {
		
		KVP kvp = null;
		
		kvp = KVP.splitKVP("flag=value", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertEquals("value", kvp.getValue());
		
		kvp = KVP.splitKVP("  \t flag \t  =  \t value \t  ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertEquals("value", kvp.getValue());
		
		kvp = KVP.splitKVP("   flag   =    ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = KVP.splitKVP("  fl \t ag \r = \n val \t ue  ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("fl \t ag", kvp.getName());
		assertEquals("val \t ue", kvp.getValue());
	}
	
	@Test
	public void splitKVPGoodNameAndValuesWithMultiDelims() throws Exception {
		
		KVP kvp = null;
		
		kvp = KVP.splitKVP("flag=val=ue", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertEquals("val=ue", kvp.getValue());
		
		kvp = KVP.splitKVP("flag=v=a=l=u=e", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertEquals("v=a=l=u=e", kvp.getValue());
		
		kvp = KVP.splitKVP("flag = v = a = l = u = e ", KeyValuePairLoader.KVP_DELIMITER);
		assertEquals("flag", kvp.getName());
		assertEquals("v = a = l = u = e", kvp.getValue());
		
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadEmptyFlagName() throws Exception {
		KVP.splitKVP("=value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadSpaceOnlyFlagName() throws Exception {
		KVP.splitKVP("  =value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadAllSpaceAndTabFlagName() throws Exception {
		KVP.splitKVP("   \t =value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadAllWhitespaceAndNewLinesFlagName() throws Exception {
		KVP.splitKVP("   \t\n\r\f = value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadAllBackspaceFlagName() throws Exception {
		KVP.splitKVP("\b =value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test(expected=ParsingException.class)
	public void splitKVPBadAllWhitespaceAndBackspaceFlagName() throws Exception {
		KVP.splitKVP("   \b  =value", KeyValuePairLoader.KVP_DELIMITER);
	}
	
	@Test
	public void newKVPByNameOnlyGoodName() throws Exception {
		KVP kvp = null;
		
		kvp = new KVP("flag");
		assertEquals("flag", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = new KVP("  \t fl\t \tag \t\r\n ");
		assertEquals("fl\t \tag", kvp.getName());
		assertNull(kvp.getValue());
		
		kvp = new KVP("   .    ");
		assertEquals(".", kvp.getName());
		assertNull(kvp.getValue());
	}
	
	@Test(expected=ParsingException.class)
	public void newKVPByNameOnlyBadEmptyName() throws Exception {
		new KVP("  \t \r\n ");
	}
	
	@Test
	public void newKVPByNameAndValueGood() throws Exception {
		KVP kvp = null;
		
		kvp = new KVP(" \n\r fl\tag \t ", " \t va\tlue \t");
		assertEquals("fl\tag", kvp.getName());
		assertEquals("va\tlue", kvp.getValue());
		
		kvp = new KVP(" \n\r fl\tag \t ", "   \t  \r \n ");
		assertEquals("fl\tag", kvp.getName());
		assertNull(kvp.getValue());
	}
	
	@Test(expected=ParsingException.class)
	public void newKVPByNameAndValueBadEmptyName() throws Exception {
		new KVP("  \t \r\n ", "value");
	}
}
