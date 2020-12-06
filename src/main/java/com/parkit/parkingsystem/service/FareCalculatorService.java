package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static final String String = null;

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double duration;
		String numeroVehicule = ticket.getVehicleRegNumber();

		Date debStationnement = ticket.getInTime();
		Instant instantdeb = debStationnement.toInstant();
		ZonedDateTime zonedeb = instantdeb.atZone(ZoneId.systemDefault());
		LocalDateTime localDateDeb = zonedeb.toLocalDateTime();

		Date finStationnement = ticket.getOutTime();
		Instant instantfin = finStationnement.toInstant();
		ZonedDateTime zonefin = instantfin.atZone(ZoneId.systemDefault());
		LocalDateTime localDateFin = zonefin.toLocalDateTime();

		long duree = Duration.between(localDateDeb, localDateFin).getSeconds();

		// Duree exprimée en secondes
		// Duration en minutes avec une préxision au centième de minutes
		duration = (StrictMath.round(duree / 60));
		duration = duration / 60;

		if (duree > 3599) {
			duration = StrictMath.round(duree / 3600);
		}

		if ((numeroVehicule != null) && (duration != 0)) {

			if (ticket.getClass() != null) {
				duration = duration * 0.95;
			}

			// Réduction de 5% pour les clients récurrents c'est à dire ayant rentré plus
			// d'une fois dans le parking

		}
		double price = 0;
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			// ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
			price = (duration * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			// ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
			price = (duration * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
		price = Math.round(price * 100) * 0.01;
		ticket.setPrice(price);
	}
}