/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.controller;

import java.io.Serializable;  
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named; 
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.inventory.client.services.DinaServiceExcelFileUploadClient; 

/**
 *
 * @author idali
 */
@Named(value = "observationmap") 
@SessionScoped
public class ObservationMap  implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final int COLLECTION_ID_CFILE = 655361;
    private final int COLLECTION_ID_DFILE = 688128; 
    
    private final String OBSERVATION_COLLECTION_NAME = "Observation";
    private final String SPECIMENS_COLLECTION_NAME = "Specimens";
    
    private MindmapNode root; 
    private MindmapNode selectedNode;
     
    private String fileName;

    @Inject
    private DinaServiceExcelFileUploadClient client;
    
    @Inject
//    private ExcelFileHandler_1 fileHandler;
    
    @PostConstruct
    public void init() {
        logger.info("observation map init");  
        
//        this.fileName = fileHandler.getUploadedFileName();
//        String rootText = fileHandler.isCFile() ? OBSERVATION_COLLECTION_NAME : SPECIMENS_COLLECTION_NAME; 
//        root = new DefaultMindmapNode(rootText, "Excel file: " + fileName, "FFCC00", true);
    }
     
    public MindmapNode getRoot() {
        return root;
    }
 
    public MindmapNode getSelectedNode() {
        return selectedNode;
    }
    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }
 
//    public void onNodeSelect(SelectEvent event) {
//        logger.info("onNodeSelect");
//        
//        if(fileName == null || fileName.isEmpty()) {
//            fileName = fileHandler.getUploadedFileName();
//        }
//        
//        MindmapNode node = (MindmapNode) event.getObject();
//        
//        int collection = fileHandler.isCFile() ? COLLECTION_ID_CFILE : COLLECTION_ID_DFILE;
//         
//        //populate if not already loaded
//        if(node.getChildren().isEmpty()) { 
//            if(node.getParent() == null) {
//                Map<Integer, String> traps = client.getTraps(fileName, collection); 
//                 
//                traps.entrySet().parallelStream().forEach((entry) -> {
//                    root.addNode(new DefaultMindmapNode(entry.getValue(), entry.getKey(), "6e9ebf", true)); 
//                }); 
//            } else {
//                String label = node.getLabel();
//                if(label.startsWith("Trap ID") || label.startsWith("Sweden")) {
//                    Map<Integer, String> events = client.getEvents(fileName, Integer.parseInt(node.getData().toString()));
//                    events.entrySet().parallelStream().forEach((entry) -> {
//                        node.addNode(new DefaultMindmapNode(entry.getValue(), entry.getKey(), "82c542", true)); 
//                    });  
//                } else if(label.startsWith("Event ID")) {   
//                    Map<Integer, String> collectionObjects = client.getCollectionObjectsByEventId(fileName, Integer.parseInt(node.getData().toString()), collection);
//                    collectionObjects.entrySet().parallelStream().forEach((entry) -> {
//                        node.addNode(new DefaultMindmapNode(entry.getValue(), entry.getKey(), "fce24f", true));
//                    });
//                }  
//            } 
//        }  
//    }
     
    public void onNodeDblselect(SelectEvent event) {
        this.selectedNode = (MindmapNode) event.getObject();       
    }
}
