package com.company.Utilities.Net;

import com.company.App;
import com.company.Utilities.Colorfull_Console.ColorfulConsole;
import com.company.Utilities.Events.EventListener;
import com.company.Utilities.Tuple;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import java.util.stream.Stream;

import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.Colorfull_Console.ConsoleColors.AnsiColor.Modifier.Underline;
import static java.time.OffsetDateTime.now;

public class HolidaysManager implements EventListener {

    /**
     * So this website works like this
     *
     * Returns the public holidays from given country and year
     * http://publicholiday.azurewebsites.net/api/v1/get/{CountryCode}/{Year}
     * */
    public static final String holidaysUrl = "http://publicholiday.azurewebsites.net/Home/Countries";
    public static final String holidaysGetUrl = "http://publicholiday.azurewebsites.net/api/v1/get";

    /**
     * All the countries available in the website
     *
     * Around 230 entries
     * */
    public List<Tuple<String , String>> Countries;


    /**
     * Google GSON Used to parse all the holidays to Disk
     * */
    private Gson gson;
    private boolean isLoaded = false;
    private Document doc;
    Connection connection;

    public HolidaysManager(){
        gson = new GsonBuilder ().create ();
        Countries = new ArrayList<>();
    }

    private String buildGetURL(String countryCode, int year){
        String url;
        if(year <= 0)
            url = String.format ("%s/%s", holidaysGetUrl, countryCode);
        else url = String.format ("%s/%s/%d", holidaysGetUrl,countryCode,year);
        return url;
    }

    private Document Connect(String string) {
        try {
            connection = Jsoup.connect(string);
            connection.maxBodySize(0);
            doc = connection.get();
            return doc;
        }
        catch (IOException e) {
            return null;
        }
    }

    private JsonArray GetResponse(String string) {
        try {
            URL url = new URL (string);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect ();
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            return root.getAsJsonArray ();
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     * Saving to file allows offline holidays checking -> maybe wont work :p
     * */
    public void loadZones() {
        if(isLoaded) {
            ColorfulConsole.WriteLine(Green(Underline), "Countries Already Loaded");
            return;
        }

        doc = Connect(holidaysUrl);
        if (doc != null) {
           Elements table = doc.getElementsByTag ("table");
           table.forEach ((element) -> {
               if(element.hasClass ("table")){
                   element.children ().forEach ((children) -> {
                       for (Element tr : element.getElementsByTag ("tr")) {
                           Elements td = tr.getElementsByTag ("td");
                           String fullName = td.first ().text ();
                           String abv = td.get (1).text ();
                           Countries.add (new Tuple <> (abv, fullName));
                       }
                   });
               }
           });
        }
        else {
            ColorfulConsole.WriteLine(Red(Underline),"Error loading page: " + holidaysUrl);
            isLoaded = false;
            return;
        }
        isLoaded = true;
        ColorfulConsole.WriteLine(Green(Underline), "Loaded: " + Countries.size ()
                + "  Countries from " + holidaysUrl);
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
        if(year <= 0){
            year = now().getYear();
        }
        url = buildGetURL (country, year);

        //Only get official holidays
        //url += URL_OFFICIAL_NONWORKING_HOLIDAYS_CODE;

        ColorfulConsole.WriteLine (Purple (Bold), "Loading " + year + " " + country + " holidays.");
        ColorfulConsole.WriteLine (Purple (Bold), url);

        Tuple <Boolean, List> booleanArrayListTuple = tryLoadFromFile (country, year);
        //If we were able to load from a file! get it from there
        if(booleanArrayListTuple.a) {
            ColorfulConsole.WriteLine (Green (Bold), "File found.");
            return booleanArrayListTuple.b;
        }

        ColorfulConsole.WriteLine (Yellow (Bold), "File not found. Trying to Download instead");
        ColorfulConsole.WriteLine (Red (Bold), "Make sure you have a stable internet connection");
        //ColorfulConsole.WriteLineFormatted ("{0}Connecting to: {1}"+url,
        //        Green (Regular), Green (Underline));
        JsonArray response = GetResponse (url);
        if(response == null) {
            ColorfulConsole.WriteLine (Red (Underline), "Error loading page: " + url);
            return null;
        }

        List<Holiday> holidays = new ArrayList <> ();
        for (JsonElement jsonElement : response) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject ();
            Holiday h = new Holiday ();
            /*UGLY BUT WORKS*/
            h.Name = asJsonObject.get ("name").getAsString ();
            h.Date = asJsonObject.get ("date").getAsString ();
            h.LocalName = asJsonObject.get ("localName").getAsString ();
            h.CountryCode =asJsonObject.get ("countryCode").getAsString ();
            h.Fixed = asJsonObject.get ("fixed").getAsBoolean ();
            h.CountyOfficialHoliday = asJsonObject.get ("countyOfficialHoliday").getAsBoolean ();
            h.CountyAdministrationHoliday = asJsonObject.get ("countyAdministrationHoliday").getAsBoolean ();
            h.Global = asJsonObject.get ("global").getAsBoolean ();
            holidays.add (h);
        }

        if(App.SaveToFile)
            try {
                saveToFile (country, year, holidays);
            }
            catch (IOException e) {
                e.printStackTrace ();
            }

        return holidays;
    }

    public Tuple
            <Boolean, ArrayList
                    <Tuple
                            <Integer, List
                                    <Holiday>>>> tryLoadFromFile(String country, int yearFrom, int to){

        ArrayList<Tuple<Integer, List<Holiday>>> holidaysByYear = new ArrayList <> ();
        List arrayList;
        for (int i = yearFrom; i <= to; i++){

            if(!fileExists (country,i)) {
                ColorfulConsole.WriteLine (Red (Bold), "Error occurred while loading from File (Stopping)");
                ColorfulConsole.WriteLine (Red (Bold), "File does not Exist (Stopping)");
                return new Tuple <> (false, null);
            }

            try {
                arrayList = loadFromFile (country, i);
            }
            catch (NullPointerException | EmptyStackException | FileNotFoundException e){
                return new Tuple <> (false, null);
            }
            Tuple<Integer, List<Holiday>> resHolidays = new Tuple <> (i, arrayList);
            holidaysByYear.add (resHolidays);
        }
        return new Tuple <> (true, holidaysByYear);
    }

    public Tuple<Boolean, List> tryLoadFromFile(String country, int year){
        if(!fileExists (country,year))
            return new Tuple <> (false, null);
        List arrayList;
        try {
            arrayList = loadFromFile (country, year);
        }
        catch (NullPointerException | EmptyStackException | FileNotFoundException e){
            return new Tuple <> (false, null);
        }
        return new Tuple <> (true, arrayList);
    }

    public List loadFromFile(String country, int year) throws FileNotFoundException {
        Path path = Paths.get (".\\src\\Resources\\" + country + "_" + year + ".json");
        Type aClass = new TypeToken<List<Holiday>> () {}.getType ();
        FileReader reader = new FileReader(path.toString ());
        List arrayList = this.gson.fromJson (reader, aClass);

        if(arrayList == null)
            throw new NullPointerException ();

        if(arrayList.size () == 0)
            throw new EmptyStackException ();

        return arrayList;
    }

    public void saveToFile(String country, int year, List <Holiday>  result) throws IOException {
        if (!isLoaded)
            return;

        //Para cada pais criar uma file
        Path path = Paths.get (".\\src\\Resources\\" + country + "_" + year + ".json");
        Writer writer = new FileWriter (path.toString ());

        Type aClass = new TypeToken<List<Holiday>> () {}.getType ();
        gson.toJson (result, aClass, writer);
        writer.close ();
    }

    public String[] checkSaves(){
        Path path = Paths.get (".\\src\\Resources\\");
        if(path == null)
            return null;
        return new File (path.toUri ()).list ();
    }

    public boolean fileExists(String country, int year){
        String[] strings = checkSaves ();
        if(strings == null)
            return false;
        Stream <String> arrayList = Arrays.stream (strings);
        return arrayList.anyMatch ((st) -> st.contains (country + "_" + year));
    }
}
