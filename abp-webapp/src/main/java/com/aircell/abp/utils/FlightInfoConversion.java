package com.aircell.abp.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aircell.abp.model.FlightStatus;


/*Added By Hardik for Decimal to Degree Conversion of Flight Info*/


public class FlightInfoConversion
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    long i = 1000000;
    long j = 100000;
    int sec = 60;

    public FlightInfoConversion(){

    }

    public Map<String, String>  decToDegLatitude(FlightStatus objFlightStatus)
    {


        Map<String,String> latitudes = new HashMap<String,String>();



    //    logger.info("Dec to Deg Latitude Enters");
         int signlat = 1;

         long latAbs=0;

         String deglat="";
         String minlat="";
         String seclat="";
         String strAltitude="";
         long intAltitude=0;
         float decLatitude= 0;
         float altitude=0;
         double feetAltitude=0;
         StringBuffer sb=new StringBuffer();
         try{
         decLatitude=objFlightStatus.getLatitude();
         altitude=objFlightStatus.getAltitude();
         /*Meters to Feet conversion of Altitude*/
         feetAltitude=3.2808399*altitude;
         intAltitude=Math.abs(Math.round(feetAltitude));
         if(decLatitude < 0)  { signlat = -1; }
         latAbs = Math.abs(Math.round(decLatitude * 1000000.));
         if(latAbs > (90 * i)) {
         decLatitude = 0;
         latAbs=0;
         }
        strAltitude=""+(long)intAltitude;
        sb=sb.append(intAltitude);
        for(int i=sb.length()-3;i>0;i-=3){
            sb=sb.insert(i,',');
        }
        strAltitude=sb.toString();
        deglat = ""+(int)((Math.floor(latAbs / i) * signlat));
        minlat=""+(int)Math.floor(((latAbs/i) - Math.floor(latAbs/i)) * sec);
         if(signlat>=0){
        seclat=""+(int)( Math.floor(((((latAbs/i) - Math.floor(latAbs/i)) * sec)
        - Math.floor(((latAbs/i) - Math.floor(latAbs/i)) * sec)) * j)
        *sec/j )+"\""+"N";
        }
         else{
        seclat=""+(int)( Math.floor(((((latAbs/i) - Math.floor(latAbs/i)) * sec)
        - Math.floor(((latAbs/i) - Math.floor(latAbs/i)) * sec)) * j) *sec/j )
        +"\""+"S";
        }
        }catch(Exception e){
            logger.warn("FlightInfoConversion.decToDegLatitude:"
                    + " Unable to convert the latitude value",e);
        }
        signlat=1;
        latitudes.put("intAltitude", strAltitude);
        latitudes.put("degLatitude", deglat);
        latitudes.put("minLatitude", minlat);
        latitudes.put("secLatitude", seclat);
    //    logger.info("Dec to Deg Latitude Exit");
        return latitudes;
    }

    public Map<String, String>
    decToDegreeLongitude(FlightStatus objFlightStatus)
    {
        Map<String,String> longitudes = new HashMap<String,String>();

    //    logger.info("DecToDegreeLongitude Enter");
        int signlon = 1;
        long lonAbs=0;
        String deglon="";
        String minlon="";
        String seclon="";
        float decLongitude =0;
            try{
        decLongitude = objFlightStatus.getLongitude();
        if(decLongitude < 0)  { signlon = -1; }
         lonAbs = Math.abs(Math.round(decLongitude * 1000000.));
         if(lonAbs > (180 * i)) {
         decLongitude=0;
         lonAbs=0;
         }

         deglon = ""+(int)((Math.floor(lonAbs / i) * signlon));
         minlon=""+(int)Math.floor(((lonAbs/i) - Math.floor(lonAbs/i)) * sec);
         if(signlon>=0)
          seclon=""+(int)( Math.floor(((((lonAbs/i) - Math.floor(lonAbs/i))
          * sec) - Math.floor(((lonAbs/i) - Math.floor(lonAbs/i)) * sec)) * j)
          *sec/j )+"\""+"E";
         else
             seclon=""+(int)( Math.floor(((((lonAbs/i) - Math.floor(lonAbs/i))
          * sec) - Math.floor(((lonAbs/i) - Math.floor(lonAbs/i)) * sec)) * j)
             *sec/j )+"\""+"W";

         signlon=1;
         }catch(Exception e){
             e.printStackTrace();
             logger.warn("FlightInfoConversion.decToDegLatitude: "
                    + "Unable to convert the value to longitude",e);
         }
    //     logger.info("Deg longitude"+deglon);

         longitudes.put("degLongitude", deglon);
         longitudes.put("minLongitude", minlon);
         longitudes.put("secLongitude", seclon);
    //     logger.info("DecToDegreeLongitude Exit");
         return longitudes;
    }
}
