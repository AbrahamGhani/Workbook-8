package com.pluralsight;

import com.github.lalyos.jfiglet.FigletFont;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Main {

    final static Logger logger = (Logger) LogManager.getLogger();

    public static void main(String[] args) {

logger.info("This is info log");
logger.warn("This is warning log");
logger.error("error log");
logger.debug("debug log");
logger.info("app finished");

try {
    // using default font standard.flf, obtained from maven artifact
    String asciiArt1 = FigletFont.convertOneLine("Am Sweeepy");
    System.out.println(asciiArt1);

    // using font font2.flf, located somewhere in classpath under path /flf/font2.flf
    String asciiArt2 = FigletFont.convertOneLine(FigletFont.class.getResourceAsStream("/flf/3D-ASCII.flf"), "Cypher King,");
    System.out.println(asciiArt2);
    String asciiArt3 = FigletFont.convertOneLine(FigletFont.class.getResourceAsStream("/flf/3D-ASCII.flf"), "The Intel Broker");
    System.out.println(asciiArt3);

//    asciiArt2 = FigletFont.convertOneLine("classpath:/flf/font2.flf", "hello");
//    System.out.println(asciiArt2);
//
//    // using font font3.flf, located in file system under path /opt/font3.flf
//    String asciiArt3 = FigletFont.convertOneLine(new File("/opt/font3.flf"), "hello");
//    System.out.println(asciiArt3);
//
//    asciiArt3 = FigletFont.convertOneLine("/opt/font3.flf", "hello");
//    System.out.println(asciiArt3);
//
//    // using font font4.flf, from www
//    String asciiArt4 = FigletFont.convertOneLine("http://myhost.com/font4.flf", "hello");
//    System.out.println(asciiArt4);

} catch (Exception e) {
    throw new RuntimeException(e);
}


    }
}