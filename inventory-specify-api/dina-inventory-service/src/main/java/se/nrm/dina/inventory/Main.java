/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.inventory;

//import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm; 
import org.wildfly.swarm.datasources.DatasourcesFraction;  
//import org.wildfly.swarm.jaxrs.JAXRSArchive;
//import org.wildfly.swarm.keycloak.Secured;
//import org.wildfly.swarm.undertow.UndertowFraction;
import org.wildfly.swarm.undertow.WARArchive;
//import org.wildfly.swarm.keycloak.Secured;
//import org.wildfly.swarm.management.ManagementFraction;
//import org.wildfly.swarm.undertow.WARArchive;

/**
 *
 * @author idali
 */
public class Main {
    
    public static void main(String[] args) throws Exception {

        System.out.println("Running " + Main.class.getCanonicalName() + ".main");

        Swarm swarm = new Swarm();
         
          
        swarm.fraction(datasourceWithMysql());                                      // local
//          swarm.fraction(datasourceProductionWithMysql());                        // production
//        swarm.fraction(getManagementFraction());
        

//        swarm.fraction(UndertowFraction.createDefaultFraction().)    
                


        swarm.start().deploy();




//        WARArchive deployment = ShrinkWrap.create(WARArchive.class);
        
        
//        JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
//        deployment.as(Secured.class); 
//        swarm.start().deploy(deployment); 
    }
    
    private static DatasourcesFraction datasourceProductionWithMysql() {
        return new DatasourcesFraction()
                .jdbcDriver("com.mysql", (d) -> { 
                    d.driverClassName("com.mysql.jdbc.Driver");
                    d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    d.driverModuleName("com.mysql");
                })
                .dataSource("DinaDS", (ds) -> {
                    ds.initialPoolSize(12);
                    ds.minPoolSize(12);
                    ds.maxPoolSize(64); 
                    ds.checkValidConnectionSql("select 1"); 
                    ds.idleTimeoutMinutes(Long.valueOf(14400));
//                    ds.blockingTimeoutWaitMillis(Long.valueOf(3600));
                    ds.setTxQueryTimeout(Boolean.FALSE); 
                    ds.jndiName("java:/jdbc/DinaDS");
                    ds.driverName("com.mysql");
                    ds.connectionUrl("jdbc:mysql://dina-db.nrm.se:3306/dina_nrm?autoReconnect=true&useSSL=false");
                    ds.userName("dina");
                    ds.password("dorinda");
                });
    }
    

    private static DatasourcesFraction datasourceWithMysql() {
        return new DatasourcesFraction()
                .jdbcDriver("com.mysql", (d) -> { 
                    d.driverClassName("com.mysql.jdbc.Driver");
                    d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    d.driverModuleName("com.mysql");
                })
                .dataSource("DinaDS", (ds) -> {
                    ds.initialPoolSize(12);
                    ds.minPoolSize(12);
                    ds.maxPoolSize(64); 
                    ds.checkValidConnectionSql("select 1"); 
                    ds.idleTimeoutMinutes(Long.valueOf(14400));
//                    ds.blockingTimeoutWaitMillis(Long.valueOf(3600));
                    ds.setTxQueryTimeout(Boolean.FALSE); 
                    ds.jndiName("java:/jdbc/DinaDS");
                    ds.driverName("com.mysql");
                    ds.connectionUrl("jdbc:mysql://localhost:3306/dina_nrm?autoReconnect=true&useSSL=false");
                    ds.userName("root");
                    ds.password("friday18"); 
                });
    }
    
        
//    private static ManagementFraction getManagementFraction() { 
//        return ManagementFraction.createDefaultFraction()
//                        .httpInterfaceManagementInterface((iface) -> {
//                            iface.allowedOrigin("http://localhost:8080");
//                            iface.securityRealm("ManagementRealm");
//                        })
//                        .securityRealm("ManagementRealm", (realm) -> {
//                            realm.inMemoryAuthentication((authn) -> {
//                                authn.add("admin", "dina", true);
//                            });
//                            realm.inMemoryAuthorization((authz) -> {
//                                authz.add("admin", "dina");
//                            });
//                        });
//    }

}
