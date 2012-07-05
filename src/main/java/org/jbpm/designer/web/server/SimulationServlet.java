package org.jbpm.designer.web.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.bpmn2.Definitions;
import org.jbpm.designer.bpmn2.impl.Bpmn2JsonUnmarshaller;
import org.jbpm.designer.web.profile.IDiagramProfile;
import org.jbpm.simulation.PathFinder;
import org.jbpm.simulation.PathFinderFactory;
import org.jbpm.simulation.converter.JSONPathFormatConverter;
import org.json.JSONObject;

/**
 * Sevlet for simulation actions.
 * 
 * @author Tihomir Surdilovic
 */
public class SimulationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger _logger = Logger.getLogger(SimulationServlet.class);
	private static final String ACTION_GETPATHINFO = "getpathinfo";
	private ServletConfig config;
	
	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
    }
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		String profileName = req.getParameter("profile");
		String json = req.getParameter("json");
		String action = req.getParameter("action");
		String preprocessingData = req.getParameter("ppdata");
		
		IDiagramProfile profile = ServletUtil.getProfile(req, profileName, getServletContext());
        
        if(action != null && action.equals(ACTION_GETPATHINFO)) {
        	Bpmn2JsonUnmarshaller unmarshaller = new Bpmn2JsonUnmarshaller();
            Definitions def = ((Definitions) unmarshaller.unmarshall(json, preprocessingData).getContents().get(0));
            PathFinder pfinder = PathFinderFactory.getInstance(def);
            JSONObject pathjson =  pfinder.findPaths(new JSONPathFormatConverter());
            PrintWriter pw = resp.getWriter();
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			pw.write(pathjson.toString());
        }
        
	}
}
