package it.polimi.deib.ds4m.main.movement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;

public class ManageMovementsActions 
{
	public static Boolean instantiateMovementActions(List<DataSource> dataSources, String movementsJSON) 
	{
		//deep copy movements, raggiunto serializzando di volta in volta
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(movementsJSON);
			
			//retrieve movements
			JsonNode movementNode = root.get("movements");		
			Vector<Movement> movements;
			
			//assigned copied 
			for(DataSource dataSource: dataSources)
			{
				movements = new Vector<Movement> (Arrays.asList(mapper.treeToValue(movementNode, Movement[].class)));
				dataSource.setMovements(movements);
				movements = null;
			}
			
			return true;
			
		} catch (IOException e) 
		{
			return false;
		}

	}

}
