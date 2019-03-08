package com.is.dev.assessment;

import com.is.dev.assessment.util.*;
import com.is.dev.assessment.domain.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.*;
import com.opencsv.*;


public class Main {
    static HashSet alreadyProcessedSKUs, SKUsToProcessThisRun;
    static Map<String, Integer> dataAccuracy;
    static Map<String, String[]> finalEntryData;
    static Map<String, Integer> highestEntryDataSourceAccuracy;
    static Product[] alreadySavedProducts, finalProducts;

    public static void main(String[] args) {

       specifyDataAccuracy(new Integer[]{6,5}); //the number of integers should match the number of files to process in /data_To_Process. 
                                                // the numbers correspond to the file names in the folder sorted in ascending order.
       LoadVariables();
       ProcessDataFiles();
       saveEntriesAsProductFiles();
       bounceProductFiles();
    }
    
    public static void specifyDataAccuracy(Integer[] accuracies){
        dataAccuracy = new HashMap<String,Integer>();
        File[] files = new File("data_To_Process").listFiles();
        Arrays.sort( files );
        
        if (files.length != accuracies.length){
            System.out.println("accuracies list does not match number of data files to process. Please enter a vailid data accuracy list.");
            System.exit(1);
        }
        
        for (int x = 0; x < files.length; x++)
        {
            dataAccuracy.put( files[x].toString() , accuracies[x] );
        }
        WriteObjectToFile(dataAccuracy, "var/dataAccuracy");
    }
    
    public static void LoadVariables(){
        if (new File("var/processedSKUs").exists()){
            try{
            alreadyProcessedSKUs = (HashSet)ReadObjectFromFile( "var/processedSKUs" ).readObject();
            }
            catch (Exception e){System.out.println(e.toString());
            }
        }
        else{
            alreadyProcessedSKUs = new HashSet();
            System.out.println("already processed skus file not found. new file instantiated");
        }
        
        try{
            dataAccuracy = (HashMap)ReadObjectFromFile( "var/dataAccuracy" ).readObject();
        }
        catch (Exception ex){
            System.out.println("data accuracy file not found. Terminating program.");
            System.exit(1);
        }
        
        if (new File("products/savedProducts").exists()){
            try{
            alreadySavedProducts  = (Product[])ReadObjectFromFile("products/savedProducts").readObject();
            }
            catch(Exception e){System.out.println(e.toString());
               }
        }
        else {
            System.out.println("already saved Products List Not Found, passing null");
            alreadySavedProducts  = null;
        }
    }
    
    public static void ProcessDataFiles(){
        DetermineSkusToPRocess();
        ProcessFiles();
    }
    
    public static void DetermineSkusToPRocess() {
        SKUsToProcessThisRun = new HashSet();
        
        File[] files = new File("data_To_Process").listFiles();
        for (File file : files) {
            if (file.toString().contains(".csv")) {
                identifyNewSKU_CSV(file.toString());
            } else if (file.toString().contains(".tsv")) {
                identifyNewSKU_TSV(file.toString());
            } 
        }
        System.out.println(SKUsToProcessThisRun.size() + " = number Of Products processed this run" );
    }
    
    public static void identifyNewSKU_CSV(String filePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String [] nextLine;
            int headerPassed = 0;
            while (( nextLine = reader.readNext()) != null) {
                for (int x = 0; x < nextLine.length; x++ ){
                    if(headerPassed == 1)
                    {   if (x == 2)
                        {
                            if ( !alreadyProcessedSKUs.contains(nextLine[x]) )
                            {
                                SKUsToProcessThisRun.add( nextLine[x] );
                            }
                        }
                    }
                    if (x == 5)
                        headerPassed = 1;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }
    
    public static void identifyNewSKU_TSV(String fileName) {
        try {
            FileInputStream inStream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String lineFetched; String[] itemsFetched = new String[0]; int numLineFetched = 0, entryNum = 0;
            while ((lineFetched = br.readLine()) != null){
                if (numLineFetched == 0)
                    numLineFetched = 1;
                else
                    itemsFetched = lineFetched.split("\t");
                    for(int x = 0; x < itemsFetched.length; x++)
                    {
                        if(x ==2)
                        {
                            ////System.out.println(itemsFetched[x]);
                            if ( !alreadyProcessedSKUs.contains( itemsFetched[x]) )
                            {
                                SKUsToProcessThisRun.add( itemsFetched[x] );
                            }
                        }
                    }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public static void ProcessFiles(){
        finalEntryData = new HashMap<String, String[]>();
        highestEntryDataSourceAccuracy = new HashMap<String, Integer>();
        
        File[] files = new File("data_To_Process").listFiles();
        for (File file : files) {
            if (file.toString().contains(".csv") &&  !file.toString().contains(".tsv") ) {
                getCSV_Data(file.toString());
            } else if (file.toString().contains(".tsv") &&  !file.toString().contains(".csv") ) {
                getTSV_Data(file.toString());
            } else{
                System.out.println("data Formatted Inappropriately. Terminating Program");
                System.exit(1);
            }
        }
    }
    
    public static void getCSV_Data(String filePath){
       try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            String [] nextLine;
            int headerPassed = 0;
            String[] entryData;
            while (( nextLine = reader.readNext()) != null) {
                entryData = new String[6]; 
                for (int x = 0; x < nextLine.length; x++ ){
                    if(headerPassed == 1)
                    {
                        entryData[x] = ( nextLine[x] );
                    }
                    if (x == 5)
                        headerPassed = 1;
                }
                String sku = entryData[2];
                
                addFinalData(sku, entryData, filePath);
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
   }
    
    public static void getTSV_Data(String fileName){
       try {
            FileInputStream inStream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String lineFetched; String[] itemsFetched = new String[0]; int numLineFetched = 0;
            while ((lineFetched = br.readLine()) != null){
                if (numLineFetched == 0)
                    numLineFetched = 1;
                else
                {
                    itemsFetched = lineFetched.split("\t");
                    String sku = itemsFetched[2];
                
                    addFinalData(sku, itemsFetched, fileName);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
   }
    
    public static void addFinalData(String sku, String[] entryData, String filePath){
       if( SKUsToProcessThisRun.contains(sku) )
        {
            String[] insertionData = new String[entryData.length + 2];
            System.arraycopy(entryData, 0, insertionData, 0, entryData.length);
            
            if (!highestEntryDataSourceAccuracy.containsKey( sku ) || highestEntryDataSourceAccuracy.get( sku ) < dataAccuracy.get( filePath ))
            {
                highestEntryDataSourceAccuracy.put( sku , dataAccuracy.get( filePath ));
                insertionData[entryData.length] = String.valueOf( dataAccuracy.get( filePath ) );
                insertionData[entryData.length+1] = filePath;
                finalEntryData.put( sku, entryData);
            }
        }
   }
    
    public static void saveEntriesAsProductFiles(){
        if (alreadySavedProducts != null)
        {   
            finalProducts = new Product[ alreadySavedProducts.length + finalEntryData.size() ]; //creates an array big enough to house products stored in memory and new products found in last run. Final Products will always contain all unique products from all runs.
            System.out.println(alreadySavedProducts.length + " products were loaded from memory" );
        }
        else
        {
            finalProducts = new Product[finalEntryData.size()];
        }
        
        int iterator = 0;
        for (Map.Entry pair : finalEntryData.entrySet()) { //adds all products found on this run to the finalProducts Product array
            String[] entryData = (String[])pair.getValue();
            finalProducts[iterator] = new Product( entryData[0], entryData[1], entryData[2], entryData[3], entryData[4], entryData[5]);
            iterator+=1;
            alreadyProcessedSKUs.add( entryData[2] ); //adds the unique SKUs encountered to a hash table for memory storage.
        }
        
        if (alreadySavedProducts != null) 
        {
            for(int x = 0; x < alreadySavedProducts.length; x++)
                finalProducts[finalEntryData.size() + x] = alreadySavedProducts[x]; //adds all products from memory to the end of the finalProducts array, which contained only those unique products found this run before this line.
        }
            
        
        WriteObjectToFile(finalProducts, "products/savedProducts");  
        System.out.println( "final array of unique products succesfully saved to products/savedProducts. Contains unique products from all runs." );
        WriteObjectToFile(alreadyProcessedSKUs, "var/processedSKUs"); 
    }
    
    public static void WriteObjectToFile(Object serObj, String filepath){
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
       }
       catch (Exception e){
           System.out.println(e.toString());
       }
    }
    
    public static ObjectInputStream ReadObjectFromFile(String filepath){
        try{
            FileInputStream fi = new FileInputStream(new File(filepath));
            ObjectInputStream oi = new ObjectInputStream(fi);
            return oi;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }
    
    public static void bounceProductFiles(){
        ProductXmlUtil.bounceToXML(finalProducts);
        ProductJsonUtil.bounceToJson(finalProducts);
    }
}
