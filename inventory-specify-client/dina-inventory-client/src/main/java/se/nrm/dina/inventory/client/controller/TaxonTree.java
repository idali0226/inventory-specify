/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.controller;

import java.io.Serializable; 
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.inventory.client.services.DinaServiceExcelFileUploadClient;
import se.nrm.dina.inventory.client.services.SmtpServiceClient;
import se.nrm.dina.inventory.client.vo.TreeTaxa;

/**
 *
 * @author idali
 */
//@ManagedBean(name="taxontree")
@Named(value = "taxontree") 
@SessionScoped
public class TaxonTree implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
     
    private TreeNode root;
    private TreeNode selectedNode;
//    private TreeNode familyNode;
    private TreeNode rootNode;
//    private final String insecta = "Insecta"; 
    private final String hexapoda = "Hexapoda";
    private boolean isHighClass = true;
    
    
    @Inject
    private SmtpServiceClient client;
     
    @PostConstruct
    public void init() {
        logger.info("tree init");  
    }
    
    private DefaultTreeNode buildTreeNode(TreeTaxa s, TreeNode node) {  
        return new DefaultTreeNode(new TreeTaxa(s.getTaxaName(), false, s.getSyns()), node);
    }

    public void initTree() {
        logger.info("initTree");
        
        root = new DefaultTreeNode(new TreeTaxa("Root", false, null), null);  
        rootNode = new DefaultTreeNode(new TreeTaxa(hexapoda, false, null), root);
        List<TreeTaxa> children = client.getChildren(hexapoda); 
        
        children.stream()
                .forEach(s -> {
                    rootNode.getChildren().add(buildTreeNode(s, rootNode));
                }); 
        
        
//        logger.info("initTree : {}", family);
        
//        if(family.contains(":")) {
//            family = StringUtils.substringAfterLast(family, ":");
//        }
//         
//        int rank = client.isOrderAndUp(family, 11);
          
        
        
//        if(rank == 0) {
//           familyNode = new DefaultTreeNode(new TreeTaxa(insecta, false, null), root);
//           children = client.getChildren(insecta, "Taxon", 11); 
//        } else {
//            children = client.getChildren(family, "Taxon", 11);
//            familyNode = new DefaultTreeNode(new TreeTaxa(family, false, null), root);
//        }
 
          
//        children.stream()
//                .forEach(s -> {
//                    familyNode.getChildren().add(buildTreeNode(s, familyNode));
//                }); 
//        
//        
//        if(rank >= 110) {
//            isHighClass = false;
//            familyNode.setExpanded(true);
//            familyNode.getChildren().stream()
//                    .forEach(n -> {
//                        collapsingORexpanding(n, true);
//                    }); 
//        } 
    }
    
    private void collapsingORexpanding(TreeNode n, boolean option) {
        TreeTaxa treetaxa = (TreeTaxa) n.getData();
        String taxonName = StringUtils.substringBefore(treetaxa.getTaxaName(), " [");
        List<TreeTaxa> children = client.getChildren(taxonName);
        if(children.isEmpty()) {
//            n.setSelected(false);
        } else {
            children.stream()
                .forEach(s -> {
                    TreeNode node = buildTreeNode(s, n);
                    n.getChildren().add(node);
                    collapsingORexpanding(node, option);
                    n.setExpanded(option);
                });
        } 
    }
    
    public void expand() {
        logger.info("expand ");
        collapseOrExpandTree(rootNode, true);
    }
    
    public void collapse() {
        logger.info("collapse");
        collapseOrExpandTree(rootNode, false);
    }
    
    private void collapseOrExpandTree(TreeNode n, boolean option) {
        if(n.getChildren().isEmpty()) { 
        } else { 
            n.getChildren().stream().forEach((s) -> {
                collapseOrExpandTree(s, option);
            });
            n.setExpanded(option); 
        }
    }
 

    public boolean isIsHighClass() {
        return isHighClass;
    }
    
    
    
 
    public TreeNode getRoot() { 
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
     

    public void onNodeExpand(NodeExpandEvent event) {
        logger.info("onNodeExpand : {}", event.getTreeNode().toString());

        TreeNode node = event.getTreeNode();    
        List<TreeNode> nodes = node.getChildren();  
        
        nodes.stream()
                .forEach(n -> {
                    TreeTaxa treetaxa = (TreeTaxa) n.getData();
                    String taxonName = StringUtils.substringBefore(treetaxa.getTaxaName(), " [");
                    List<TreeTaxa> grandChildren = client.getChildren(taxonName);
                    grandChildren.stream()
                           .forEach(s -> {
                                n.getChildren().add(buildTreeNode(s, n));
                            });
                }); 
    }
 
    public void onNodeCollapse(NodeCollapseEvent event) { 
    }
 
    public void onNodeSelect(NodeSelectEvent event) { 
    }
 
    public void onNodeUnselect(NodeUnselectEvent event) { 
    } 
}
