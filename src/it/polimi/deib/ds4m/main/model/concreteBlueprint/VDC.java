package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Resource;

public class VDC 
{	
	//vector of requirements
	private ArrayList<DataManagement> dataManagement;
	private ArrayList<AbstractProperty> abstractProperties;
	private ArrayList<DataSource> dataSources;
	private String id;
	private ArrayList<Resource> resources;
	
    @JsonIgnore
    private ArrayList<Movement> movements;
	
	
	/**
	 * @return the dataSources
	 */
	public ArrayList<DataSource> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(ArrayList<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the dataManagement
	 */
	public ArrayList<DataManagement> getDataManagement() {
		return dataManagement;
	}

	/**
	 * @param dataManagement the dataManagement to set
	 */
	public void setDataManagement(ArrayList<DataManagement> dataManagement) {
		this.dataManagement = dataManagement;
	}

	/**
	 * @return the abstractProperties
	 */
	public ArrayList<AbstractProperty> getAbstractProperties() {
		return abstractProperties;
	}

	/**
	 * @param abstractProperties the abstractProperties to set
	 */
	public void setAbstractProperties(ArrayList<AbstractProperty> abstractProperties) {
		this.abstractProperties = abstractProperties;
	}

	/**
	 * connects the attributes in the abstract properties with the attributes in the data management, using the id link.
	 * it populates the  attribute linked of the leaves in the abstract property objects.
	 */
	public void connectAbstractProperties()
	{
		for (AbstractProperty abstractProperty : abstractProperties)
		{
			//select the data management with correspondent methodID
			DataManagement selectedDM = null;
			for(DataManagement dm : dataManagement)
			{
				if (abstractProperty.getMethod_id().equals(dm.getMethod_id()))
				{
					selectedDM = dm;
					break;
				}
			}
			
			if (selectedDM == null)
				return;
			
			//for each tree
			//navigate at the end of the tree
			
			//dataUtility
			connect(abstractProperty.getGoalTrees().getDataUtility(), selectedDM.getAttributes().dataUtility);
			
			//privacy
			connect(abstractProperty.getGoalTrees().getPrivacy(), selectedDM.getAttributes().privacy);
			
			//security
			connect(abstractProperty.getGoalTrees().getSecurity(), selectedDM.getAttributes().security);

			
		}
	}
	
	/**
	 * recursive function that inspect the tree structure, examine the IDs and connects the attributes of absract properties wityh data management.
	 * 
	 * @param treeStructure
	 * @param attributes
	 */
	private void connect(TreeStructure treeStructure, ArrayList<Attribute> attributes)
	{
		if (treeStructure.getLeaves()==null)
			for (TreeStructure cildren : treeStructure.getChildern())
				connect(cildren, attributes);
		else
			for (Goal leaf : treeStructure.getLeaves())
				for (String attributeLeaf: leaf.getAttributes())
					for (Attribute attributeDM : attributes)
						if (attributeDM.getId().equals(attributeLeaf))
							leaf.getAttributesLinked().add(attributeDM);
							
				
	}

	
	/**
	 * @return the movements
	 */
	public ArrayList<Movement> getMovements() {
		return movements;
	}
	/**
	 * @param movements the movements to set
	 */
	public void setMovements(ArrayList<Movement> movements) {
		this.movements = movements;
	}

	/**
	 * @return the resources
	 */
	public ArrayList<Resource> getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}
	
	
	
}
