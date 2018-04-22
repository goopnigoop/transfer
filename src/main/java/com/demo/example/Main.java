package com.demo.example;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String webappDirLocation = "webapp";
        Tomcat tomcat = new Tomcat();
        String webPort = "";

        if(args.length>0){
            webPort = args[0];
        }

        if(webPort == null || webPort.isEmpty()) {
            webPort = "8075";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
/*
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
*/

        org.h2.tools.Server server = org.h2.tools.Server.createTcpServer().start();

        tomcat.start();
        tomcat.getServer().await();



    }
}
