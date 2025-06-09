package cameoExamplePlugin;

import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.options.ProjectOptions;
import com.nomagic.magicdraw.plugins.Plugin;
import com.nomagic.magicdraw.ui.browser.Browser;

@SuppressWarnings("unused")
public class CameoExamplePlugin extends Plugin {

	CameoExamplePluginModel model;
	CameoExamplePluginView view;
	CameoExamplePluginController controller;
	CameoExampleProjectOptions projectOptions;

	@Override
	public void init() {
		projectOptions = new CameoExampleProjectOptions();
		ProjectOptions.addConfigurator(projectOptions);
		Browser.addBrowserInitializer(new Browser.BrowserInitializer() {
			@Override
			public void init(Browser browser, Project project) {
				model = new CameoExamplePluginModel(project);
				view = new CameoExamplePluginView(model);
				controller = new CameoExamplePluginController(view, model);
				browser.addPanel(view);
			}

			@Override
			public WindowComponentInfoRegistration getInfo() {
				return new WindowComponentInfoRegistration(view.info, null);
			}
		});
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean isSupported() {
		return true;
	}
}

