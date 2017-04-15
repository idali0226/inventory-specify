/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named; 
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.inventory.client.services.DinaServiceExcelFileUploadClient;
import se.nrm.dina.inventory.client.vo.TreeObs;  

/**
 *
 * @author idali
 */
@Named(value = "obstree") 
@SessionScoped
public class ObservationTree implements Serializable  {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final int COLLECTION_ID_CFILE = 655361;
    private final int COLLECTION_ID_DFILE = 688128; 
    
    private final String OBSERVATION_COLLECTION_NAME = "Observation";
    private final String SPECIMENS_COLLECTION_NAME = "Specimens";
    
    private final String TRAP_ID = "Trap ID";
    private final String EVENT_ID = "Event ID";
    private final String SWEDEN = "Sweden";
    
    private TreeNode root;
    private TreeNode selectedNode;  
    
    private TreeNode obsNode;
    
    private String fileName;
    private int collection;
    
    @Inject
    private DinaServiceExcelFileUploadClient client;
    
//    @Inject
//    private ExcelFileHandler_1 fileHandler;
        
    @PostConstruct
    public void init() {
        logger.info("tree init");
        root = new DefaultTreeNode(new TreeObs("Root", 0), null);  
    }
    
    private DefaultTreeNode buildTreeNode(TreeObs t) {
        return new DefaultTreeNode(t, obsNode);
    }
    
    private TreeObs buildObsTree(Entry<Integer, String> entry) {
        return new TreeObs(entry.getValue(), entry.getKey());
    }

//    public void initTree(String fileName) {
//        
//        root = new DefaultTreeNode(new TreeObs("Root", 0), null);    
//        this.fileName = fileName;
//          
//        String nodeName = fileHandler.isCFile() ? OBSERVATION_COLLECTION_NAME : SPECIMENS_COLLECTION_NAME;
//        collection = fileHandler.isCFile() ? COLLECTION_ID_CFILE : COLLECTION_ID_DFILE;
//          
//        obsNode = new DefaultTreeNode(new TreeObs(nodeName, 0), root);   
//        
//        Map<Integer, String> traps = client.getTraps(fileName, collection); 
//        traps.entrySet().stream()
//                .map(this::buildObsTree)
//                .map(this::buildTreeNode) 
////                .map((entry) -> new DefaultTreeNode(entry, obsNode))
//                .forEach(n -> {
//                    obsNode.getChildren().add(n);
//                });  
//    }
// 
    public void onNodeExpand(NodeExpandEvent event) {
        logger.info("onNodeExpand : {}", event.getTreeNode().toString());
        
//        int collection = fileHandler.isCFile() ? COLLECTION_ID_CFILE : COLLECTION_ID_DFILE; 
        TreeNode node = event.getTreeNode(); 
          
        List<TreeNode> nodes = node.getChildren();

        nodes.stream()
                .forEach((n) -> {
                    TreeObs obs = (TreeObs)n.getData();

                    Map<Integer, String> map = new HashMap();
                    if(obs.getName().startsWith(TRAP_ID) || obs.getName().startsWith(SWEDEN)) {
                        map = client.getEvents(fileName,  obs.getId() );
                    } else if(obs.getName().startsWith(EVENT_ID)) {
                        map = client.getCollectionObjectsByEventId(fileName, obs.getId(), collection);
                    } 
                    
                    map.entrySet().stream()
                            .forEach(e -> {
                                TreeNode childNode = new DefaultTreeNode(new TreeObs(e.getValue(), e.getKey()), n);
                                n.getChildren().add(childNode);
                            });
                            
//                    for(Map.Entry<Integer, String> entry : map.entrySet()) {
//                        TreeNode childNode = new DefaultTreeNode(new TreeObs(entry.getValue(), entry.getKey()), n);
//                        n.getChildren().add(childNode);
//                    }
                });  
    }
    
        
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
 
    public void onNodeCollapse(NodeCollapseEvent event) { 
    }
 
    public void onNodeSelect(NodeSelectEvent event) { 
    }
 
    public void onNodeUnselect(NodeUnselectEvent event) { 
    }

    
    public TreeNode getRoot() { 
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

}
