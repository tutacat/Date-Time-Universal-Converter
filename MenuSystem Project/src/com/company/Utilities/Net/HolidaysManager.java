package com.company.Utilities.Net;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HolidaysManager  {
    public static final String holidaysUrl = "https://www.timeanddate.com/holidays/";

    public List<String> Columns;
    public List<String> ColumnsContent;

    private Document doc;

    public HolidaysManager(){
        Columns = new ArrayList<>();
        ColumnsContent = new ArrayList <>();
    }

    public void Connect(String string) {
        try {
            Connection connect = Jsoup.connect(string);
            connect.maxBodySize(0);
            this.doc = connect.get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saving to file allows offline holidays checking
     * */
    public void loadZones(boolean saveToFile){
        Elements headLines = doc.getElementsByTag("div");
        for (Element element : headLines){
            if(element.hasClass("three columns")) {
                Elements h3Element = element.getElementsByTag("h3");
                Elements ulElement = element.getElementsByTag("ul");
                for (Element element1 : h3Element) {
                    if(element1.hasClass("mgt0")) {
                        Columns.add(element1.text());
                    }
                }
                for (Element element1 : ulElement){
                    Elements li = element1.getElementsByTag("li");
                    for (Element element3 : li){
                        ColumnsContent.add(element3.getElementsByTag("a").text());
                    }
                }
            }
        }
    }
}
