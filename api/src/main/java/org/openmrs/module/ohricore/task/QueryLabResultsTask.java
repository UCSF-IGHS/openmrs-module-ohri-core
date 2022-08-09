package org.openmrs.module.ohricore.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.ohricore.fhir.FhirRemoteAccessHelper;
import org.openmrs.scheduler.tasks.AbstractTask;

import static org.openmrs.module.ohricore.OhriCoreConstant.MUTEX_QUERY_LABRESULTS;

/**
 * @author smallGod date: 27/07/2022
 */
public class QueryLabResultsTask extends AbstractTask {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void execute() {
		
		if (!isExecuting) {
			
			if (logger.isDebugEnabled())
				logger.debug("QueryLabResultsTask running...");
			
			startExecuting();
			
			try {
				
				FhirRemoteAccessHelper fhirAccess = new FhirRemoteAccessHelper();
				synchronized (MUTEX_QUERY_LABRESULTS) {
					fhirAccess.fetchCompletedLabResults();
					fhirAccess.fetchRejectedLabResults();
				}
				
			}
			catch (Exception e) {
				logger.error("Error while running QueryLabResultsTask: ", e);
			}
			finally {
				stopExecuting();
			}
		}
	}
}
