package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;
import java.util.Vector;

import it.polimi.deib.ds4m.main.model.dataSources.DataSource;

public class VDC 
{	
	//vector of requirements
	private ArrayList<DataManagement> dataManagement;
	private ArrayList<AbstractProperty> abstractProperties;
	private Vector<DataSource> DataSources;
	private String id;
	
	
	/**
	 * @return the dataSources
	 */
	public Vector<DataSource> getDataSources() {
		return DataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(Vector<DataSource> dataSources) {
		DataSources = dataSources;
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
	
	private void connect(TreeStructure treeStructure, Vector<Attribute> attributes)
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

	
	
	
}
