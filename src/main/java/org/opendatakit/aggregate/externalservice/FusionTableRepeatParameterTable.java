/*
 * Copyright (C) 2010 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.aggregate.externalservice;

import java.util.ArrayList;
import java.util.List;

import org.opendatakit.aggregate.datamodel.FormElementKey;
import org.opendatakit.common.persistence.CommonFieldsBase;
import org.opendatakit.common.persistence.DataField;
import org.opendatakit.common.persistence.Datastore;
import org.opendatakit.common.persistence.PersistConsts;
import org.opendatakit.common.persistence.Query;
import org.opendatakit.common.persistence.Query.FilterOperation;
import org.opendatakit.common.persistence.exception.ODKDatastoreException;
import org.opendatakit.common.security.User;
import org.opendatakit.common.web.CallingContext;

/**
 * 
 * @author wbrunette@gmail.com
 * @author mitchellsundt@gmail.com
 * 
 */
public final class FusionTableRepeatParameterTable extends CommonFieldsBase {

	  private static final String TABLE_NAME = "_fusion_table_repeat";

	  private static final DataField URI_FUSION_TABLE_PROPERTY = new DataField(
			  "URI_FUSION_TABLE", DataField.DataType.URI, false, PersistConsts.URI_STRING_LEN);
	  private static final DataField FUSION_TABLE_ID_PROPERTY = new DataField(
			  "FUSION_TABLE_ID", DataField.DataType.STRING, true, 4096L);
	  private static final DataField FORM_ELEMENT_KEY_PROPERTY = new DataField(
			  "FORM_ELEMENT_KEY", DataField.DataType.STRING, true, 4096L);

		/**
		 * Construct a relation prototype. Only called via {@link #assertRelation(CallingContext)}
		 * 
		 * @param databaseSchema
		 * @param tableName
		 */
	  FusionTableRepeatParameterTable(String schemaName) {
	    super(schemaName, TABLE_NAME);
	    fieldList.add(URI_FUSION_TABLE_PROPERTY);
	    fieldList.add(FUSION_TABLE_ID_PROPERTY);
	    fieldList.add(FORM_ELEMENT_KEY_PROPERTY);
	  }

	  /**
	   * Construct an empty entity.  Only called via {@link #getEmptyRow(User)}
	   * 
	   * @param ref
	   * @param user
	   */
	  private FusionTableRepeatParameterTable(FusionTableRepeatParameterTable ref, User user) {
	    super(ref, user);
	  }

	  // Only called from within the persistence layer.
	  @Override
	  public FusionTableRepeatParameterTable getEmptyRow(User user) {
		return new FusionTableRepeatParameterTable(this, user);
	  }

	  public String getUriFusionTable() {
	    return getStringField(URI_FUSION_TABLE_PROPERTY);
	  }

	  public void setUriFusionTable(String value) {
	    if (!setStringField(URI_FUSION_TABLE_PROPERTY, value)) {
	      throw new IllegalArgumentException("overflow uriFusionTable");
	    }
	  }

	  public String getFusionTableId() {
	    return getStringField(FUSION_TABLE_ID_PROPERTY);
	  }

	  public void setFusionTableId(String value) {
	    if (!setStringField(FUSION_TABLE_ID_PROPERTY, value)) {
	      throw new IllegalArgumentException("overflow fusionTableId");
	    }
	  }

	  public FormElementKey getFormElementKey() {
		String key = getStringField(FORM_ELEMENT_KEY_PROPERTY);
		if ( key == null ) return null;
		return new FormElementKey(key);
	  }

	  public void setFormElementKey(FormElementKey value) {
	    if (!setStringField(FORM_ELEMENT_KEY_PROPERTY, value.toString())) {
	      throw new IllegalArgumentException("overflow formElementKey");
	    }
	  }

	  private static FusionTableRepeatParameterTable relation = null;

	  public static synchronized final FusionTableRepeatParameterTable assertRelation(CallingContext cc)
	      throws ODKDatastoreException {
	    if (relation == null) {
	    	FusionTableRepeatParameterTable relationPrototype;
	    	Datastore ds = cc.getDatastore();
	    	User user = cc.getUserService().getDaemonAccountUser();
	        relationPrototype = new FusionTableRepeatParameterTable(ds.getDefaultSchemaName());
	        ds.assertRelation(relationPrototype, user); // may throw exception...
	        // at this point, the prototype has become fully populated
	        relation = relationPrototype; // set static variable only upon success...
	    }
	    return relation;
	  }
	  
	  public static List<FusionTableRepeatParameterTable> getRepeatGroupAssociations(String uri,
			  												CallingContext cc) throws ODKDatastoreException {
		  List<FusionTableRepeatParameterTable> list = new ArrayList<FusionTableRepeatParameterTable> ();
		  FusionTableRepeatParameterTable frpt = assertRelation(cc);

		  Query query = cc.getDatastore().createQuery(frpt, cc.getCurrentUser());
		  query.addFilter(URI_FUSION_TABLE_PROPERTY, FilterOperation.EQUAL, uri);

		  List<? extends CommonFieldsBase> results = query.executeQuery();
		  for ( CommonFieldsBase b : results ) {
			  list.add((FusionTableRepeatParameterTable) b);
		  }
		  return list;
	  }
}