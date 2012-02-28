package all;

import work.CalcConf;
import work.CalcPayment;
import work.CalcPaymentImpl;
import work.Controller;
import work.ProjCodeChecker;
import work.ProjCodeCheckerImpl;
import work.VcdConf;
import work.VcdConfImpl;
import work.WorkCalcConf;
import work.bat.MaxCostCheckTask;
import work.bat.MaxCostRecetTask;
import work.bat.SendCostMailTask;
import work.util.Sender;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(CalcPayment.class).to(CalcPaymentImpl.class).in(Scopes.SINGLETON);
		bind(ProjCodeChecker.class).to(ProjCodeCheckerImpl.class).in(
				Scopes.SINGLETON);
		bind(CalcConf.class).to(WorkCalcConf.class).in(Scopes.SINGLETON);
		bind(Controller.class).in(Scopes.SINGLETON);
		bind(VcdConf.class).to(VcdConfImpl.class).in(Scopes.SINGLETON);
		bind(SendCostMailTask.class);
		bind(MaxCostCheckTask.class);
		bind(MaxCostRecetTask.class);
		bind(SendCostMailTask.class);
		bind(Sender.class).in(Scopes.SINGLETON);
		;

	}
}
