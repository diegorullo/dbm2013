package utils;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GraphEngine {
	private static ProjectController projectController;
	private static Workspace workspace;
	private static GraphModel graphModel;

	private static GraphEngine ge;

	private GraphEngine() {
		ge = this;
		ge.init();
	}

	public void init() {
		if (projectController == null) {
			projectController = Lookup.getDefault().lookup(ProjectController.class);
			projectController.newProject();
			workspace = projectController.getCurrentWorkspace();
			graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		}
	}
	
	public static GraphEngine getGraphEngine() {
		if(ge == null) {
			ge = new GraphEngine();
		}
		return ge;
	}
	
	public ProjectController getProjectController() {
		return projectController;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public GraphModel getGraphModel() {
		return graphModel;
	}
}
