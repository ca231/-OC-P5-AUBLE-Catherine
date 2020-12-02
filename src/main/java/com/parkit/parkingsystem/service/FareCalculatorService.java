package com.parkit.parkingsystem.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double duration;
       
        Date debStationnement = ticket.getInTime();
        Instant instantdeb = debStationnement.toInstant();
        ZonedDateTime zonedeb = instantdeb.atZone(ZoneId.systemDefault());
        LocalDateTime localDateDeb = zonedeb.toLocalDateTime();
        
        Date finStationnement =  ticket.getOutTime();
        Instant instantfin = finStationnement.toInstant();
        ZonedDateTime zonefin = instantfin.atZone(ZoneId.systemDefault());
        LocalDateTime localDateFin = zonefin.toLocalDateTime();
        
        //long finStationnement = ticket.getOutTime();
       
        long duree = Duration.between(localDateDeb,localDateFin).getSeconds();
        
        System.out.println(duree);
        
        // Duree exprimée en secondes 
        duration = (double)(StrictMath.round( duree / 60));
        duration = duration / 60;
        
        if ( duree > 3599 ) {
        	duration = StrictMath.round(duree / 3600);
        	System.out.println(duration);
        }
        
        
        System.out.println(duration);

        
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}