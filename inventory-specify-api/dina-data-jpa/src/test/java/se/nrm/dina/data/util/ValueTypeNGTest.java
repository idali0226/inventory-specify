/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.data.util;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass; 
import org.testng.annotations.BeforeClass; 
import org.testng.annotations.Test;

/**
 *
 * @author idali
 */
public class ValueTypeNGTest {
    
    public ValueTypeNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
 

    /**
     * Test of values method, of class ValueType.
     */
    @Test
    public void testValues() {
        System.out.println("values"); 
        ValueType[] result = ValueType.values();
        assertNotNull(result);
        assertEquals(result.length, 9); 
    }
 
}
