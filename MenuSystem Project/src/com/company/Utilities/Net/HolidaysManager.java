package com.company.Utilities.Net;

import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.Delegate;
import com.company.Utilities.Events.Event;
import com.company.Utilities.Events.EventExecutor;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Tuple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Regular;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static java.time.LocalDate.now;

public class HolidaysManager implements EventListener {
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

    EventExecutor onSaveToFileProgressUpdated = new EventExecutor (0, new Delegate (void.class, float.class));

    @Event (eventExecutorCode = 0)
    public void setExecutor(float progress){
        ColorfulConsole.Write (Blue (Bold), "<[");
        for (int i = 0; i < 100; i += 10) {
            if(i < progress) {
                ColorfulConsole.Write (Green (Bold), "=");
            }else ColorfulConsole.Write (White (Regular), " ");
        }
        ColorfulConsole.Write (Blue (Bold), "]> " + progress + "%");
    }

    /**
     * Google GSON Used to parse all the holidays to Disk
     * */
    private Gson gson;

    public HolidaysManager(){
        gson = new GsonBuilder ().create ();
        Columns = new ArrayList<>();

        onSaveToFileProgressUpdated.RegisterListener (this);
    }

    private boolean isLoaded = false;

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
        if(isLoaded) {
            ColorfulConsole.WriteLine(Green(Underline), "Countries Already Loaded");
            return;
        }

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
                            String urlPart = element3.getElementsByTag ("a").attr ("href");
                            String[] parts = urlPart.split ("/");
                            String country = parts[2];
                            //String a = element3.getElementsByTag ("a").text ();
                            //a = a.replaceAll (" ", "-");
                            //a = a.toLowerCase (Locale.UK);
                            Columns.get (i).b.add (country);
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
        isLoaded = true;
        ColorfulConsole.WriteLine(Green(Underline), "Loaded: 230+ Countries from timeanddate.com/holidays");
        if(saveToFile)
            try {
                this.SaveToFile ();
            }
            catch (IOException e) {
                e.printStackTrace ();
            }
    }

    public List<Holiday> getHolidays(String country){
        return getHolidays(country, -1);
    }


    /**
     * @param country Country from a available list of countries
     * @param year the year to get all the holidays
     * @return A list with a Local Date ex: 2017-08-12 and the name of the holiday
     *                 eg: 2017-12-25 Christmas Day
     */
    public List<Holiday> getHolidays(String country, int year){
        String url;
        if(year == -1){
            //Using current timeZone year
            url = holidaysUrl + country;
            year = now().getYear();
        }
        else url = holidaysUrl + country + "/" + year;
        //ColorfulConsole.WriteLineFormatted ("{0}Connecting to: {1}"+url,
        //        Green (Regular), Green (Underline));
        doc = Connect(url);
        if(doc == null)
        {
            ColorfulConsole.WriteLine(Red(Underline),"Error loading page: " + url);
            return null;
        }

        List<Holiday> holidays = new ArrayList <>();
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
                        //   .../holidays/country/
                        holidayName = e.first().text();
                        //I can break here! html does only have 1 'a' element
                        break;
                    }
                }

                Holiday holiday = new Holiday (date, holidayName);
                holidays.add(holiday);
            }
        }
        return holidays;
    }

    public ArrayList LoadFromFile(String country){
        Path path = Paths.get (".\\src\\Resources\\" + country + ".json");
        ArrayList arrayList = this.gson.fromJson (path.toString (), ArrayList.class);

        if(arrayList == null)
            throw new NullPointerException ();

        if(arrayList.size () == 0)
            throw new EmptyStackException ();

        return arrayList;
    }

    public boolean checkSaves(){
        Path path = Paths.get (".\\src\\Resources\\");
        String[] length = new File (path.toUri ()).list ();
        int i = 0;
        if(length != null) {
            i = length.length;
        }
        return i >= 200;
    }

    float pr = 0;

    public void SaveToFile() throws IOException {
        if(!isLoaded)
            return;

        if(checkSaves ())
        {
            ColorfulConsole.WriteLine (Green (Underline), "Countries already saved to Disk");
            return;
        }

        for (Tuple <String, List <String>> stringListTuple : this.Columns) {
            List <String> stringList = stringListTuple.b;
            onSaveToFileProgressUpdated.Invoke (pr);
            ColorfulConsole.WriteLine (Green (Regular),"Downloading " + stringListTuple.a + " Zone...");
            for (String s : stringList) {
                List <Holiday> holidays = getHolidays (s);
                //Para cada pais criar uma file
                Path path = Paths.get (".\\src\\Resources\\" + s + ".json");
                Writer writer = new FileWriter (path.toString ());
                gson.toJson (holidays, writer);
                writer.close();
            }
            pr += 100 / this.Columns.size ();
            if(pr >= 99.0f)
                ColorfulConsole.WriteLine (Green (Regular),"Done!");
        }
    }
}
