package de.bwdesign.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

public class Print {
	
	IReportEngine repeng;
	EngineConfig repconf;
	
	public Print() {
		repconf = aBirtEngine("birt-runtime");
		repeng = aReportEngine(repconf);
		
	}
	
	
	public boolean runReport(File report,File pdf) {
		FileOutputStream pout = null;
		try {
			pout = new FileOutputStream(pdf);
			
		} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	 
		try {
			String reports = report.getAbsolutePath();
			IReportRunnable design = repeng.openReportDesign(reports);
			IRunAndRenderTask task = repeng.createRunAndRenderTask(design);
			 
			
			PDFRenderOption pdfOptions = new PDFRenderOption();
			
			//  String emitterID = "org.eclipse.birt.report.engine.emitter.pdf";
			 // pdfOptions.setEmitterID(emitterID);
			//  pdfOptions.setOutputFormat("pdf");
			pdfOptions.setEmitterID(RenderOption.OUTPUT_EMITTERID_PDF);
			String of = pdfOptions.getOutputFormat();
			pdfOptions.setOutputStream(pout);
			task.setRenderOption(pdfOptions);
			
			task.run();
			
			task.getRenderOption().getOutputStream().close();
			task.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return true;
		
	}
	
	private IReportEngine aReportEngine(EngineConfig repconf) {
		try {
			IPlatformContext context = repconf.getPlatformContext();
			Platform.startup(repconf);
			IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			if (factory == null) {
				return null;
			}
			repeng = factory.createReportEngine(repconf);

		}catch(Exception e) {
			e.printStackTrace();
		}
	 
		return repeng;
	}
	
	private EngineConfig aBirtEngine(String runtimedir) {
		File runtime = new File(runtimedir);
		File path = runtime.getAbsoluteFile();
		String ppath = path.getParent();
		if (!runtime.exists()) {
			return null;
		}
		runtime = new File("logs");
		if (!runtime.exists()) {
			return null;
		}
		EngineConfig repconf = new EngineConfig();
		
		File bh = new File("birt-runtime/ReportEngine");
		if (bh.exists()) {
			File pl = new File(bh, "plugins");
			if (pl.exists()) {
				repconf.setEngineHome(bh.getAbsolutePath());
				repconf.setBIRTHome(bh.getAbsolutePath());
			}else {	
				pl = new File(bh, "platform");
				repconf.setEngineHome(pl.getAbsolutePath());
				repconf.setBIRTHome(pl.getAbsolutePath());
			}
			
		 
		}
		 
		repconf.setResourcePath(new File(ppath, "res/reports").getAbsolutePath());
		repconf.setLogConfig(new File(ppath, "logs").getAbsolutePath(), Level.SEVERE);

		return repconf;
		
		
		
		
		
		
		
	}
}
