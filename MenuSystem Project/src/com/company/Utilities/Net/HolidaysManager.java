package com.company.Utilities.Net;

import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Tuple;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Red;
import static java.time.LocalDate.now;

public class HolidaysManager  {
    /**
     * So this website works like this
     *
     * www.timeanddate.com/holidays/  -> this is where all locations are
     *                          .../name_of_the_country -> Shows us all the day and month were a certain holiday is
     *                          .../.../2017 -> the year
     * */
    public static final String holidaysUrl = "https://www.timeanddate.com/holidays/";

    /**
     * All the countries available in the website
     *
     * Around 230 entries
     * */
    public List<Tuple<String, List<String>>> Columns;

    public HolidaysManager(){
        Columns = new ArrayList<>();
    }

    Document doc;

    private Document Connect(String string) {
        try {
            Connection connect = Jsoup.connect(string);
            connect.maxBodySize(0);
            doc = connect.get();
            return doc;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saving to file allows offline holidays checking -> maybe wont work :p
     * */
    public void loadZones(boolean saveToFile) {
        doc = Connect(holidaysUrl);
        Elements headLines;
        int i = 0;
        if (doc != null) {
            headLines = doc.getElementsByTag("div");
            for (Element element : headLines) {
                if (element.hasClass("three columns")) {
                    Elements h3Element = element.getElementsByTag("h3");
                    Elements ulElement = element.getElementsByTag("ul");
                    for (Element element1 : h3Element) {
                        if (element1.hasClass("mgt0")) {
                            Tuple t = new Tuple(element1.text(), new ArrayList<String>());
                            Columns.add(t);
                        }
                    }
                    for (Element element1 : ulElement) {
                        Elements li = element1.getElementsByTag("li");
                        for (Element element3 : li) {
                            String a = element3.getElementsByTag("a").text().toLowerCase();
                            Columns.get(i).b.add(a);
                        }
                        i++;
                    }
                }
            }
        }
        else {
            ColorfulConsole.WriteLine(Red(Underline),"Error loading page: " + holidaysUrl);
            return;
        }
        ColorfulConsole.WriteLine(Green(Underline), "Loaded: " + Columns.size() +
                " Countries from timeanddate.com/holidays");
    }

    public List<Tuple<LocalDate, String>> getHolidays(String country){
        return getHolidays(country, -1);
    }

    public List<Tuple<LocalDate, String>> getHolidays(String country, int year){
        String url;
        if(year == -1){
            //Using current timeZone year
            url = holidaysUrl + country;
            year = now().getYear();
        }
        else url = holidaysUrl + country + "/" + year;
        doc = Connect(url);
        if(doc == null)
        {
            ColorfulConsole.WriteLine(Red(Underline),"Error loading page: " + url);
            return null;
        }

        List<Tuple<LocalDate, String>> holidays = new ArrayList <>();
        LocalDate date = null;
        String holidayName = null;

        Elements tr = doc.getElementsByTag("tr");
        for (Element etr : tr){
            if(etr.hasClass("c0") || etr.hasClass("c1")){
                Elements th = etr.getElementsByTag("th");
                Elements td = etr.getElementsByTag("td");
                for (Element elementTh : th){
                    if(elementTh.hasClass("nw"))
                    {
                        //format Jan 1
                        String monthDay = elementTh.text();
                        //FullFormat yyyy MMM dd
                        String fullDate = (year + " " + monthDay);
                        DateTimeFormatter pattern = new DateTimeFormatterBuilder()
                                .parseCaseSensitive()
                                .appendPattern("yyyy MMM d")
                                .toFormatter(Locale.UK);
                        date = LocalDate.parse(fullDate, pattern);
                    }
                }

                for (Element elementTd : td){
                    Elements e = elementTd.getElementsByTag("a");
                    if(e.size() > 0){
                        holidayName = e.first().text();
                        //I can break here! html does only have 1 'a' element
                        break;
                    }
                }

                Tuple t = new Tuple(date, holidayName);
                holidays.add(t);
            }
        }
        return holidays;
    }
}