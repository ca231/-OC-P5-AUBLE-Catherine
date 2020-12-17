package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");
	private static final String CONNEXIONIMPOSSIBLE = "Connection impossible à la base";
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public int getNextAvailableSlot(ParkingType parkingType) throws Exception, SQLException {
		Connection con = null;
		int result = -1;

		con = dataBaseConfig.getConnection();
		if (con == null) {
			throw new IllegalArgumentException(CONNEXIONIMPOSSIBLE);
		}

		try (PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)) {
			ps.setString(1, parkingType.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
		} catch (Exception ex) {
			logger.info("Error fetching next available slot {0}", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}

	public boolean updateParking(@NonNull ParkingSpot parkingSpot) throws ClassNotFoundException, SQLException {
		// update the availability for that parking slot
		Connection con = null;
		con = dataBaseConfig.getConnection();
		if (con == null) {
			throw new IllegalArgumentException(CONNEXIONIMPOSSIBLE);
		}
		try (PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)) {
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			ps.execute();
			dataBaseConfig.closePreparedStatement(ps);
			return true;
		} catch (Exception ex) {
			logger.info("Error updating parking info {0}", ex);
			return false;
		} finally {
			dataBaseConfig.closeConnection(con);
		}
	}

}