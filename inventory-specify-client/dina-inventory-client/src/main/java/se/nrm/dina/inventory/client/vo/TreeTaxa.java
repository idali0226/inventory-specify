/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.vo;

import java.util.List;

/**
 *
 * @author idali
 */
public class TreeTaxa {
    
    private final String taxaName;
    private final boolean isSynomyn;
    private final List<String> syns;
    
    public TreeTaxa(String taxaName, boolean isSynomyn, List<String> syns) {
        this.taxaName = taxaName;
        this.isSynomyn = isSynomyn;
        this.syns = syns;
    }

    public String getTaxaName() {
        return taxaName;
    }

    public boolean isIsSynomyn() {
        return isSynomyn;
    }

    public List<String> getSyns() {
        return syns;
    } 
}
