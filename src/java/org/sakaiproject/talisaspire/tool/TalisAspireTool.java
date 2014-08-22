/**
 * Copyright 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.talisaspire.tool;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.ToolManager;

import org.apache.log4j.Logger;
import org.sakaiproject.component.api.ComponentManager;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.exception.IdUnusedException;

/**
 * Hosts your Talis Aspire module page in an iframe.
 * 
 * @author Adrian Fish (a.fish@lancaster.ac.uk)
 */
public class TalisAspireTool extends HttpServlet {
	private Logger logger = Logger.getLogger(TalisAspireTool.class);

    private SiteService siteService;
    private ToolManager toolManager;
	private ServerConfigurationService serverConfigurationService;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (logger.isDebugEnabled())
			logger.debug("doGet()");
		
		String host = serverConfigurationService.getString("talisaspire.host","http://localhost");
		System.out.println("HOST: " + host);
		String hierarchy = serverConfigurationService.getString("talisaspire.hierarchy","lists");
		boolean useSiteIdAsTalisCode = serverConfigurationService.getBoolean("talisaspire.useSiteIdAsTalisCode",false);
		
		String siteId = toolManager.getCurrentPlacement().getContext();
		Site site = null;
		try {
			site = siteService.getSite(siteId);
		} catch (IdUnusedException e) {
			e.printStackTrace();
		}
		
		String talisCode = "";
		if(useSiteIdAsTalisCode) {
			talisCode = site.getId();
		} else {
			ResourceProperties props = site.getProperties();
			talisCode = props.getProperty("taliscode");
			if(talisCode == null || talisCode.length() <= 0) {
				talisCode = site.getTitle();
			}
		}
		String toolId = toolManager.getCurrentPlacement().getId();
		
		String url = host + "/" + hierarchy + "/" + talisCode + "/lists";
		req.setAttribute("aspireURL", url);
		req.setAttribute("toolId", toolId);

        res.setContentType("text/html");
        res.setStatus(HttpServletResponse.SC_OK);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
        dispatcher.include(req,res);
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		if (logger.isDebugEnabled())
			logger.debug("init");

		try {
			ComponentManager componentManager = org.sakaiproject.component.cover.ComponentManager.getInstance();
			siteService = (SiteService) componentManager.get(SiteService.class);
			toolManager = (ToolManager) componentManager.get(ToolManager.class);
			serverConfigurationService = (ServerConfigurationService) componentManager.get(ServerConfigurationService.class);
		} catch (Throwable t) {
			throw new ServletException("Failed to initialise TalisAspireTool.", t);
		}
	}
}
